package uk.gov.companieshouse.addresslookup.entity;

import java.math.BigDecimal;

/**
 * Builder pattern for constructing AddressEntity instances.
 */
public class AddressBuilder {
    
    private Long id;
    private Long uprn;
    private int udprn;
    private String uprnMatchType;
    private String premises;
    private String addressLine1;
    private String addressLine2;
    private String postTown;
    private String country;
    private String postcode;
    private Boolean isWelsh;
    private Boolean isResidential;
    private String classicationCode;
    private String classicationShortDescription;
    private String classicationFullDescription;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public AddressBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public AddressBuilder uprn(Long uprn) {
        this.uprn = uprn;
        return this;
    }

    public AddressBuilder udprn(int udprn) {
        this.udprn = udprn;
        return this;
    }

    public AddressBuilder uprnMatchType(String uprnMatchType) {
        this.uprnMatchType = uprnMatchType;
        return this;
    }

    public AddressBuilder premises(String premises) {
        this.premises = premises;
        return this;
    }

    public AddressBuilder addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public AddressBuilder addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public AddressBuilder postTown(String postTown) {
        this.postTown = postTown;
        return this;
    }

    public AddressBuilder country(String country) {
        this.country = country;
        return this;
    }

    public AddressBuilder postcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public AddressBuilder isWelsh(Boolean isWelsh) {
        this.isWelsh = isWelsh;
        return this;
    }

    public AddressBuilder isResidential(Boolean isResidential) {
        this.isResidential = isResidential;
        return this;
    }

    public AddressBuilder classicationCode(String classicationCode) {
        this.classicationCode = classicationCode;
        return this;
    }

    public AddressBuilder classicationShortDescription(String classicationShortDescription) {
        this.classicationShortDescription = classicationShortDescription;
        return this;
    }

    public AddressBuilder classicationFullDescription(String classicationFullDescription) {
        this.classicationFullDescription = classicationFullDescription;
        return this;
    }

    public AddressBuilder latitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public AddressBuilder longitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
    }

    /**
     * Build and return the AddressEntity instance
     */
    public AddressEntity build() {
        AddressEntity entity = new AddressEntity();
        entity.setId(id);
        entity.setUprn(uprn);
        entity.setUdprn(udprn);
        entity.setUprnMatchType(uprnMatchType);
        entity.setPremises(premises);
        entity.setAddressLine1(addressLine1);
        entity.setAddressLine2(addressLine2);
        entity.setPostTown(postTown);
        entity.setCountry(country);
        entity.setPostcode(postcode);
        entity.setIsWelsh(isWelsh);
        entity.setIsResidential(isResidential);
        entity.setClassicationCode(classicationCode);
        entity.setClassicationShortDescription(classicationShortDescription);
        entity.setClassicationFullDescription(classicationFullDescription);
        entity.setLatitude(latitude);
        entity.setLongitude(longitude);
        return entity;
    }
}
