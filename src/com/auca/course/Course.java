package com.auca.course;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
import com.auca.teacher.Teacher;
public class Course implements Registrable {
    private String courseId, courseCode, courseTitle;
    private int creditHours;
    private Department department;
    private Teacher assignedTeacher;
    public Course(String id,String code,String title,int credits,Department dep,Teacher teacher){courseId=id;courseCode=code;courseTitle=title;creditHours=credits;department=dep;assignedTeacher=teacher;}
    public String getCourseId(){return courseId;} public void setCourseId(String v){courseId=v;}
    public String getCourseCode(){return courseCode;} public void setCourseCode(String v){courseCode=v;}
    public String getCourseTitle(){return courseTitle;} public void setCourseTitle(String v){courseTitle=v;}
    public int getCreditHours(){return creditHours;} public void setCreditHours(int v){creditHours=v;}
    public Department getDepartment(){return department;} public void setDepartment(Department v){department=v;}
    public Teacher getAssignedTeacher(){return assignedTeacher;} public void setAssignedTeacher(Teacher v){assignedTeacher=v;}
    public String getId(){return courseId;} public String getDisplayName(){return courseTitle+" ("+courseCode+")";} public boolean isActive(){return true;}
    public String getRegistrationSummary(){return "Course "+courseCode+" ["+courseId+"]: "+courseTitle+", "+creditHours+" credits, "+department.getDepartmentName()+", teacher "+assignedTeacher.getDisplayName();}
    public void printDetails(){System.out.println(getRegistrationSummary());}
    public void validate() throws ValidationException {
        if(courseId==null||courseId.trim().length()<4||courseCode==null||!courseCode.matches("[A-Za-z0-9-]{2,20}")) throw new ValidationException("Course ID (4+ chars) and code (2-20 letters, digits or hyphens) are required.");
        if(courseTitle==null||courseTitle.trim().length()<2) throw new ValidationException("Course title must contain at least 2 characters.");
        if(creditHours<1||creditHours>6) throw new ValidationException("Credit hours must be from 1 to 6.");
        if(department==null||assignedTeacher==null) throw new ValidationException("Select an existing department and teacher.");
        if(assignedTeacher.getDepartment()!=department) throw new ValidationException("The selected teacher must belong to the selected department.");
    }
    public String toString(){return getRegistrationSummary();}
}
