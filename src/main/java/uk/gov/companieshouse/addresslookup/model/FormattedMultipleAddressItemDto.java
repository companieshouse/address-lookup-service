package uk.gov.companieshouse.addresslookup.model;

/**
 * Formatted address item DTO for multiple addresses response
 * Contains: premises, addressLine1, addressLine2, postTown, postcode
 */
public class FormattedMultipleAddressItemDto {

    private String premises;
    private String addressLine1;
    private String addressLine2;
    private String postTown;
    private String postcode;

    public FormattedMultipleAddressItemDto() {
    }

    public FormattedMultipleAddressItemDto(String premises, String addressLine1, String addressLine2, String postTown, String postcode) {
        this.premises = premises;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postTown = postTown;
        this.postcode = postcode;
    }

    public String getPremises() {
        return premises;
    }

    public void setPremises(String premises) {
        this.premises = premises;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getPostTown() {
        return postTown;
    }

    public void setPostTown(String postTown) {
        this.postTown = postTown;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return "FormattedAddressItemDto{" +
                "premises='" + premises + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", postTown='" + postTown + '\'' +
                ", postcode='" + postcode + '\'' +
                '}';
    }
}
