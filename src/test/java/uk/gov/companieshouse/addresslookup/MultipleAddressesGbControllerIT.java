package uk.gov.companieshouse.addresslookup;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static uk.gov.companieshouse.logging.util.LogContextProperties.REQUEST_ID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Integration tests for the /multiple-addresses endpoint.
 * Tests the legacy address format with premise information and country mapping.
 */
@AutoConfigureMockMvc
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.docker.compose.enabled=false",
                "spring.liquibase.change-log=classpath:db/changelog/db.changelog-local.yaml"
        })
@Testcontainers
class MultipleAddressesGbControllerIT {

    @SuppressWarnings("resource")
    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:18-3.6").asCompatibleSubstituteFor("postgres"))
            .withCreateContainerCmdModifier(command -> command.withPlatform("linux/amd64"));

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES::getDriverClassName);
    }

    @Autowired
    private MockMvc mockMvc;

    // ========================
    // Happy Path Tests
    // ========================

    @Test
    void shouldReturnMultipleLegacyAddressesForPostcode() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "AB101AU")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThan(1)))
                .andExpect(jsonPath("$[0].postcode").value("AB10 1AU"))
                .andExpect(jsonPath("$[0].premise").value("14"))
                .andExpect(jsonPath("$[0].addressLine1").value("NETHERKIRKGATE"))
                .andExpect(jsonPath("$[0].postTown").value("ABERDEEN"))
                .andExpect(jsonPath("$[0].country").value("GB-SCT"))
                .andExpect(jsonPath("$[1].postcode").value("AB10 1AU"))
                .andExpect(jsonPath("$[1].premise").value("FLAT 1, 16"))
                .andExpect(jsonPath("$[1].addressLine1").value("NETHERKIRKGATE"))
                .andExpect(jsonPath("$[1].country").value("GB-SCT"));
    }

    @Test
    void shouldReturnLegacyCountryFromGbBuiltAddress() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "AB124NY")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThan(1)))
                .andExpect(jsonPath("$[0].postcode").value("AB12 4NY"))
                .andExpect(jsonPath("$[0].premise").value("2"))
                .andExpect(jsonPath("$[0].country").value("GB-SCT"))
                .andExpect(jsonPath("$[1].postcode").value("AB12 4NY"))
                .andExpect(jsonPath("$[1].premise").value("4"))
                .andExpect(jsonPath("$[1].country").value("GB-SCT"));
    }

    @Test
    void shouldNormalizePostcodeWithoutSpaces() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "AB101AU")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThan(1)))
                .andExpect(jsonPath("$[0].postcode").value("AB10 1AU"))
                .andExpect(jsonPath("$[0].premise").value("14"));
    }

    @Test
    void shouldNormalizePostcodeWithExtraWhitespace() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "  AB10  1AU  ")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThan(1)))
                .andExpect(jsonPath("$[0].postcode").value("AB10 1AU"))
                .andExpect(jsonPath("$[0].premise").value("14"));
    }

    @Test
    void shouldHandleLowercasePostcode() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "ab10 1au")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThan(1)))
                .andExpect(jsonPath("$[0].postcode").value("AB10 1AU"))
                .andExpect(jsonPath("$[0].premise").value("14"));
    }

    // ========================
    // Validation Tests
    // ========================

    @Test
    void shouldReturnBadRequestWhenPostcodeIsEmpty() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeIsMissing() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeIsWhitespace() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "   ")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnEmptyArrayWhenPostcodeIsTooShort() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "A")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeIsTooLong() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "A VERY LONG POSTCODE")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeContainsInvalidCharacters() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "INVALID@POSTCODE")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeContainsSpaces() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "INVALID POSTCODE")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnEmptyArrayWhenPostcodeHasNoAddress() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "ZZ1 1ZZ")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldReturnEmptyArrayWhenPostcodeIsInvalid() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "INVALID")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldVerifyAllAddressesHavePremiseField() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "AB10 1AU")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThan(1)))
                .andExpect(jsonPath("$[0].premise").exists())
                .andExpect(jsonPath("$[1].premise").exists())
                .andExpect(jsonPath("$[2].premise").exists());
    }

    @Test
    void shouldVerifyAddressLine1IsNotEmpty() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "AB10 1AU")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].addressLine1").isNotEmpty())
                .andExpect(jsonPath("$[1].addressLine1").isNotEmpty())
                .andExpect(jsonPath("$[2].addressLine1").isNotEmpty());
    }

    @Test
    void shouldHandleEmptyAddressLine2Field() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "AB10 1AU")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].addressLine2").exists())
                .andExpect(jsonPath("$[0].addressLine2").value(""));
    }
}
