package uk.gov.companieshouse.addresslookup.model;

import java.math.BigDecimal;

public class AddressDto {

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

    public AddressDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUprn() {
        return uprn;
    }

    public void setUprn(Long uprn) {
        this.uprn = uprn;
    }

    public int getUdprn() {
        return udprn;
    }

    public void setUdprn(int udprn) {
        this.udprn = udprn;
    }

    public String getUprnMatchType() {
        return uprnMatchType;
    }

    public void setUprnMatchType(String uprnMatchType) {
        this.uprnMatchType = uprnMatchType;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Boolean getIsWelsh() {
        return isWelsh;
    }

    public void setIsWelsh(Boolean isWelsh) {
        this.isWelsh = isWelsh;
    }

    public Boolean getIsResidential() {
        return isResidential;
    }

    public void setIsResidential(Boolean isResidential) {
        this.isResidential = isResidential;
    }

    public String getClassicationCode() {
        return classicationCode;
    }

    public void setClassicationCode(String classicationCode) {
        this.classicationCode = classicationCode;
    }

    public String getClassicationShortDescription() {
        return classicationShortDescription;
    }

    public void setClassicationShortDescription(String classicationShortDescription) {
        this.classicationShortDescription = classicationShortDescription;
    }

    public String getClassicationFullDescription() {
        return classicationFullDescription;
    }

    public void setClassicationFullDescription(String classicationFullDescription) {
        this.classicationFullDescription = classicationFullDescription;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "AddressDto{" +
                "id=" + id +
                ", uprn=" + uprn +
                ", udprn=" + udprn +
                ", uprnMatchType='" + uprnMatchType + '\'' +
                ", premises='" + premises + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", postTown='" + postTown + '\'' +
                ", country='" + country + '\'' +
                ", postcode='" + postcode + '\'' +
                ", isWelsh=" + isWelsh +
                ", isResidential=" + isResidential +
                ", classicationCode='" + classicationCode + '\'' +
                ", classicationShortDescription='" + classicationShortDescription + '\'' +
                ", classicationFullDescription='" + classicationFullDescription + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
