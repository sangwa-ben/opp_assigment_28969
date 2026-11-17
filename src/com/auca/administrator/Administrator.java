package com.auca.administrator;
import com.auca.abstracts.Person;
import com.auca.exceptions.ValidationException;
import java.time.LocalDate;
public class Administrator extends Person {
    private String staffNumber, jobTitle, assignedOffice;
    private LocalDate employmentDate;
    private int accessLevel;
    private boolean active;
    public Administrator(String p,String f,String l,String e,String ph,LocalDate dob,String sn,String title,String office,LocalDate date,int level,boolean on){
        super(p,f,l,e,ph,dob);staffNumber=sn;jobTitle=title;assignedOffice=office;employmentDate=date;accessLevel=level;active=on;
    }
    public String getStaffNumber(){return staffNumber;} public void setStaffNumber(String v){staffNumber=v;}
    public String getJobTitle(){return jobTitle;} public void setJobTitle(String v){jobTitle=v;}
    public String getAssignedOffice(){return assignedOffice;} public void setAssignedOffice(String v){assignedOffice=v;}
    public LocalDate getEmploymentDate(){return employmentDate;} public void setEmploymentDate(LocalDate v){employmentDate=v;}
    public int getAccessLevel(){return accessLevel;} public void setAccessLevel(int v){accessLevel=v;}
    public void setActive(boolean v){active=v;}
    public String getRoleDescription(){return "Administrator";} public String getId(){return staffNumber;}
    public String getDisplayName(){return super.getDisplayName()+" ["+staffNumber+"]";} public boolean isActive(){return active;}
    public String getRegistrationSummary(){return "Administrator "+staffNumber+": "+getDisplayName()+", "+jobTitle;}
    public void printDetails(){System.out.println(getRegistrationSummary()+", office "+assignedOffice+", access level "+accessLevel);}
    public void validate() throws ValidationException {
        validatePerson();
        if(staffNumber==null||staffNumber.trim().length()<4||jobTitle==null||jobTitle.trim().isEmpty()||assignedOffice==null||assignedOffice.trim().isEmpty()) throw new ValidationException("Staff number, job title and assigned office are required.");
        if(employmentDate==null||employmentDate.isAfter(LocalDate.now())) throw new ValidationException("Employment date must not be in the future.");
        if(accessLevel<1||accessLevel>5) throw new ValidationException("Access level must be from 1 to 5.");
    }
    public String toString(){return getRegistrationSummary();}
}
