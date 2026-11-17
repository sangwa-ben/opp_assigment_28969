package com.auca.abstracts;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import java.time.LocalDate;

public abstract class AcademicMember extends Person {
    private String memberNumber;
    private Department department;
    private LocalDate joinDate;
    private String academicStatus;
    private String institutionalEmail;
    private String advisorName;

    protected AcademicMember(String personId, String firstName, String lastName, String email,
            String phoneNumber, LocalDate dateOfBirth, String memberNumber, Department department,
            LocalDate joinDate, String academicStatus, String institutionalEmail, String advisorName) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth);
        this.memberNumber = memberNumber; this.department = department; this.joinDate = joinDate;
        this.academicStatus = academicStatus; this.institutionalEmail = institutionalEmail; this.advisorName = advisorName;
    }
    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String value) { memberNumber = value; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department value) { department = value; }
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate value) { joinDate = value; }
    public String getAcademicStatus() { return academicStatus; }
    public void setAcademicStatus(String value) { academicStatus = value; }
    public String getInstitutionalEmail() { return institutionalEmail; }
    public void setInstitutionalEmail(String value) { institutionalEmail = value; }
    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String value) { advisorName = value; }
    protected void validateAcademicMember() throws ValidationException {
        validatePerson();
        if (memberNumber == null || memberNumber.trim().length() < 4 || department == null)
            throw new ValidationException("Member number (4+ characters) and an existing department are required.");
        if (joinDate == null || joinDate.isAfter(LocalDate.now()))
            throw new ValidationException("Join date must not be in the future.");
        if (academicStatus == null || !academicStatus.matches("ACTIVE|ON_LEAVE|INACTIVE"))
            throw new ValidationException("Academic status must be ACTIVE, ON_LEAVE or INACTIVE.");
        if (institutionalEmail == null || !institutionalEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            throw new ValidationException("Enter a valid institutional email.");
        if (advisorName == null || advisorName.trim().isEmpty())
            throw new ValidationException("Advisor name is required (use N/A when not applicable).");
    }
    public boolean isActive() { return "ACTIVE".equals(academicStatus); }
    public abstract String getAcademicCategory();
}
