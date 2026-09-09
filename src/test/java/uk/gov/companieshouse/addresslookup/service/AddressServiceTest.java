package uk.gov.companieshouse.addresslookup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.addresslookup.entity.AddressEntity;
import uk.gov.companieshouse.addresslookup.mapper.AddressMapper;
import uk.gov.companieshouse.addresslookup.model.AddressDto;
import uk.gov.companieshouse.addresslookup.repository.AddressRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressService addressService;

    private AddressEntity testEntity;
    private AddressDto testDto;

    @BeforeEach
    void setUp() {
        testEntity = new AddressEntity();
        testEntity.setId(1L);
        testEntity.setUprn(123456789L);
        testEntity.setUdprn(12345);
        testEntity.setUprnMatchType("EXACT");
        testEntity.setPremises("10");
        testEntity.setAddressLine1("Downing Street");
        testEntity.setPostTown("London");
        testEntity.setCountry("United Kingdom");
        testEntity.setPostcode("SW1A1AA");
        testEntity.setIsWelsh(false);
        testEntity.setIsResidential(true);
        testEntity.setClassicationCode("RES");
        testEntity.setLatitude(new BigDecimal("51.5033"));
        testEntity.setLongitude(new BigDecimal("-0.1276"));

        testDto = new AddressDto();
        testDto.setId(1L);
        testDto.setUprn(123456789L);
        testDto.setPostcode("SW1A1AA");
    }

    @Test
    void testSearchSingleAddressByPostcode() throws Exception {
        List<AddressEntity> entities = List.of(testEntity);

        when(addressRepository.findByPostcode("SW1A1AA")).thenReturn(entities);
        when(addressMapper.entityToDto(testEntity)).thenReturn(testDto);

        CompletableFuture<AddressDto> result = addressService.searchByPostcode("SW1A1AA");
        AddressDto address = result.get();

        assertNotNull(address);
        assertEquals("SW1A1AA", address.getPostcode());
        verify(addressRepository).findByPostcode("SW1A1AA");
        verify(addressMapper).entityToDto(testEntity);
    }

    @Test
    void testSearchSingleAddressByPostcodeNotFound() throws Exception {
        when(addressRepository.findByPostcode("SW1A1AA")).thenReturn(List.of());
        when(addressRepository.findAll()).thenReturn(List.of());

        CompletableFuture<AddressDto> result = addressService.searchByPostcode("SW1A1AA");
        AddressDto address = result.get();

        assertNull(address);
        verify(addressRepository).findByPostcode("SW1A1AA");
        verify(addressRepository).findAll();
    }

    @Test
    void testSearchSingleAddressByPostcodeWithNormalisation() throws Exception {
        // Test that "SW1A 1AA" (with space) matches database entry "SW1A1AA" (without space)
        when(addressRepository.findByPostcode("SW1A 1AA")).thenReturn(List.of());
        when(addressRepository.findAll()).thenReturn(List.of(testEntity));
        when(addressMapper.entityToDto(testEntity)).thenReturn(testDto);

        CompletableFuture<AddressDto> result = addressService.searchByPostcode("SW1A 1AA");
        AddressDto address = result.get();

        assertNotNull(address);
        assertEquals("SW1A1AA", address.getPostcode());
        verify(addressRepository).findByPostcode("SW1A 1AA");
        verify(addressRepository).findAll();
        verify(addressMapper).entityToDto(testEntity);
    }

    @Test
    void testSearchMultipleAddressesByPostcode() throws Exception {
        List<AddressEntity> entities = List.of(testEntity, testEntity);

        when(addressRepository.findMultipleByPostcode("SW1A1AA")).thenReturn(entities);
        when(addressMapper.entitiesToDtos(entities)).thenReturn(List.of(testDto, testDto));

        CompletableFuture<List<AddressDto>> result = addressService.searchMultipleByPostcode("SW1A1AA");
        List<AddressDto> addresses = result.get();

        assertNotNull(addresses);
        assertEquals(2, addresses.size());
        verify(addressRepository).findMultipleByPostcode("SW1A1AA");
        verify(addressMapper).entitiesToDtos(entities);
    }

    @Test
    void testSearchMultipleAddressesByPostcodeNotFound() throws Exception {
        when(addressRepository.findMultipleByPostcode("SW1A1AA")).thenReturn(List.of());
        when(addressRepository.findAll()).thenReturn(List.of());

        CompletableFuture<List<AddressDto>> result = addressService.searchMultipleByPostcode("SW1A1AA");
        List<AddressDto> addresses = result.get();

        assertNotNull(addresses);
        assertTrue(addresses.isEmpty());
        verify(addressRepository).findMultipleByPostcode("SW1A1AA");
        verify(addressRepository).findAll();
    }

    @Test
    void testSearchMultipleAddressesByPostcodeWithNormalisation() throws Exception {
        // Test that "SW1A 1AA" (with space) matches database entry "SW1A1AA" (without space)
        List<AddressEntity> entities = List.of(testEntity, testEntity);

        when(addressRepository.findMultipleByPostcode("SW1A 1AA")).thenReturn(List.of());
        when(addressRepository.findAll()).thenReturn(entities);
        when(addressMapper.entitiesToDtos(entities)).thenReturn(List.of(testDto, testDto));

        CompletableFuture<List<AddressDto>> result = addressService.searchMultipleByPostcode("SW1A 1AA");
        List<AddressDto> addresses = result.get();

        assertNotNull(addresses);
        assertEquals(2, addresses.size());
        verify(addressRepository).findMultipleByPostcode("SW1A 1AA");
        verify(addressRepository).findAll();
        verify(addressMapper).entitiesToDtos(entities);
    }

}
