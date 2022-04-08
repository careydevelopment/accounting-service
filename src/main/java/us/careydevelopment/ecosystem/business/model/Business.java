package us.careydevelopment.ecosystem.business.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Document(collection = "#{@environment.getProperty('mongo.business.collection')}")
public class Business {

    @Id
    private String id;

    private BusinessType businessType = BusinessType.BUSINESS;

    @Valid
    private Person person;

    @NotBlank(message = "Business name must be included")
    @Size(max = 128, message = "Business name cannot exceed 128 characters")
    private String name;

    @NotBlank(message = "Business display name must be included")
    @Size(max = 128, message = "Display name cannot exceed 128 characters")
    private String displayName;

    @Valid
    private Address address;

    @Size(max = 512, message = "Notes cannot exceed 512 characters")
    private String notes;

    @Size(max = 128, message = "Email address cannot exceed 128 characters")
    @Email
    private String email;

    @Size(max = 24, message = "Phone number cannot exceed 24 characters")
    private String phone;

    @Size(max = 24, message = "Mobile number cannot exceed 24 characters")
    private String mobile;

    @Size(max = 24, message = "Fax number cannot exceed 24 characters")
    private String fax;

    @Size(max = 64, message="Website name cannot exceed 64 characters")
    private String website;

    @Size(max = 32, message = "Account number cannot exceed 32 characters")
    private String accountNumber;

    @Size(max = 16, message = "Tax ID cannot exceed 16 characters")
    private String taxId;

    private UserLightweight owner;

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

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserLightweight getOwner() {
        return owner;
    }

    public void setOwner(UserLightweight owner) {
        this.owner = owner;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Business business = (Business) o;
        return Objects.equals(id, business.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
