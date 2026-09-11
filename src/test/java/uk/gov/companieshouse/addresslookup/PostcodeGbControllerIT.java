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
 * Integration tests for the /postcode endpoint.
 * Tests the single address lookup without premise, including validation edge cases.
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
class PostcodeGbControllerIT {

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
    void shouldReturnLegacyAddressWithoutPremiseForPostcode() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "WF2 7QD")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postcode").value("WF2 7QD"))
                .andExpect(jsonPath("$.premise").doesNotExist())
                .andExpect(jsonPath("$.addressLine1").value("WOODMOOR ROAD"))
                .andExpect(jsonPath("$.postTown").value("WAKEFIELD"))
                .andExpect(jsonPath("$.country").value("GB-ENG"));
    }

    @Test
    void shouldReturnScottishAddressWithoutPremise() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "AB124NY")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postcode").value("AB12 4NY"))
                .andExpect(jsonPath("$.premise").doesNotExist())
                .andExpect(jsonPath("$.country").value("GB-SCT"));
    }

    @Test
    void shouldReturnAddressWhenPostcodeHasNoSpaces() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "WF27QD")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postcode").value("WF2 7QD"))
                .andExpect(jsonPath("$.addressLine1").value("WOODMOOR ROAD"));
    }

    @Test
    void shouldReturnAddressWhenPostcodeHasExtraWhitespace() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "  WF2  7QD  ")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postcode").value("WF2 7QD"))
                .andExpect(jsonPath("$.addressLine1").value("WOODMOOR ROAD"));
    }

    @Test
    void shouldReturnAddressWhenPostcodeIsLowercase() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "wf2 7qd")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postcode").value("WF2 7QD"))
                .andExpect(jsonPath("$.addressLine1").value("WOODMOOR ROAD"));
    }

    // ========================
    // Validation Tests
    // ========================

    @Test
    void shouldReturnNotFoundWhenPostcodeHasNoAddress() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "ZZ1 1ZZ")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenPostcodeIsInvalid() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "INVALID")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeIsEmpty() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeIsMissing() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeIsWhitespace() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "   ")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenPostcodeIsTooShort() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "A")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeIsTooLong() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "A VERY LONG POSTCODE")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeContainsInvalidCharacters() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "INVALID@POSTCODE")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostcodeContainsSpaces() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "INVALID POSTCODE")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // ========================
    // Response Structure Validation Tests
    // ========================

    @Test
    void shouldVerifyRequiredFieldsExist() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "WF2 7QD")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postcode").exists())
                .andExpect(jsonPath("$.addressLine1").exists())
                .andExpect(jsonPath("$.postTown").exists())
                .andExpect(jsonPath("$.country").exists());
    }

    @Test
    void shouldVerifyAddressLine1IsNotEmpty() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "WF2 7QD")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressLine1").exists())
                .andExpect(jsonPath("$.addressLine1").value("WOODMOOR ROAD"));
    }

    @Test
    void shouldVerifyPremiseFieldDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/postcode")
                        .queryParam("postcode", "WF2 7QD")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.premise").doesNotExist());
    }
}
