package us.careydevelopment.accounting.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.Size;

public class Person {

    @Size(max = 32, message = "First name cannot exceed 32 characters")
    private String firstName;

    @Size(max = 32, message = "Last name cannot exceed 32 characters")
    private String lastName;

    @Size(max = 32, message = "Middle name cannot exceed 32 characters")
    private String middleName;

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
