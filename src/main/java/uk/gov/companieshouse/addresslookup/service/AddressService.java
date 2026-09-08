package uk.gov.companieshouse.addresslookup.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.addresslookup.entity.AddressEntity;
import uk.gov.companieshouse.addresslookup.mapper.AddressMapper;
import uk.gov.companieshouse.addresslookup.model.AddressDto;
import uk.gov.companieshouse.addresslookup.repository.AddressRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        if (addressRepository == null) {
            throw new IllegalArgumentException("AddressRepository cannot be null");
        }
        this.addressRepository = addressRepository;
        try {
            this.addressMapper = Mappers.getMapper(AddressMapper.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize AddressMapper", e);
        }
    }

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    /**
     * Normalize postcode by removing all whitespace and converting to uppercase
     * Allows matching "SW1A 1AA" with "SW1A1AA"
     */
    private String normalisePostcode(String postcode) {
        if (postcode == null || postcode.isEmpty()) {
            return "";
        }
        return postcode.replaceAll("\\s+", "").toUpperCase();
    }

    /**
     * Filter addresses by normalized postcode comparison
     * Finds all addresses where the postcode matches ignoring spaces
     */
    private List<AddressEntity> filterByNormalizedPostcode(List<AddressEntity> addresses, String inputPostcode) {
        String normalized = normalisePostcode(inputPostcode);
        return addresses.stream()
                .filter(addr -> normalisePostcode(addr.getPostcode()).equals(normalized))
                .collect(Collectors.toList());
    }

    /**
     * Asynchronously search for a single formatted address by postcode
     * Returns the first address found with postcode, addressLine1, addressLine2, postTown, and country
     * Accepts postcodes with or without spaces (e.g., "SW1A 1AA" or "SW1A1AA")
     */
    @Async
    public CompletableFuture<AddressDto> searchByPostcode(String postcode) {
        // First try exact match with input as-is
        List<AddressEntity> addresses = addressRepository.findByPostcode(postcode);
        
        // If no exact match, search all and filter by normalized postcode
        if (addresses.isEmpty()) {
            List<AddressEntity> allAddresses = addressRepository.findAll();
            addresses = filterByNormalizedPostcode(allAddresses, postcode);
        }
        
        if (addresses.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.completedFuture(addressMapper.entityToDto(addresses.get(0)));
    }

    /**
     * Asynchronously search multiple addresses by postcode
     * Accepts postcodes with or without spaces (e.g., "SW1A 1AA" or "SW1A1AA")
     */
    @Async
    public CompletableFuture<List<AddressDto>> searchMultipleByPostcode(String postcode) {
        // First try exact match with input as-is
        List<AddressEntity> addresses = addressRepository.findMultipleByPostcode(postcode);
        
        // If no exact match, search all and filter by normalized postcode
        if (addresses.isEmpty()) {
            List<AddressEntity> allAddresses = addressRepository.findAll();
            addresses = filterByNormalizedPostcode(allAddresses, postcode);
        }
        
        if (addresses.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return CompletableFuture.completedFuture(addressMapper.entitiesToDtos(addresses));
    }

}
