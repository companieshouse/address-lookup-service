package uk.gov.companieshouse.addresslookup.model;

/**
 * Formatted address response DTO with only essential fields
 */
public class FormattedAddressDto {

    private String postcode;
    private String addressLine1;
    private String addressLine2;
    private String postTown;
    private String country;

    public FormattedAddressDto() {
    }

    public FormattedAddressDto(String postcode, String addressLine1, String addressLine2, String postTown, String country) {
        this.postcode = postcode;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postTown = postTown;
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "FormattedAddressDto{" +
                "postcode='" + postcode + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", postTown='" + postTown + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
