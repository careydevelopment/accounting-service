package us.careydevelopment.accounting.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {

    @Size(max = 32, message = "First name cannot exceed 32 characters")
    private String firstName;

    @Size(max = 32, message = "Last name cannot exceed 32 characters")
    private String lastName;

    @Size(max = 32, message = "Middle name cannot exceed 32 characters")
    private String middleName;

    @Size(max = 12, message = "Title cannot exceed 32 characters")
    private String title;

    @Size(max = 12, message = "Suffix cannot exceed 32 characters")
    private String suffix;

    private boolean useSameContactInfoAsBusiness = false;

    @Size(max = 32, message = "Email cannot exceed 32 characters")
    @Email
    private String email;

    @Size(max = 24, message = "Phone number cannot exceed 24 characters")
    private String phoneNumber;

    @Size(max = 24, message = "Mobile number exceed 24 characters")
    private String mobileNUmber;

    @Size(max = 24, message = "Fax cannot exceed 24 characters")
    private String fax;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
