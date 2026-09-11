package uk.gov.companieshouse.addresslookup;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
 * Integration tests for the /addresses endpoint.
 * Tests the full address lookup response with total results and address list.
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
class AddressesGbControllerIT {

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
    void shouldReturnAddressesForPostcode() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .queryParam("postcode", "WF2 7QD")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.addresses[0].udprn").value(26394069))
                .andExpect(jsonPath("$.addresses[0].postcode").value("WF2 7QD"));
    }

    @Test
    void shouldNormalizePostcodeWithoutSpaces() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .queryParam("postcode", "WF27QD")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.addresses[0].postcode").value("WF2 7QD"))
                .andExpect(jsonPath("$.addresses[0].udprn").value(26394069));
    }

    @Test
    void shouldNormalizePostcodeWithExtraWhitespace() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .queryParam("postcode", "  WF2  7QD  ")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.addresses[0].postcode").value("WF2 7QD"))
                .andExpect(jsonPath("$.addresses[0].udprn").value(26394069));
    }

    @Test
    void shouldHandleLowercasePostcode() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .queryParam("postcode", "wf2 7qd")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.addresses[0].postcode").value("WF2 7QD"))
                .andExpect(jsonPath("$.addresses[0].udprn").value(26394069));
    }

    // ========================
    // Validation Tests
    // ========================

    @Test
    void shouldReturnBadRequestWhenPostcodeIsEmpty() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .queryParam("postcode", "")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeIsMissing() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeIsWhitespace() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .queryParam("postcode", "   ")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeIsTooLong() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .queryParam("postcode", "A VERY LONG POSTCODE")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeContainsInvalidCharacters() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .queryParam("postcode", "INVALID@POSTCODE")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeContainsSpaces() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .queryParam("postcode", "INVALID POSTCODE")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnEmptyResultsWhenPostcodeHasNoAddress() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .queryParam("postcode", "ZZ1 1ZZ")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(0))
                .andExpect(jsonPath("$.addresses").isArray())
                .andExpect(jsonPath("$.addresses").isEmpty());
    }

    @Test
    void shouldReturnEmptyResultsWhenPostcodeIsInvalid() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/addresses")
                        .queryParam("postcode", "INVALID")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(0))
                .andExpect(jsonPath("$.addresses").isArray())
                .andExpect(jsonPath("$.addresses").isEmpty());
    }
}
