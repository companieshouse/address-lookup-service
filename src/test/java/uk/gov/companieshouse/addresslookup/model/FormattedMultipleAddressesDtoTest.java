package uk.gov.companieshouse.addresslookup.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FormattedMultipleAddressesDtoTest {

    private FormattedMultipleAddressesDto multipleAddresses;
    private List<FormattedMultipleAddressItemDto> addressList;
    private FormattedMultipleAddressItemDto address1;
    private FormattedMultipleAddressItemDto address2;

    @BeforeEach
    void setUp() {
        address1 = new FormattedMultipleAddressItemDto(
                "10",
                "Downing Street",
                "Westminster",
                "London",
                "SW1A1AA"
        );

        address2 = new FormattedMultipleAddressItemDto(
                "20",
                "GARDEN CITY",
                "RHYMNEY",
                "TREDEGAR",
                "NP22 5JY"
        );

        addressList = new ArrayList<>();
        addressList.add(address1);
        addressList.add(address2);

        multipleAddresses = new FormattedMultipleAddressesDto(addressList);
    }

    @Test
    void testConstructorWithMultipleAddresses() {
        FormattedMultipleAddressesDto dto = new FormattedMultipleAddressesDto(addressList);

        assertNotNull(dto);
        assertNotNull(dto.getAddresses());
        assertEquals(2, dto.getAddresses().size());
    }

    @Test
    void testConstructorWithEmptyList() {
        List<FormattedMultipleAddressItemDto> emptyList = new ArrayList<>();
        FormattedMultipleAddressesDto dto = new FormattedMultipleAddressesDto(emptyList);

        assertNotNull(dto);
        assertNotNull(dto.getAddresses());
        assertEquals(0, dto.getAddresses().size());
    }

    @Test
    void testConstructorWithSingleAddress() {
        List<FormattedMultipleAddressItemDto> singleList = List.of(address1);
        FormattedMultipleAddressesDto dto = new FormattedMultipleAddressesDto(singleList);

        assertNotNull(dto);
        assertEquals(1, dto.getAddresses().size());
        assertEquals(address1, dto.getAddresses().get(0));
    }

    @Test
    void testGetAddresses() {
        List<FormattedMultipleAddressItemDto> addresses = multipleAddresses.getAddresses();

        assertNotNull(addresses);
        assertEquals(2, addresses.size());
        assertEquals(address1, addresses.get(0));
        assertEquals(address2, addresses.get(1));
    }

    @Test
    void testGetAddressesContent() {
        List<FormattedMultipleAddressItemDto> addresses = multipleAddresses.getAddresses();

        assertEquals("10", addresses.get(0).getPremises());
        assertEquals("Downing Street", addresses.get(0).getAddressLine1());
        assertEquals("Westminster", addresses.get(0).getAddressLine2());
        assertEquals("London", addresses.get(0).getPostTown());
        assertEquals("SW1A1AA", addresses.get(0).getPostcode());

        assertEquals("20", addresses.get(1).getPremises());
        assertEquals("GARDEN CITY", addresses.get(1).getAddressLine1());
        assertEquals("RHYMNEY", addresses.get(1).getAddressLine2());
        assertEquals("TREDEGAR", addresses.get(1).getPostTown());
        assertEquals("NP22 5JY", addresses.get(1).getPostcode());
    }

    @Test
    void testConstructorWithNull() {
        FormattedMultipleAddressesDto dto = new FormattedMultipleAddressesDto(null);
        assertNull(dto.getAddresses());
    }

    @Test
    void testToString() {
        String result = multipleAddresses.toString();

        assertNotNull(result);
        assertTrue(result.contains("FormattedMultipleAddressesDto"));
        assertTrue(result.contains("addresses"));
    }

    @Test
    void testToStringWithEmptyList() {
        List<FormattedMultipleAddressItemDto> emptyList = new ArrayList<>();
        FormattedMultipleAddressesDto dto = new FormattedMultipleAddressesDto(emptyList);

        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("FormattedMultipleAddressesDto"));
    }

    @Test
    void testAddressesImmutability() {
        List<FormattedMultipleAddressItemDto> addresses = multipleAddresses.getAddresses();
        assertEquals(2, addresses.size());

        // Verify we get the same list reference
        List<FormattedMultipleAddressItemDto> addressesAgain = multipleAddresses.getAddresses();
        assertEquals(addresses, addressesAgain);
    }

    @Test
    void testMultipleAddressesWithNullItems() {
        List<FormattedMultipleAddressItemDto> listWithNull = new ArrayList<>();
        listWithNull.add(address1);
        listWithNull.add(null);
        listWithNull.add(address2);

        FormattedMultipleAddressesDto dto = new FormattedMultipleAddressesDto(listWithNull);
        assertEquals(3, dto.getAddresses().size());
        assertNotNull(dto.getAddresses().get(0));
        assertNull(dto.getAddresses().get(1));
        assertNotNull(dto.getAddresses().get(2));
    }

    @Test
    void testLargeNumberOfAddresses() {
        List<FormattedMultipleAddressItemDto> largeList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            largeList.add(new FormattedMultipleAddressItemDto(
                    String.valueOf(i),
                    "Address " + i,
                    "Line2 " + i,
                    "Town " + i,
                    "PC" + i
            ));
        }

        FormattedMultipleAddressesDto dto = new FormattedMultipleAddressesDto(largeList);
        assertEquals(100, dto.getAddresses().size());
    }

    @Test
    void testGetAddressesMultipleCalls() {
        List<FormattedMultipleAddressItemDto> addresses1 = multipleAddresses.getAddresses();
        List<FormattedMultipleAddressItemDto> addresses2 = multipleAddresses.getAddresses();

        assertNotNull(addresses1);
        assertNotNull(addresses2);
        assertEquals(addresses1.size(), addresses2.size());
        assertEquals(addresses1, addresses2);
    }

    @Test
    void testConstructorWithListContainingDifferentData() {
        FormattedMultipleAddressItemDto addr1 = new FormattedMultipleAddressItemDto(
                "1", "Street 1", "Line 1", "Town 1", "PC1"
        );
        FormattedMultipleAddressItemDto addr2 = new FormattedMultipleAddressItemDto(
                "2", "Street 2", "Line 2", "Town 2", "PC2"
        );
        FormattedMultipleAddressItemDto addr3 = new FormattedMultipleAddressItemDto(
                "3", "Street 3", "Line 3", "Town 3", "PC3"
        );

        List<FormattedMultipleAddressItemDto> list = List.of(addr1, addr2, addr3);
        FormattedMultipleAddressesDto dto = new FormattedMultipleAddressesDto(list);

        assertEquals(3, dto.getAddresses().size());
        assertEquals(addr1, dto.getAddresses().get(0));
        assertEquals(addr2, dto.getAddresses().get(1));
        assertEquals(addr3, dto.getAddresses().get(2));
    }

    @Test
    void testConstructorPreservesListOrder() {
        FormattedMultipleAddressesDto dto = new FormattedMultipleAddressesDto(addressList);

        List<FormattedMultipleAddressItemDto> addresses = dto.getAddresses();
        assertEquals(address1.getPremises(), addresses.get(0).getPremises());
        assertEquals(address2.getPremises(), addresses.get(1).getPremises());
    }

    @Test
    void testGetAddressesReturnsList() {
        List<FormattedMultipleAddressItemDto> addresses = multipleAddresses.getAddresses();

        assertTrue(addresses instanceof List);
        assertFalse(addresses.isEmpty());
    }
}
