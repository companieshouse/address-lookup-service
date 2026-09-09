package uk.gov.companieshouse.addresslookup.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.companieshouse.addresslookup.entity.GbBuiltAddress;
import uk.gov.companieshouse.addresslookup.entity.RoyalMailAddress;
import uk.gov.companieshouse.addresslookup.mapper.LegacyAddressMapper;
import uk.gov.companieshouse.addresslookup.mapper.RoyalMailAddressMapper;
import uk.gov.companieshouse.addresslookup.model.LegacyAddress;
import uk.gov.companieshouse.addresslookup.model.RoyalMailAddressDto;
import uk.gov.companieshouse.addresslookup.repository.GbBuiltAddressRepository;
import uk.gov.companieshouse.addresslookup.repository.RoyalMailAddressRepository;

@Service
public class AddressLookupService {

    private static final int MAX_POSTCODE_LENGTH = 8;

    private final RoyalMailAddressRepository royalMailAddressRepository;
    private final GbBuiltAddressRepository gbBuiltAddressRepository;
    private final LegacyAddressMapper legacyAddressMapper;
    private final RoyalMailAddressMapper royalMailAddressMapper;

    public AddressLookupService(RoyalMailAddressRepository royalMailAddressRepository,
            GbBuiltAddressRepository gbBuiltAddressRepository,
            LegacyAddressMapper legacyAddressMapper,
            RoyalMailAddressMapper royalMailAddressMapper) {
        this.royalMailAddressRepository = royalMailAddressRepository;
        this.gbBuiltAddressRepository = gbBuiltAddressRepository;
        this.legacyAddressMapper = legacyAddressMapper;
        this.royalMailAddressMapper = royalMailAddressMapper;
    }

    public List<RoyalMailAddressDto> lookupByPostcode(String postcode) {
        return royalMailAddressMapper.toDtos(findByPostcode(postcode));
    }

    public List<LegacyAddress> lookupLegacyAddressesByPostcode(String postcode) {
        List<RoyalMailAddress> addresses = findByPostcode(postcode);
        Map<Long, String> countryByUprn = findCountryByUprn(addresses);

        return addresses.stream()
                .map(address -> legacyAddressMapper.toLegacyAddress(address, countryByUprn.get(address.getUprn())))
                .toList();
    }

    public Optional<LegacyAddress> lookupLegacyAddressByPostcode(String postcode) {
        List<RoyalMailAddress> addresses = findByPostcode(postcode);
        Map<Long, String> countryByUprn = findCountryByUprn(addresses);

        return addresses.stream()
                .findFirst()
                .map(address -> legacyAddressMapper.toLegacyAddressWithoutPremise(
                        address, countryByUprn.get(address.getUprn())));
    }

    private String normalizePostcode(String postcode) {
        return postcode.replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
    }

    private List<RoyalMailAddress> findByPostcode(String postcode) {
        String normalizedPostcode = normalizePostcode(postcode);
        if (normalizedPostcode.length() > MAX_POSTCODE_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Postcode must be 8 characters or fewer");
        }

        return royalMailAddressRepository.findByNormalizedPostcode(normalizedPostcode);
    }

    private Map<Long, String> findCountryByUprn(List<RoyalMailAddress> addresses) {
        List<Long> uprns = addresses.stream()
                .map(RoyalMailAddress::getUprn)
                .filter(uprn -> uprn != null)
                .distinct()
                .toList();

        if (uprns.isEmpty()) {
            return Map.of();
        }

        return gbBuiltAddressRepository.findAllById(uprns).stream()
                .collect(Collectors.toMap(
                        GbBuiltAddress::getUprn,
                        GbBuiltAddress::getCountry,
                        (existing, replacement) -> existing));
    }

}
