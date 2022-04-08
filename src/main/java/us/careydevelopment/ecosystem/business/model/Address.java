package us.careydevelopment.ecosystem.business.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import us.careydevelopment.ecosystem.business.util.Countries;
import us.careydevelopment.ecosystem.business.util.UsStates;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Address {

    @Size(max = 80, message = "Street address cannot exceed 80 characters")
    private String street1;

    @Size(max = 80, message = "Street address cannot exceed 80 characters")
    private String street2;

    @Size(max = 50, message = "City name cannot exceed 50 characters")
    private String city;
    
    @Size(max = 64, message = "Region name cannot exceed 64 characters")
    private String region;

    @Size(max = 64, message = "Province name cannot exceed 64 characters")
    private String province;

    private String state;

    @Size(max = 2, message = "Country code cannot exceed 2 characters")
    private String country;

    @Size(max = 16, message = "Postal code cannot exceed 16 characters")
    private String postalCode;

    //bit of a hack, gotta name this method isState() so the proper property
    //("state") gets returned
    @AssertTrue(message = "Valid 2-letter U.S. state abbreviation is required")
    public boolean isState() {
        if (StringUtils.isBlank(country) || !"US".equals(country)) {
            return true;
        }

        if (StringUtils.isBlank(state)) {
            return true;
        }

        if (state.length() != 2) {
            return false;
        }

        return UsStates.isValid(state);
    }

    @AssertTrue(message = "Valid 2-letter country code is required")
    public boolean isCountry() {
        if (StringUtils.isBlank(country)) {
            return true;
        }

        if (country.length() != 2) {
            return false;
        }

        return Countries.isValid(country);
    }

    @AssertTrue(message = "Street1 cannot be empty if Street2 is populated")
    public boolean isStreet2() {
        if (!StringUtils.isBlank(street2)) {
            if (StringUtils.isBlank(street1)) {
                return false;
            }
        }

        return true;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street1, address.street1) && Objects.equals(city, address.city) && Objects.equals(country, address.country) && Objects.equals(postalCode, address.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street1, city, country, postalCode);
    }
}
