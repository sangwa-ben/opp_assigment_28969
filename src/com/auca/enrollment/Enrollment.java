package com.auca.enrollment;
import com.auca.course.Course;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;
import java.time.LocalDate;
public class Enrollment implements Registrable {
    private String enrollmentId;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private String semester, enrollmentStatus;
    public Enrollment(String id,Student s,Course c,LocalDate d,String sem,String status){enrollmentId=id;student=s;course=c;enrollmentDate=d;semester=sem;enrollmentStatus=status;}
    public String getEnrollmentId(){return enrollmentId;} public void setEnrollmentId(String v){enrollmentId=v;}
    public Student getStudent(){return student;} public void setStudent(Student v){student=v;}
    public Course getCourse(){return course;} public void setCourse(Course v){course=v;}
    public LocalDate getEnrollmentDate(){return enrollmentDate;} public void setEnrollmentDate(LocalDate v){enrollmentDate=v;}
    public String getSemester(){return semester;} public void setSemester(String v){semester=v;}
    public String getEnrollmentStatus(){return enrollmentStatus;} public void setEnrollmentStatus(String v){enrollmentStatus=v;}
    public String getId(){return enrollmentId;} public String getDisplayName(){return student.getDisplayName()+" - "+course.getCourseCode()+" ("+semester+")";}
    public boolean isActive(){return "ACTIVE".equals(enrollmentStatus);}
    public String getRegistrationSummary(){return "Enrollment "+enrollmentId+": "+getDisplayName()+", "+enrollmentDate+", "+enrollmentStatus;}
    public void printDetails(){System.out.println(getRegistrationSummary());}
    public void validate() throws ValidationException {
        if(enrollmentId==null||enrollmentId.trim().length()<4||student==null||course==null) throw new ValidationException("Enrollment ID and existing student and course are required.");
        if(enrollmentDate==null||enrollmentDate.isAfter(LocalDate.now())) throw new ValidationException("Enrollment date must be today or earlier.");
        if(semester==null||!semester.matches("(?i)(semester )?[1-3](?:[- ]20\\d{2})?")) throw new ValidationException("Semester must be e.g. Semester 1, Semester 2, or Semester 3.");
        if(enrollmentStatus==null||!enrollmentStatus.matches("ACTIVE|DROPPED|COMPLETED")) throw new ValidationException("Status must be ACTIVE, DROPPED or COMPLETED.");
    }
    public String toString(){return getRegistrationSummary();}
}
