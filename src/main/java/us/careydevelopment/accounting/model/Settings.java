package us.careydevelopment.accounting.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Document(collection = "#{@environment.getProperty('mongo.setting.collection')}")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Settings extends OwnedItem {

    @Id
    private String id;

    @Size(max = 50, message = "Company name cannot exceed 50 characters")
    private String companyName;

    @Size(max = 60, message = "Company legal name cannot exceed 60 characters")
    private String companyLegalName;

    @Size(max = 20, message = "EIN cannot exceed 20 characters")
    private String ein;

    @Valid
    private Address address;

    @Size(max = 30, message = "Phone number cannot exceed 30 characters")
    private String phoneNumber;

    @Size(max = 30, message = "Email address cannot exceed 30 characters")
    private String emailAddress;

    @Size(max = 30, message = "Website cannot exceed 30 characters")
    private String website;

    @Max(value = 12, message = "First month of fiscal year must be between 1 and 12")
    @Min(value = 1, message = "First month of fiscal year must be between 1 and 12")
    private Integer firstMonthOfFiscalYear = 1;

    @NotNull
    private AccountingMethod accountingMethod = AccountingMethod.CASH;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLegalName() {
        return companyLegalName;
    }

    public void setCompanyLegalName(String companyLegalName) {
        this.companyLegalName = companyLegalName;
    }

    public String getEin() {
        return ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getFirstMonthOfFiscalYear() {
        return firstMonthOfFiscalYear;
    }

    public void setFirstMonthOfFiscalYear(Integer firstMonthOfFiscalYear) {
        this.firstMonthOfFiscalYear = firstMonthOfFiscalYear;
    }

    public AccountingMethod getAccountingMethod() {
        return accountingMethod;
    }

    public void setAccountingMethod(AccountingMethod accountingMethod) {
        this.accountingMethod = accountingMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return Objects.equals(id, settings.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
