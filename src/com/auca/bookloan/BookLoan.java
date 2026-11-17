package com.auca.bookloan;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import java.time.LocalDate;
public class BookLoan implements Registrable {
    private String loanId;
    private Student student;
    private LibraryBook book;
    private LocalDate borrowedDate, dueDate, returnedDate;
    public BookLoan(String id,Student s,LibraryBook b,LocalDate borrowed,LocalDate due,LocalDate returned){loanId=id;student=s;book=b;borrowedDate=borrowed;dueDate=due;returnedDate=returned;}
    public String getLoanId(){return loanId;} public void setLoanId(String v){loanId=v;}
    public Student getStudent(){return student;} public void setStudent(Student v){student=v;}
    public LibraryBook getBook(){return book;} public void setBook(LibraryBook v){book=v;}
    public LocalDate getBorrowedDate(){return borrowedDate;} public void setBorrowedDate(LocalDate v){borrowedDate=v;}
    public LocalDate getDueDate(){return dueDate;} public void setDueDate(LocalDate v){dueDate=v;}
    public LocalDate getReturnedDate(){return returnedDate;} public void setReturnedDate(LocalDate v){returnedDate=v;}
    public String getId(){return loanId;} public String getDisplayName(){return book.getTitle()+" - "+student.getDisplayName();} public boolean isActive(){return returnedDate==null;}
    public String getRegistrationSummary(){return "Loan "+loanId+": "+book.getTitle()+" to "+student.getDisplayName()+", borrowed "+borrowedDate+", due "+dueDate+(returnedDate==null?", not returned":", returned "+returnedDate);}
    public void printDetails(){System.out.println(getRegistrationSummary());}
    public void validate() throws ValidationException {
        if(loanId==null||loanId.trim().length()<4||student==null||book==null) throw new ValidationException("Loan ID and existing student and book are required.");
        if(borrowedDate==null||dueDate==null||!dueDate.isAfter(borrowedDate)) throw new ValidationException("Due date must be after borrowed date.");
        if(returnedDate!=null&&returnedDate.isBefore(borrowedDate)) throw new ValidationException("Returned date cannot be before borrowed date.");
    }
    public String toString(){return getRegistrationSummary();}
}
