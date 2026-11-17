package com.auca.student;
import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import java.time.LocalDate;
public class Student extends AcademicMember {
    private String studentNumber, programName;
    private LocalDate admissionDate;
    private int studyYear;
    private String guardianPhone;
    private boolean scholarshipStatus;
    public Student(String p,String f,String l,String e,String ph,LocalDate dob,String m,Department d,LocalDate j,String s,String ie,String a,String sn,String pn,LocalDate ad,int y,String gp,boolean sch){
        super(p,f,l,e,ph,dob,m,d,j,s,ie,a); studentNumber=sn;programName=pn;admissionDate=ad;studyYear=y;guardianPhone=gp;scholarshipStatus=sch;
    }
    public String getStudentNumber(){return studentNumber;} public void setStudentNumber(String v){studentNumber=v;}
    public String getProgramName(){return programName;} public void setProgramName(String v){programName=v;}
    public LocalDate getAdmissionDate(){return admissionDate;} public void setAdmissionDate(LocalDate v){admissionDate=v;}
    public int getStudyYear(){return studyYear;} public void setStudyYear(int v){studyYear=v;}
    public String getGuardianPhone(){return guardianPhone;} public void setGuardianPhone(String v){guardianPhone=v;}
    public boolean isScholarshipStatus(){return scholarshipStatus;} public void setScholarshipStatus(boolean v){scholarshipStatus=v;}
    public String getRoleDescription(){return "Student";} public String getAcademicCategory(){return "Undergraduate student";}
    public String getId(){return studentNumber;} public String getDisplayName(){return super.getDisplayName()+" ["+studentNumber+"]";}
    public String getRegistrationSummary(){return "Student "+studentNumber+": "+getDisplayName()+", "+programName+", year "+studyYear+", "+getDepartment().getDepartmentName();}
    public void validate() throws ValidationException {
        validateAcademicMember();
        if(studentNumber==null||studentNumber.trim().length()<4||programName==null||programName.trim().length()<2) throw new ValidationException("Student number (4+ chars) and program name (2+ chars) are required.");
        if(admissionDate==null||admissionDate.isAfter(LocalDate.now())||getJoinDate().isBefore(admissionDate)) throw new ValidationException("Admission date must be valid, not future, and no later than join date.");
        if(studyYear<1||studyYear>6) throw new ValidationException("Study year must be from 1 to 6.");
        if(guardianPhone==null||!guardianPhone.matches("\\+?\\d{10,15}")) throw new ValidationException("Guardian phone must contain 10-15 digits, optionally prefixed by +.");
    }
    public String toString(){return getRegistrationSummary();}
}
