package com.auca.abstracts;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
import java.time.LocalDate;

public abstract class Person implements Registrable {
    private String personId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    protected Person(String personId, String firstName, String lastName, String email,
                     String phoneNumber, LocalDate dateOfBirth) {
        this.personId = personId; this.firstName = firstName; this.lastName = lastName;
        this.email = email; this.phoneNumber = phoneNumber; this.dateOfBirth = dateOfBirth;
    }
    public String getPersonId() { return personId; }
    public void setPersonId(String value) { personId = value; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String value) { firstName = value; }
    public String getLastName() { return lastName; }
    public void setLastName(String value) { lastName = value; }
    public String getEmail() { return email; }
    public void setEmail(String value) { email = value; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String value) { phoneNumber = value; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate value) { dateOfBirth = value; }
    public String getId() { return personId; }
    public String getDisplayName() { return firstName + " " + lastName; }
    public boolean isActive() { return true; }
    public String getAcademicCategory() { return "Not applicable"; }
    protected void validatePerson() throws ValidationException {
        if (personId == null || personId.trim().length() < 4 || personId.length() > 30)
            throw new ValidationException("Person ID must contain 4-30 characters.");
        if (!validName(firstName) || !validName(lastName))
            throw new ValidationException("First and last names must contain 2-60 characters.");
        if (email == null || email.length() > 120 || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            throw new ValidationException("Enter a valid email address (maximum 120 characters).");
        if (phoneNumber == null || !phoneNumber.matches("\\+?\\d{10,15}"))
            throw new ValidationException("Phone must contain 10-15 digits, optionally prefixed by +.");
        if (dateOfBirth == null || !dateOfBirth.isBefore(LocalDate.now()))
            throw new ValidationException("Date of birth must be a valid past date.");
    }
    protected boolean validName(String value) {
        return value != null && value.trim().length() >= 2 && value.trim().length() <= 60;
    }
    public void printDetails() { System.out.println(getRegistrationSummary()); }
    public String getRegistrationSummary() { return getRoleDescription() + " " + getId() + ": " + getDisplayName() + ", " + email; }
    public String toString() { return getRegistrationSummary(); }
    public abstract String getRoleDescription();
    public abstract void validate() throws ValidationException;
}
