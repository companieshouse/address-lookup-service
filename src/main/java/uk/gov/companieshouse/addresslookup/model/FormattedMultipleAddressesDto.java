package uk.gov.companieshouse.addresslookup.model;
import java.util.List;
/**
 * FormattedMultipleAddressesDto - wraps a list of formatted address items
 */
public class FormattedMultipleAddressesDto {
    private final List<FormattedMultipleAddressItemDto> addresses;

    public FormattedMultipleAddressesDto(List<FormattedMultipleAddressItemDto> addresses) {
        this.addresses = addresses;
    }

    public List<FormattedMultipleAddressItemDto> getAddresses() {
        return addresses;
    }
    
    @Override
    public String toString() {
        return "FormattedMultipleAddressesDto{" +
                "addresses=" + addresses +
                '}';
    }


}
