package uk.gov.companieshouse.addresslookup;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
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
 * Integration tests for the /multiple-addresses endpoint with ISL (Northern Ireland) postcodes.
 * Tests the legacy address format with premise information and country mapping for Northern Ireland.
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
class MultipleAddressesIslControllerIT {

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
    void shouldReturnLegacyCountryFromIslBuiltAddress() throws Exception {
        this.mockMvc.perform(get("/address-lookup-api/multiple-addresses")
                        .queryParam("postcode", "BT100EQ")
                        .header(REQUEST_ID.value(), "request_id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThan(1)))
                .andExpect(jsonPath("$[0].postcode").value("BT10 0EQ"))
                .andExpect(jsonPath("$[0].premise").value(containsString("1")))
                .andExpect(jsonPath("$[0].addressLine1").value("GARRON CRESCENT"))
                .andExpect(jsonPath("$[0].postTown").value("BELFAST"))
                .andExpect(jsonPath("$[0].country").value("GB-NIR"))
                .andExpect(jsonPath("$[1].postcode").value("BT10 0EQ"))
                .andExpect(jsonPath("$[1].premise").value(containsString("2")))
                .andExpect(jsonPath("$[1].addressLine1").value("GARRON CRESCENT"))
                .andExpect(jsonPath("$[1].postTown").value("BELFAST"))
                .andExpect(jsonPath("$[1].country").value("GB-NIR"));
    }

}
