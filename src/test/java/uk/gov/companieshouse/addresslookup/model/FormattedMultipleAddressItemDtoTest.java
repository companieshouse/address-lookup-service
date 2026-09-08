package uk.gov.companieshouse.addresslookup.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormattedMultipleAddressItemDtoTest {

    private FormattedMultipleAddressItemDto addressItem;

    @BeforeEach
    void setUp() {
        addressItem = new FormattedMultipleAddressItemDto();
    }

    @Test
    void testNoArgConstructor() {
        FormattedMultipleAddressItemDto item = new FormattedMultipleAddressItemDto();

        assertNotNull(item);
        assertNull(item.getPremises());
        assertNull(item.getAddressLine1());
        assertNull(item.getAddressLine2());
        assertNull(item.getPostTown());
        assertNull(item.getPostcode());
    }

    @Test
    void testAllArgsConstructor() {
        FormattedMultipleAddressItemDto item = new FormattedMultipleAddressItemDto(
                "10",
                "Downing Street",
                "Westminster",
                "London",
                "SW1A1AA"
        );

        assertNotNull(item);
        assertEquals("10", item.getPremises());
        assertEquals("Downing Street", item.getAddressLine1());
        assertEquals("Westminster", item.getAddressLine2());
        assertEquals("London", item.getPostTown());
        assertEquals("SW1A1AA", item.getPostcode());
    }

    @Test
    void testSetAndGetPremises() {
        addressItem.setPremises("20");
        assertEquals("20", addressItem.getPremises());
    }

    @Test
    void testSetAndGetAddressLine1() {
        addressItem.setAddressLine1("GARDEN CITY");
        assertEquals("GARDEN CITY", addressItem.getAddressLine1());
    }

    @Test
    void testSetAndGetAddressLine2() {
        addressItem.setAddressLine2("RHYMNEY");
        assertEquals("RHYMNEY", addressItem.getAddressLine2());
    }

    @Test
    void testSetAndGetPostTown() {
        addressItem.setPostTown("TREDEGAR");
        assertEquals("TREDEGAR", addressItem.getPostTown());
    }

    @Test
    void testSetAndGetPostcode() {
        addressItem.setPostcode("NP22 5JY");
        assertEquals("NP22 5JY", addressItem.getPostcode());
    }

    @Test
    void testSetAllFields() {
        addressItem.setPremises("10");
        addressItem.setAddressLine1("Downing Street");
        addressItem.setAddressLine2("Westminster");
        addressItem.setPostTown("London");
        addressItem.setPostcode("SW1A1AA");

        assertEquals("10", addressItem.getPremises());
        assertEquals("Downing Street", addressItem.getAddressLine1());
        assertEquals("Westminster", addressItem.getAddressLine2());
        assertEquals("London", addressItem.getPostTown());
        assertEquals("SW1A1AA", addressItem.getPostcode());
    }

    @Test
    void testNullPremises() {
        addressItem.setPremises(null);
        assertNull(addressItem.getPremises());
    }

    @Test
    void testNullAddressLine1() {
        addressItem.setAddressLine1(null);
        assertNull(addressItem.getAddressLine1());
    }

    @Test
    void testNullAddressLine2() {
        addressItem.setAddressLine2(null);
        assertNull(addressItem.getAddressLine2());
    }

    @Test
    void testNullPostTown() {
        addressItem.setPostTown(null);
        assertNull(addressItem.getPostTown());
    }

    @Test
    void testNullPostcode() {
        addressItem.setPostcode(null);
        assertNull(addressItem.getPostcode());
    }

    @Test
    void testEmptyStringFields() {
        addressItem.setPremises("");
        addressItem.setAddressLine1("");
        addressItem.setAddressLine2("");
        addressItem.setPostTown("");
        addressItem.setPostcode("");

        assertEquals("", addressItem.getPremises());
        assertEquals("", addressItem.getAddressLine1());
        assertEquals("", addressItem.getAddressLine2());
        assertEquals("", addressItem.getPostTown());
        assertEquals("", addressItem.getPostcode());
    }

    @Test
    void testToString() {
        FormattedMultipleAddressItemDto item = new FormattedMultipleAddressItemDto(
                "10",
                "Downing Street",
                "Westminster",
                "London",
                "SW1A1AA"
        );

        String result = item.toString();
        assertNotNull(result);
        assertTrue(result.contains("10"));
        assertTrue(result.contains("Downing Street"));
        assertTrue(result.contains("Westminster"));
        assertTrue(result.contains("London"));
        assertTrue(result.contains("SW1A1AA"));
    }

    @Test
    void testToStringWithNullValues() {
        FormattedMultipleAddressItemDto item = new FormattedMultipleAddressItemDto();
        String result = item.toString();

        assertNotNull(result);
    }

    @Test
    void testFieldOverwrite() {
        addressItem.setPremises("10");
        assertEquals("10", addressItem.getPremises());

        addressItem.setPremises("20");
        assertEquals("20", addressItem.getPremises());
    }

    @Test
    void testPartialFieldsSet() {
        addressItem.setPremises("10");
        addressItem.setAddressLine1("Downing Street");

        assertEquals("10", addressItem.getPremises());
        assertEquals("Downing Street", addressItem.getAddressLine1());
        assertNull(addressItem.getAddressLine2());
        assertNull(addressItem.getPostTown());
        assertNull(addressItem.getPostcode());
    }

    @Test
    void testConstructorWithAllNullValues() {
        FormattedMultipleAddressItemDto item = new FormattedMultipleAddressItemDto(
                null, null, null, null, null
        );

        assertNull(item.getPremises());
        assertNull(item.getAddressLine1());
        assertNull(item.getAddressLine2());
        assertNull(item.getPostTown());
        assertNull(item.getPostcode());
    }

    @Test
    void testConstructorPartiallyNull() {
        FormattedMultipleAddressItemDto item = new FormattedMultipleAddressItemDto(
                "10",
                "Downing Street",
                null,
                "London",
                null
        );

        assertEquals("10", item.getPremises());
        assertEquals("Downing Street", item.getAddressLine1());
        assertNull(item.getAddressLine2());
        assertEquals("London", item.getPostTown());
        assertNull(item.getPostcode());
    }

    @Test
    void testMultipleInstances() {
        FormattedMultipleAddressItemDto item1 = new FormattedMultipleAddressItemDto(
                "10", "Street 1", "Line 1", "Town 1", "PC1"
        );
        FormattedMultipleAddressItemDto item2 = new FormattedMultipleAddressItemDto(
                "20", "Street 2", "Line 2", "Town 2", "PC2"
        );

        assertEquals("10", item1.getPremises());
        assertEquals("20", item2.getPremises());
        assertEquals("Street 1", item1.getAddressLine1());
        assertEquals("Street 2", item2.getAddressLine1());
    }

    @Test
    void testConstructorWithMixedCases() {
        FormattedMultipleAddressItemDto item = new FormattedMultipleAddressItemDto(
                "10",
                "DOWNING STREET",
                "Westminster",
                "LONDON",
                "sw1a1aa"
        );

        assertEquals("10", item.getPremises());
        assertEquals("DOWNING STREET", item.getAddressLine1());
        assertEquals("Westminster", item.getAddressLine2());
        assertEquals("LONDON", item.getPostTown());
        assertEquals("sw1a1aa", item.getPostcode());
    }
}
