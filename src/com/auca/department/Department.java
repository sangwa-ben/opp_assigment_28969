package com.auca.department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
import com.auca.teacher.Teacher;
public class Department implements Registrable {
    private String departmentId, departmentName, departmentCode, officeLocation, contactEmail;
    private Teacher headTeacher;
    public Department(String id, String name, String code, String office, String email, Teacher head) {
        departmentId=id; departmentName=name; departmentCode=code; officeLocation=office; contactEmail=email; headTeacher=head;
    }
    public String getDepartmentId(){return departmentId;} public void setDepartmentId(String v){departmentId=v;}
    public String getDepartmentName(){return departmentName;} public void setDepartmentName(String v){departmentName=v;}
    public String getDepartmentCode(){return departmentCode;} public void setDepartmentCode(String v){departmentCode=v;}
    public String getOfficeLocation(){return officeLocation;} public void setOfficeLocation(String v){officeLocation=v;}
    public String getContactEmail(){return contactEmail;} public void setContactEmail(String v){contactEmail=v;}
    public Teacher getHeadTeacher(){return headTeacher;} public void setHeadTeacher(Teacher v){headTeacher=v;}
    public String getId(){return departmentId;} public String getDisplayName(){return departmentName+" ("+departmentCode+")";}
    public boolean isActive(){return true;}
    public String getRegistrationSummary(){return "Department "+departmentId+": "+getDisplayName();}
    public void printDetails(){System.out.println(getRegistrationSummary()+", office "+officeLocation+", email "+contactEmail);}
    public void validate() throws ValidationException {
        if(departmentId==null||departmentId.trim().length()<4||departmentName==null||departmentName.trim().length()<2) throw new ValidationException("Department ID (4+ chars) and name (2+ chars) are required.");
        if(departmentCode==null||!departmentCode.matches("[A-Za-z0-9-]{2,20}")) throw new ValidationException("Department code must be 2-20 letters, digits or hyphens.");
        if(officeLocation==null||officeLocation.trim().isEmpty()||contactEmail==null||!contactEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) throw new ValidationException("Office and valid contact email are required.");
    }
    public String toString(){return getRegistrationSummary();}
}
