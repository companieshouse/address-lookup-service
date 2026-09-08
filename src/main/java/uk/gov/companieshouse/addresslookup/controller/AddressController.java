package uk.gov.companieshouse.addresslookup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.companieshouse.addresslookup.model.FormattedAddressDto;
import uk.gov.companieshouse.addresslookup.model.FormattedMultipleAddressItemDto;
import uk.gov.companieshouse.addresslookup.model.FormattedMultipleAddressesDto;
import uk.gov.companieshouse.addresslookup.service.AddressService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/address-lookup")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * GET /address-lookup/postcode?postcode=XX1%201AA - Return one formatted address for a postcode
     */
    @GetMapping("/postcode")
    public CompletableFuture<ResponseEntity<FormattedAddressDto>> getAddressesByPostcode(@RequestParam String postcode) {
        return addressService.searchByPostcode(postcode)
                .thenApply(address -> {
                    if (address == null) {
                        return ResponseEntity.notFound().build();
                    }

                    FormattedAddressDto formattedAddress = new FormattedAddressDto(
                            address.getPostcode(),
                            address.getAddressLine1(),
                            address.getAddressLine2(),
                            address.getPostTown(),
                            address.getCountry()
                    );
                    return ResponseEntity.ok(formattedAddress);
                });
    }

    /**
     * GET /address-lookup/multiple-addresses/postcode?postcode=XX1%201AA - Return all premises and addresses for a postcode
     */
    @GetMapping("/multiple-addresses/postcode")
    public CompletableFuture<ResponseEntity<FormattedMultipleAddressesDto>> getMultipleAddressesByPostcode(@RequestParam String postcode) {
        return addressService.searchMultipleByPostcode(postcode)
                .thenApply(addresses -> {
                    if (addresses.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    FormattedMultipleAddressesDto formattedAddresses = new FormattedMultipleAddressesDto(
                            addresses.stream()
                                    .map(address -> new FormattedMultipleAddressItemDto(
                                            address.getPremises(),
                                            address.getAddressLine1(),
                                            address.getAddressLine2(),
                                            address.getPostTown(),
                                            address.getPostcode()
                                    ))
                                    .toList()
                    );
                    return ResponseEntity.ok(formattedAddresses);
                });
    }

}
