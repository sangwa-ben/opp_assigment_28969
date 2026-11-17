package com.auca.teacher;
import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
public class Teacher extends AcademicMember {
    private String employeeNumber, specialization;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String officeNumber, employmentType;
    public Teacher(String p,String f,String l,String e,String ph,LocalDate dob,String m,Department d,LocalDate j,String s,String ie,String a,String en,String spec,LocalDate hd,BigDecimal sal,String off,String type){
        super(p,f,l,e,ph,dob,m,d,j,s,ie,a);employeeNumber=en;specialization=spec;hireDate=hd;salary=sal;officeNumber=off;employmentType=type;
    }
    public String getEmployeeNumber(){return employeeNumber;} public void setEmployeeNumber(String v){employeeNumber=v;}
    public String getSpecialization(){return specialization;} public void setSpecialization(String v){specialization=v;}
    public LocalDate getHireDate(){return hireDate;} public void setHireDate(LocalDate v){hireDate=v;}
    public BigDecimal getSalary(){return salary;} public void setSalary(BigDecimal v){salary=v;}
    public String getOfficeNumber(){return officeNumber;} public void setOfficeNumber(String v){officeNumber=v;}
    public String getEmploymentType(){return employmentType;} public void setEmploymentType(String v){employmentType=v;}
    public String getRoleDescription(){return "Teacher";} public String getAcademicCategory(){return "Faculty";}
    public String getId(){return employeeNumber;} public String getDisplayName(){return super.getDisplayName()+" ["+employeeNumber+"]";}
    public String getRegistrationSummary(){return "Teacher "+employeeNumber+": "+getDisplayName()+", "+specialization+", "+getDepartment().getDepartmentName();}
    public void validate() throws ValidationException {
        validateAcademicMember();
        if(employeeNumber==null||employeeNumber.trim().length()<4||specialization==null||specialization.trim().isEmpty()) throw new ValidationException("Employee number (4+ chars) and specialization are required.");
        if(hireDate==null||hireDate.isAfter(LocalDate.now())||getJoinDate().isBefore(hireDate)) throw new ValidationException("Hire date must not be in the future or later than join date.");
        if(salary==null||salary.signum()<=0) throw new ValidationException("Salary must be greater than zero.");
        if(officeNumber==null||officeNumber.trim().isEmpty()) throw new ValidationException("Office number is required.");
        if(employmentType==null||!employmentType.matches("FULL_TIME|PART_TIME|CONTRACT")) throw new ValidationException("Employment type must be FULL_TIME, PART_TIME or CONTRACT.");
    }
    public String toString(){return getRegistrationSummary();}
}
