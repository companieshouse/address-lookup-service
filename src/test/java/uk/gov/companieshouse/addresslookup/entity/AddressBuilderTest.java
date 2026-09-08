package uk.gov.companieshouse.addresslookup.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AddressBuilderTest {

    @Test
    void testBuilderCreateCompleteAddress() {
        AddressEntity address = AddressEntity.builder()
                .id(1L)
                .uprn(123456789L)
                .udprn(12345)
                .uprnMatchType("EXACT")
                .premises("10")
                .addressLine1("Downing Street")
                .addressLine2("Westminster")
                .postTown("London")
                .country("United Kingdom")
                .postcode("SW1A1AA")
                .isWelsh(false)
                .isResidential(true)
                .classicationCode("RES")
                .classicationShortDescription("Residential")
                .classicationFullDescription("Residential Building")
                .latitude(new BigDecimal("51.5033"))
                .longitude(new BigDecimal("-0.1276"))
                .build();

        assertNotNull(address);
        assertEquals(1L, address.getId());
        assertEquals(123456789L, address.getUprn());
        assertEquals(12345, address.getUdprn());
        assertEquals("EXACT", address.getUprnMatchType());
        assertEquals("10", address.getPremises());
        assertEquals("Downing Street", address.getAddressLine1());
        assertEquals("Westminster", address.getAddressLine2());
        assertEquals("London", address.getPostTown());
        assertEquals("United Kingdom", address.getCountry());
        assertEquals("SW1A1AA", address.getPostcode());
        assertFalse(address.getIsWelsh());
        assertTrue(address.getIsResidential());
        assertEquals("RES", address.getClassicationCode());
        assertEquals(new BigDecimal("51.5033"), address.getLatitude());
        assertEquals(new BigDecimal("-0.1276"), address.getLongitude());
    }

    @Test
    void testBuilderCreateMinimalAddress() {
        AddressEntity address = AddressEntity.builder()
                .uprn(987654321L)
                .premises("20")
                .addressLine1("Oxford Street")
                .postTown("London")
                .country("United Kingdom")
                .postcode("W1A1AA")
                .build();

        assertNotNull(address);
        assertEquals(987654321L, address.getUprn());
        assertEquals("20", address.getPremises());
        assertEquals("Oxford Street", address.getAddressLine1());
        assertEquals("London", address.getPostTown());
        assertEquals("United Kingdom", address.getCountry());
        assertEquals("W1A1AA", address.getPostcode());
        assertNull(address.getId());
        assertNull(address.getAddressLine2());
    }

    @Test
    void testBuilderFluentInterface() {
        AddressBuilder builder = AddressEntity.builder();

        assertNotNull(builder);
        assertNotNull(builder.uprn(123L));
        assertNotNull(builder.premises("10"));
        assertNotNull(builder.addressLine1("Street"));
    }

    @Test
    void testBuilderPartialBuild() {
        AddressEntity address = AddressEntity.builder()
                .uprn(111111111L)
                .premises("5")
                .addressLine1("Main Street")
                .build();

        assertEquals(111111111L, address.getUprn());
        assertEquals("5", address.getPremises());
        assertEquals("Main Street", address.getAddressLine1());
        assertNull(address.getPostTown());
        assertNull(address.getCountry());
        assertNull(address.getPostcode());
    }

    @Test
    void testBuilderWithBigDecimalCoordinates() {
        BigDecimal latitude = new BigDecimal("48.8566");
        BigDecimal longitude = new BigDecimal("2.3522");

        AddressEntity address = AddressEntity.builder()
                .uprn(222222222L)
                .premises("100")
                .addressLine1("Avenue des Champs-Élysées")
                .postTown("Paris")
                .country("France")
                .postcode("75008")
                .latitude(latitude)
                .longitude(longitude)
                .build();

        assertEquals(latitude, address.getLatitude());
        assertEquals(longitude, address.getLongitude());
        assertTrue(latitude.compareTo(new BigDecimal("48.8566")) == 0);
    }

    @Test
    void testBuilderWithBooleanFlags() {
        AddressEntity address = AddressEntity.builder()
                .uprn(333333333L)
                .premises("30")
                .addressLine1("Test Street")
                .postTown("Test Town")
                .country("Test Country")
                .postcode("TEST1")
                .isWelsh(true)
                .isResidential(false)
                .build();

        assertTrue(address.getIsWelsh());
        assertFalse(address.getIsResidential());
    }

    @Test
    void testBuilderWithClassificationDetails() {
        AddressEntity address = AddressEntity.builder()
                .uprn(444444444L)
                .premises("40")
                .addressLine1("Commercial Way")
                .postTown("Business City")
                .country("Businessland")
                .postcode("BUSI1")
                .classicationCode("COM")
                .classicationShortDescription("Commercial")
                .classicationFullDescription("Commercial Business Premises")
                .build();

        assertEquals("COM", address.getClassicationCode());
        assertEquals("Commercial", address.getClassicationShortDescription());
        assertEquals("Commercial Business Premises", address.getClassicationFullDescription());
    }

    @Test
    void testBuilderOverwrite() {
        AddressEntity address = AddressEntity.builder()
                .uprn(555555555L)
                .uprn(666666666L)  // Overwrite
                .premises("50")
                .premises("51")    // Overwrite
                .build();

        assertEquals(666666666L, address.getUprn());
        assertEquals("51", address.getPremises());
    }
}
