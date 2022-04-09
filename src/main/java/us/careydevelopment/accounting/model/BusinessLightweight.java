package us.careydevelopment.accounting.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.NotBlank;
import java.util.Objects;


public class BusinessLightweight {

    @NotBlank(message = "Business ID is required")
    private String id;

    private BusinessType businessType = BusinessType.BUSINESS;
    private Person person;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessLightweight business = (BusinessLightweight) o;
        return Objects.equals(id, business.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
