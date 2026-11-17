package com.auca.assessment;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
import java.time.LocalDate;
public class Assessment implements Registrable {
    private String assessmentId;
    private Enrollment enrollment;
    private String assessmentType;
    private double maximumScore, obtainedScore;
    private LocalDate assessmentDate;
    public Assessment(String id,Enrollment e,String type,double max,double score,LocalDate date){assessmentId=id;enrollment=e;assessmentType=type;maximumScore=max;obtainedScore=score;assessmentDate=date;}
    public String getAssessmentId(){return assessmentId;} public void setAssessmentId(String v){assessmentId=v;}
    public Enrollment getEnrollment(){return enrollment;} public void setEnrollment(Enrollment v){enrollment=v;}
    public String getAssessmentType(){return assessmentType;} public void setAssessmentType(String v){assessmentType=v;}
    public double getMaximumScore(){return maximumScore;} public void setMaximumScore(double v){maximumScore=v;}
    public double getObtainedScore(){return obtainedScore;} public void setObtainedScore(double v){obtainedScore=v;}
    public LocalDate getAssessmentDate(){return assessmentDate;} public void setAssessmentDate(LocalDate v){assessmentDate=v;}
    public String getId(){return assessmentId;} public String getDisplayName(){return enrollment.getStudent().getDisplayName()+" - "+assessmentType;} public boolean isActive(){return true;}
    public String getRegistrationSummary(){return "Assessment "+assessmentId+": "+getDisplayName()+", "+obtainedScore+"/"+maximumScore+", "+assessmentDate;}
    public void printDetails(){System.out.println(getRegistrationSummary());}
    public void validate() throws ValidationException {
        if(assessmentId==null||assessmentId.trim().length()<4||enrollment==null||!enrollment.isActive()) throw new ValidationException("Assessment ID and an active existing enrollment are required.");
        if(assessmentType==null||!assessmentType.matches("QUIZ|ASSIGNMENT|MIDTERM|FINAL|PROJECT|EXAM")) throw new ValidationException("Assessment type must be QUIZ, ASSIGNMENT, MIDTERM, FINAL, PROJECT or EXAM.");
        if(!Double.isFinite(maximumScore)||maximumScore<=0||!Double.isFinite(obtainedScore)||obtainedScore<0||obtainedScore>maximumScore) throw new ValidationException("Maximum score must be positive and obtained score must be between zero and maximum.");
        if(assessmentDate==null||assessmentDate.isAfter(LocalDate.now())) throw new ValidationException("Assessment date must not be in the future.");
    }
    public String toString(){return getRegistrationSummary();}
}
