package uk.gov.companieshouse.addresslookup.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.companieshouse.addresslookup.model.AddressDto;
import uk.gov.companieshouse.addresslookup.service.AddressService;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    private AddressDto testAddress;
    private AddressDto testAddress2;

    @BeforeEach
    void setUp() {
        testAddress = createTestAddress("10", 123456789L, "SW1A1AA");
        testAddress2 = createTestAddress("20", 987654321L, "SW1A1AA");
    }

    private AddressDto createTestAddress(String premises, Long uprn, String postcode) {
        AddressDto address = new AddressDto();
        address.setId(1L);
        address.setUprn(uprn);
        address.setUdprn(12345);
        address.setUprnMatchType("EXACT");
        address.setPremises(premises);
        address.setAddressLine1("Downing Street");
        address.setAddressLine2("");
        address.setPostTown("London");
        address.setCountry("United Kingdom");
        address.setPostcode(postcode);
        address.setIsWelsh(false);
        address.setIsResidential(true);
        address.setClassicationCode("RES");
        address.setClassicationShortDescription("Residential");
        address.setClassicationFullDescription("Residential Building");
        address.setLatitude(new BigDecimal("51.5033"));
        address.setLongitude(new BigDecimal("-0.1276"));
        return address;
    }

    @Test
    void testSearchAddressesByPostcode() throws Exception {
        when(addressService.searchByPostcode("SW1A1AA"))
                .thenReturn(CompletableFuture.completedFuture(testAddress));

        var mvcResult = mockMvc.perform(get("/address-lookup/postcode?postcode=SW1A1AA")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postcode", is("SW1A1AA")))
                .andExpect(jsonPath("$.addressLine1", is("Downing Street")))
                .andExpect(jsonPath("$.addressLine2", is("")))
                .andExpect(jsonPath("$.postTown", is("London")))
                .andExpect(jsonPath("$.country", is("United Kingdom")));
    }

    @Test
    void testSearchMultipleAddressesByPostcode() throws Exception {
        when(addressService.searchMultipleByPostcode("SW1A1AA"))
                .thenReturn(CompletableFuture.completedFuture(java.util.List.of(testAddress, testAddress2)));

        var mvcResult = mockMvc.perform(get("/address-lookup/multiple-addresses/postcode?postcode=SW1A1AA")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addresses", hasSize(2)))
                .andExpect(jsonPath("$.addresses[0].premises", is("10")))
                .andExpect(jsonPath("$.addresses[0].addressLine1", is("Downing Street")))
                .andExpect(jsonPath("$.addresses[0].addressLine2", is("")))
                .andExpect(jsonPath("$.addresses[0].postTown", is("London")))
                .andExpect(jsonPath("$.addresses[0].postcode", is("SW1A1AA")))
                .andExpect(jsonPath("$.addresses[1].premises", is("20")))
                .andExpect(jsonPath("$.addresses[1].addressLine1", is("Downing Street")))
                .andExpect(jsonPath("$.addresses[1].addressLine2", is("")))
                .andExpect(jsonPath("$.addresses[1].postTown", is("London")))
                .andExpect(jsonPath("$.addresses[1].postcode", is("SW1A1AA")));
    }



}
