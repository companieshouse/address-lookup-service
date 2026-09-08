package uk.gov.companieshouse.addresslookup.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormattedAddressDtoTest {

    private FormattedAddressDto formattedAddress;

    @BeforeEach
    void setUp() {
        formattedAddress = new FormattedAddressDto();
    }

    @Test
    void testNoArgConstructor() {
        FormattedAddressDto address = new FormattedAddressDto();
        assertNotNull(address);
        assertNull(address.getPostcode());
        assertNull(address.getAddressLine1());
        assertNull(address.getAddressLine2());
        assertNull(address.getPostTown());
        assertNull(address.getCountry());
    }

    @Test
    void testAllArgsConstructor() {
        FormattedAddressDto address = new FormattedAddressDto(
                "SW1A1AA",
                "Downing Street",
                "Westminster",
                "London",
                "United Kingdom"
        );

        assertNotNull(address);
        assertEquals("SW1A1AA", address.getPostcode());
        assertEquals("Downing Street", address.getAddressLine1());
        assertEquals("Westminster", address.getAddressLine2());
        assertEquals("London", address.getPostTown());
        assertEquals("United Kingdom", address.getCountry());
    }

    @Test
    void testSetAndGetPostcode() {
        formattedAddress.setPostcode("NP22 5JY");
        assertEquals("NP22 5JY", formattedAddress.getPostcode());
    }

    @Test
    void testSetAndGetAddressLine1() {
        formattedAddress.setAddressLine1("GARDEN CITY");
        assertEquals("GARDEN CITY", formattedAddress.getAddressLine1());
    }

    @Test
    void testSetAndGetAddressLine2() {
        formattedAddress.setAddressLine2("RHYMNEY");
        assertEquals("RHYMNEY", formattedAddress.getAddressLine2());
    }

    @Test
    void testSetAndGetPostTown() {
        formattedAddress.setPostTown("TREDEGAR");
        assertEquals("TREDEGAR", formattedAddress.getPostTown());
    }

    @Test
    void testSetAndGetCountry() {
        formattedAddress.setCountry("GB-WLS");
        assertEquals("GB-WLS", formattedAddress.getCountry());
    }

    @Test
    void testSetAllFields() {
        formattedAddress.setPostcode("SW1A1AA");
        formattedAddress.setAddressLine1("Downing Street");
        formattedAddress.setAddressLine2("Westminster");
        formattedAddress.setPostTown("London");
        formattedAddress.setCountry("United Kingdom");

        assertEquals("SW1A1AA", formattedAddress.getPostcode());
        assertEquals("Downing Street", formattedAddress.getAddressLine1());
        assertEquals("Westminster", formattedAddress.getAddressLine2());
        assertEquals("London", formattedAddress.getPostTown());
        assertEquals("United Kingdom", formattedAddress.getCountry());
    }

    @Test
    void testNullPostcode() {
        formattedAddress.setPostcode(null);
        assertNull(formattedAddress.getPostcode());
    }

    @Test
    void testNullAddressLine1() {
        formattedAddress.setAddressLine1(null);
        assertNull(formattedAddress.getAddressLine1());
    }

    @Test
    void testNullAddressLine2() {
        formattedAddress.setAddressLine2(null);
        assertNull(formattedAddress.getAddressLine2());
    }

    @Test
    void testNullPostTown() {
        formattedAddress.setPostTown(null);
        assertNull(formattedAddress.getPostTown());
    }

    @Test
    void testNullCountry() {
        formattedAddress.setCountry(null);
        assertNull(formattedAddress.getCountry());
    }

    @Test
    void testEmptyStringFields() {
        formattedAddress.setPostcode("");
        formattedAddress.setAddressLine1("");
        formattedAddress.setAddressLine2("");
        formattedAddress.setPostTown("");
        formattedAddress.setCountry("");

        assertEquals("", formattedAddress.getPostcode());
        assertEquals("", formattedAddress.getAddressLine1());
        assertEquals("", formattedAddress.getAddressLine2());
        assertEquals("", formattedAddress.getPostTown());
        assertEquals("", formattedAddress.getCountry());
    }

    @Test
    void testToString() {
        FormattedAddressDto address = new FormattedAddressDto(
                "NP22 5JY",
                "GARDEN CITY",
                "RHYMNEY",
                "TREDEGAR",
                "GB-WLS"
        );

        String result = address.toString();
        assertNotNull(result);
        assertTrue(result.contains("NP22 5JY"));
        assertTrue(result.contains("GARDEN CITY"));
        assertTrue(result.contains("RHYMNEY"));
        assertTrue(result.contains("TREDEGAR"));
        assertTrue(result.contains("GB-WLS"));
    }

    @Test
    void testToStringWithNullValues() {
        FormattedAddressDto address = new FormattedAddressDto();
        String result = address.toString();
        assertNotNull(result);
        assertTrue(result.contains("FormattedAddressDto"));
    }

    @Test
    void testFieldOverwrite() {
        formattedAddress.setPostcode("SW1A1AA");
        assertEquals("SW1A1AA", formattedAddress.getPostcode());

        formattedAddress.setPostcode("NP22 5JY");
        assertEquals("NP22 5JY", formattedAddress.getPostcode());
    }

    @Test
    void testPartialFieldsSet() {
        formattedAddress.setPostcode("SW1A1AA");
        formattedAddress.setAddressLine1("Downing Street");

        assertEquals("SW1A1AA", formattedAddress.getPostcode());
        assertEquals("Downing Street", formattedAddress.getAddressLine1());
        assertNull(formattedAddress.getAddressLine2());
        assertNull(formattedAddress.getPostTown());
        assertNull(formattedAddress.getCountry());
    }

    @Test
    void testConstructorWithAllNullValues() {
        FormattedAddressDto address = new FormattedAddressDto(null, null, null, null, null);
        assertNull(address.getPostcode());
        assertNull(address.getAddressLine1());
        assertNull(address.getAddressLine2());
        assertNull(address.getPostTown());
        assertNull(address.getCountry());
    }

    @Test
    void testConstructorPartiallyNull() {
        FormattedAddressDto address = new FormattedAddressDto(
                "SW1A1AA",
                "Downing Street",
                null,
                "London",
                null
        );

        assertEquals("SW1A1AA", address.getPostcode());
        assertEquals("Downing Street", address.getAddressLine1());
        assertNull(address.getAddressLine2());
        assertEquals("London", address.getPostTown());
        assertNull(address.getCountry());
    }
}
