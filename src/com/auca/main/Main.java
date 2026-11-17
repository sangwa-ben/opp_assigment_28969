package com.auca.main;

import com.auca.abstracts.AcademicMember;
import com.auca.abstracts.Person;
import com.auca.administrator.Administrator;
import com.auca.assessment.Assessment;
import com.auca.bookloan.BookLoan;
import com.auca.classroom.Classroom;
import com.auca.course.Course;
import com.auca.department.Department;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.BookUnavailableException;
import com.auca.exceptions.DuplicateRecordException;
import com.auca.exceptions.PrerequisiteMissingException;
import com.auca.exceptions.RecordNotFoundException;
import com.auca.exceptions.ValidationException;
import com.auca.feepayment.FeePayment;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import com.auca.teacher.Teacher;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class Main {
    private static final Scanner IN = new Scanner(System.in);
    private static final List<Department> DEPARTMENTS = new ArrayList<>();
    private static final List<Student> STUDENTS = new ArrayList<>();
    private static final List<Teacher> TEACHERS = new ArrayList<>();
    private static final List<Administrator> ADMINISTRATORS = new ArrayList<>();
    private static final List<Course> COURSES = new ArrayList<>();
    private static final List<Classroom> CLASSROOMS = new ArrayList<>();
    private static final List<Enrollment> ENROLLMENTS = new ArrayList<>();
    private static final List<Assessment> ASSESSMENTS = new ArrayList<>();
    private static final List<FeePayment> PAYMENTS = new ArrayList<>();
    private static final List<LibraryBook> BOOKS = new ArrayList<>();
    private static final List<BookLoan> LOANS = new ArrayList<>();

    private static final class Cancelled extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private static final class Field {
        final String label, format, kind;
        final Supplier<List<String>> choices;
        Field(String label, String format, String kind) { this(label, format, kind, null); }
        Field(String label, String format, String kind, Supplier<List<String>> choices) {
            this.label=label; this.format=format; this.kind=kind; this.choices=choices;
        }
    }
    private static Field f(String name,String format,String kind){return new Field(name,format,kind);}
    private static Field select(String name,String type,Supplier<List<String>> choices){return new Field(name,type+" (choose a number)", "SELECT",choices);}

    public static void main(String[] args) {
        while (true) {
            menu();
            String choice = IN.nextLine().trim();
            if ("0".equals(choice)) { System.out.println("Goodbye."); return; }
            try {
                switch (choice) {
                    case "1": registerDepartment(); break;
                    case "2": registerStudent(); break;
                    case "3": registerTeacher(); break;
                    case "4": registerAdministrator(); break;
                    case "5": registerCourse(); break;
                    case "6": registerClassroom(); break;
                    case "7": registerEnrollment(); break;
                    case "8": registerAssessment(); break;
                    case "9": registerPayment(); break;
                    case "10": registerBook(); break;
                    case "11": borrowBook(); break;
                    case "12": returnBook(); break;
                    case "13": viewAll(); break;
                    case "14": search(); break;
                    case "15": update(); break;
                    case "16": academicSummary(); break;
                    case "17": paymentHistory(); break;
                    case "18": availableBooks(); break;
                    case "19": demonstratePolymorphism(); break;
                    default: System.out.println("Select a menu option from 0 to 19.");
                }
            } catch (Cancelled ignored) {
                System.out.println("Operation cancelled; no unsaved changes were made.");
            } catch (ValidationException | DuplicateRecordException | RecordNotFoundException |
                     PrerequisiteMissingException | BookUnavailableException ex) {
                System.out.println("Unable to complete operation: " + ex.getMessage());
            }
            System.out.println();
        }
    }

    private static void menu() {
        System.out.println("========== AUCA_ID SCHOOL MANAGEMENT ==========");
        System.out.println("1.  Register Department");
        System.out.println("2.  Register Student");
        System.out.println("3.  Register Teacher");
        System.out.println("4.  Register Administrator");
        System.out.println("5.  Register Course");
        System.out.println("6.  Register Classroom");
        System.out.println("7.  Enroll Student");
        System.out.println("8.  Record Assessment");
        System.out.println("9.  Record Fee Payment");
        System.out.println("10. Register Library Book");
        System.out.println("11. Borrow Library Book");
        System.out.println("12. Return Library Book");
        System.out.println("13. View All Records");
        System.out.println("14. Search Record by ID");
        System.out.println("15. Update Existing Record");
        System.out.println("16. Student Academic Summary");
        System.out.println("17. Student Payment History");
        System.out.println("18. View Available Books");
        System.out.println("19. Demonstrate Polymorphism");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
    }

    private static String[] form(String title, List<Field> fields) throws ValidationException {
        String[] values=new String[fields.size()];
        int i=0;
        while(i<fields.size()) {
            Field field=fields.get(i);
            int step=Math.min(3,(i*3/fields.size())+1);
            System.out.println("\n------------- "+title.toUpperCase()+" | STEP "+step+" OF 4 -------------");
            System.out.println("Enter BACK for the previous field or CANCEL to discard.");
            String value=askField(field);
            if(value==null) { if(i>0)i--; else System.out.println("Already at the first field."); continue; }
            values[i++]=value;
        }
        while(true) {
            System.out.println("\n------------- REVIEW "+title.toUpperCase()+" | STEP 4 OF 4 -------------");
            for(int j=0;j<fields.size();j++) System.out.println((j+1)+". "+fields.get(j).label+": "+display(fields.get(j),values[j]));
            System.out.println("1. Save record   2. Edit a field   0. Cancel");
            String action=read("Select: ");
            if("1".equals(action)) return values;
            if("0".equals(action)) throw new Cancelled();
            if("2".equals(action)) {
                int n=integer("Field number (1-"+fields.size()+"): ",1,fields.size());
                String updated=askField(fields.get(n-1));
                if(updated!=null)values[n-1]=updated;
            } else System.out.println("Choose 1, 2, or 0.");
        }
    }

    private static String askField(Field field) {
        while(true) {
            List<String> options=field.choices==null?null:field.choices.get();
            if(options!=null) {
                System.out.println(field.label+" ["+field.format+"]");
                for(int i=0;i<options.size();i++)System.out.println("  "+(i+1)+". "+options.get(i));
                System.out.print("  Select (BACK/CANCEL): ");
            } else System.out.print(field.label+" ["+field.format+"]: ");
            String value=IN.nextLine().trim();
            if("CANCEL".equalsIgnoreCase(value))throw new Cancelled();
            if("BACK".equalsIgnoreCase(value))return null;
            try {
                if(options!=null) {
                    int n=Integer.parseInt(value);
                    if(n<1||n>options.size())throw new ValidationException("Select one of the displayed choices.");
                    return value;
                }
                validate(field.kind,value);
                uniqueAtEntry(field.label,value);
                return value;
            } catch(NumberFormatException ex) { System.out.println("Enter a valid number."); }
            catch(ValidationException ex) { System.out.println("Invalid "+field.label+": "+ex.getMessage()); }
        }
    }

    private static void validate(String kind,String v) throws ValidationException {
        boolean ok;
        switch(kind) {
            case "ID": ok=v.length()>=4&&v.length()<=30; break;
            case "CODE": ok=v.matches("[A-Za-z0-9-]{2,20}"); break;
            case "NAME": ok=v.trim().length()>=2&&v.trim().length()<=60; break;
            case "TEXT": ok=!v.trim().isEmpty(); break;
            case "EMAIL": ok=v.length()<=120&&v.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"); break;
            case "PHONE": ok=v.matches("\\+?\\d{10,15}"); break;
            case "DATE_PAST": ok=date(v).isBefore(LocalDate.now()); break;
            case "DATE": ok=!date(v).isAfter(LocalDate.now()); break;
            case "DATE_ANY": date(v); ok=true; break;
            case "YEAR": number(v,1,6); ok=true; break;
            case "CREDITS": number(v,1,6); ok=true; break;
            case "CAPACITY": number(v,1,Integer.MAX_VALUE); ok=true; break;
            case "FLOOR": number(v,0,Integer.MAX_VALUE); ok=true; break;
            case "ACCESS": number(v,1,5); ok=true; break;
            case "NONNEG": number(v,0,Integer.MAX_VALUE); ok=true; break;
            case "POSITIVE": ok=new BigDecimal(v).signum()>0; break;
            case "SCORE": ok=Double.isFinite(Double.parseDouble(v))&&Double.parseDouble(v)>=0; break;
            case "ISBN": ok=v.matches("(?:\\d{9}[\\dXx]|\\d{13})"); break;
            case "BOOL": ok=v.matches("(?i)YES|NO|TRUE|FALSE|1|2"); break;
            case "STATUS": ok=v.matches("ACTIVE|ON_LEAVE|INACTIVE"); break;
            case "ENROLL_STATUS": ok=v.matches("ACTIVE|DROPPED|COMPLETED"); break;
            case "SEMESTER": ok=v.matches("(?i)(semester )?[1-3](?:[- ]20\\d{2})?"); break;
            case "EMPLOYMENT": ok=v.matches("FULL_TIME|PART_TIME|CONTRACT"); break;
            case "ASSESSMENT": ok=v.matches("QUIZ|ASSIGNMENT|MIDTERM|FINAL|PROJECT|EXAM"); break;
            case "METHOD": ok=v.matches("CASH|CARD|BANK_TRANSFER|MOBILE_MONEY"); break;
            case "DERIVED": ok=v.isEmpty(); break;
            default: ok=!v.trim().isEmpty();
        }
        if(!ok)throw new ValidationException("Value does not match the required format.");
    }
    private static LocalDate date(String s) throws ValidationException {
        try{return LocalDate.parse(s);}catch(DateTimeParseException ex){throw new ValidationException("Use a valid YYYY-MM-DD date.");}
    }
    private static int number(String s,int min,int max) throws ValidationException {
        try{int n=Integer.parseInt(s);if(n<min||n>max)throw new ValidationException("Enter a whole number from "+min+" to "+max+".");return n;}
        catch(NumberFormatException ex){throw new ValidationException("Enter a whole number.");}
    }
    private static int integer(String prompt,int min,int max) {
        while(true)try{return number(read(prompt),min,max);}catch(ValidationException ex){System.out.println(ex.getMessage());}
    }
    private static String read(String prompt) {
        System.out.print(prompt);String v=IN.nextLine().trim();if("CANCEL".equalsIgnoreCase(v))throw new Cancelled();return v;
    }
    private static String display(Field f,String v) {
        if(f.choices==null) return "DERIVED".equals(f.kind)&&v.isEmpty()?"(not set until returned)":v;
        int n=Integer.parseInt(v)-1;List<String> options=f.choices.get();return n>=0&&n<options.size()?options.get(n):v;
    }
    private static boolean yes(String s){return s.equalsIgnoreCase("YES")||s.equalsIgnoreCase("TRUE")||"1".equals(s);}
    private static int selected(String value){return Integer.parseInt(value)-1;}

    private static List<String> options(List<? extends Registrable> records) {
        List<String> result=new ArrayList<>();for(Registrable r:records)result.add(r.getRegistrationSummary());return result;
    }
    private static List<String> teacherChoices() {
        List<String> result=options(TEACHERS);result.add("No head teacher assigned");return result;
    }
    private static List<Enrollment> activeEnrollments() {
        List<Enrollment> result=new ArrayList<>();for(Enrollment e:ENROLLMENTS)if(e.isActive())result.add(e);return result;
    }
    private static void ensureDepartment() throws ValidationException,DuplicateRecordException {
        if(DEPARTMENTS.isEmpty()){System.out.println("No department is registered; register the required department first.");registerDepartment();}
    }
    private static void ensureTeacher() throws ValidationException,DuplicateRecordException {
        if(TEACHERS.isEmpty()){System.out.println("No teacher is registered; register the required teacher first.");registerTeacher();}
    }
    private static void ensureStudent() throws ValidationException,DuplicateRecordException {
        if(STUDENTS.isEmpty()){System.out.println("No student is registered; register the required student first.");registerStudent();}
    }
    private static void ensureCourse() throws ValidationException,DuplicateRecordException,PrerequisiteMissingException {
        if(COURSES.isEmpty()){System.out.println("No course is registered; register the required course first.");registerCourse();}
    }

    private static void registerDepartment() throws ValidationException,DuplicateRecordException {
        String[] v=form("Register Department",Arrays.asList(
            f("Department ID","unique String, 4-30 characters","ID"),f("Department name","String, 2-60 characters","NAME"),
            f("Department code","unique 2-20 letters/digits/hyphens","CODE"),f("Office location","required text","TEXT"),
            f("Contact email","valid email","EMAIL"),select("Head teacher","optional Teacher",Main::teacherChoices)));
        Teacher head=selected(v[5])==TEACHERS.size()?null:TEACHERS.get(selected(v[5]));
        Department item=new Department(v[0],v[1],v[2].toUpperCase(),v[3],v[4],head);
        save(item,DEPARTMENTS,v[2]);
    }

    private static void registerStudent() throws ValidationException,DuplicateRecordException {
        ensureDepartment();
        List<Field> f=Arrays.asList(
            f("Person ID","unique String, 4-30 characters","ID"),f("First name","String, 2-60 characters","NAME"),
            f("Last name","String, 2-60 characters","NAME"),f("Personal email","valid email","EMAIL"),
            f("Phone","10-15 digits, optional leading +","PHONE"),f("Date of birth","LocalDate, YYYY-MM-DD, past","DATE_PAST"),
            f("Member number","unique String, 4-30 characters","ID"),select("Department","existing Department",()->options(DEPARTMENTS)),
            f("Join date","LocalDate, YYYY-MM-DD, not future","DATE"),f("Academic status","ACTIVE / ON_LEAVE / INACTIVE","STATUS"),
            f("Institutional email","valid email","EMAIL"),f("Advisor name","required text; N/A if none","TEXT"),
            f("Student number","unique String, 4-30 characters","ID"),f("Program name","String, 2-60 characters","NAME"),
            f("Admission date","LocalDate, YYYY-MM-DD, not future","DATE"),f("Study year","integer, 1-6","YEAR"),
            f("Guardian phone","10-15 digits, optional leading +","PHONE"),f("Scholarship","YES / NO","BOOL"));
        String[] v=form("Register Student",f);
        Student item=new Student(v[0],v[1],v[2],v[3],v[4],date(v[5]),v[6],DEPARTMENTS.get(selected(v[7])),
            date(v[8]),v[9].toUpperCase(),v[10],v[11],v[12],v[13],date(v[14]),Integer.parseInt(v[15]),v[16],yes(v[17]));
        for(Person p:people())if(p.getPersonId().equalsIgnoreCase(v[0])||
            (p instanceof AcademicMember&&((AcademicMember)p).getMemberNumber().equalsIgnoreCase(v[6])))
            throw new DuplicateRecordException("Person ID or academic member number already exists.");
        save(item,STUDENTS,v[12]);
    }

    private static void registerTeacher() throws ValidationException,DuplicateRecordException {
        ensureDepartment();
        List<Field> f=Arrays.asList(
            f("Person ID","unique String, 4-30 characters","ID"),f("First name","String, 2-60 characters","NAME"),
            f("Last name","String, 2-60 characters","NAME"),f("Personal email","valid email","EMAIL"),
            f("Phone","10-15 digits, optional leading +","PHONE"),f("Date of birth","LocalDate, YYYY-MM-DD, past","DATE_PAST"),
            f("Member number","unique String, 4-30 characters","ID"),select("Department","existing Department",()->options(DEPARTMENTS)),
            f("Join date","LocalDate, YYYY-MM-DD, not future","DATE"),f("Academic status","ACTIVE / ON_LEAVE / INACTIVE","STATUS"),
            f("Institutional email","valid email","EMAIL"),f("Advisor name","required text; N/A if none","TEXT"),
            f("Employee number","unique String, 4-30 characters","ID"),f("Specialization","required text","TEXT"),
            f("Hire date","LocalDate, YYYY-MM-DD, not future","DATE"),f("Salary","positive decimal","POSITIVE"),
            f("Office number","required text","TEXT"),f("Employment type","FULL_TIME / PART_TIME / CONTRACT","EMPLOYMENT"));
        String[] v=form("Register Teacher",f);
        Teacher item=new Teacher(v[0],v[1],v[2],v[3],v[4],date(v[5]),v[6],DEPARTMENTS.get(selected(v[7])),
            date(v[8]),v[9].toUpperCase(),v[10],v[11],v[12],v[13],date(v[14]),new BigDecimal(v[15]),v[16],v[17].toUpperCase());
        for(Person p:people())if(p.getPersonId().equalsIgnoreCase(v[0])||
            (p instanceof AcademicMember&&((AcademicMember)p).getMemberNumber().equalsIgnoreCase(v[6])))
            throw new DuplicateRecordException("Person ID or academic member number already exists.");
        save(item,TEACHERS,v[12]);
    }

    private static void registerAdministrator() throws ValidationException,DuplicateRecordException {
        List<Field> f=Arrays.asList(f("Person ID","unique String, 4-30 characters","ID"),
            f("First name","String, 2-60 characters","NAME"),f("Last name","String, 2-60 characters","NAME"),
            f("Personal email","valid email","EMAIL"),f("Phone","10-15 digits, optional leading +","PHONE"),
            f("Date of birth","LocalDate, YYYY-MM-DD, past","DATE_PAST"),f("Staff number","unique String, 4-30 characters","ID"),
            f("Job title","required text","TEXT"),f("Assigned office","required text","TEXT"),
            f("Employment date","LocalDate, YYYY-MM-DD, not future","DATE"),f("Access level","integer, 1-5","ACCESS"),
            f("Active","YES / NO","BOOL"));
        String[] v=form("Register Administrator",f);
        for(Person p:people())if(p.getPersonId().equalsIgnoreCase(v[0]))throw new DuplicateRecordException("Person ID already exists.");
        Administrator item=new Administrator(v[0],v[1],v[2],v[3],v[4],date(v[5]),v[6],v[7],v[8],date(v[9]),Integer.parseInt(v[10]),yes(v[11]));
        save(item,ADMINISTRATORS,null);
    }

    private static void registerCourse() throws ValidationException,DuplicateRecordException,PrerequisiteMissingException {
        ensureDepartment();ensureTeacher();
        List<Field> f=Arrays.asList(f("Course ID","unique String, 4-30 characters","ID"),
            f("Course code","unique 2-20 letters/digits/hyphens","CODE"),f("Course title","String, 2-60 characters","NAME"),
            f("Credit hours","integer, 1-6","CREDITS"),select("Department","existing Department",()->options(DEPARTMENTS)),
            select("Assigned teacher","existing Teacher",()->options(TEACHERS)));
        String[] v=form("Register Course",f);
        Department dep=DEPARTMENTS.get(selected(v[4]));Teacher teacher=TEACHERS.get(selected(v[5]));
        if(teacher.getDepartment()!=dep) {
            System.out.println("The selected teacher belongs to a different department.");
            if(yes(read("Register a teacher for the selected department and retry? YES/NO: "))) {
                registerTeacher();registerCourse();return;
            }
            throw new PrerequisiteMissingException("A teacher in the selected department is required.");
        }
        save(new Course(v[0],v[1].toUpperCase(),v[2],Integer.parseInt(v[3]),dep,teacher),COURSES,v[1]);
    }

    private static void registerClassroom() throws ValidationException,DuplicateRecordException {
        String[] v=form("Register Classroom",Arrays.asList(f("Classroom ID","unique String, 4-30 characters","ID"),
            f("Building name","String, 2-60 characters","NAME"),f("Room number","required text","TEXT"),
            f("Capacity","positive integer","CAPACITY"),f("Floor number","integer, zero or greater","FLOOR"),
            f("Available","YES / NO","BOOL")));
        save(new Classroom(v[0],v[1],v[2],Integer.parseInt(v[3]),Integer.parseInt(v[4]),yes(v[5])),CLASSROOMS,null);
    }

    private static void registerEnrollment() throws ValidationException,DuplicateRecordException,PrerequisiteMissingException {
        ensureStudent();ensureCourse();
        String[] v=form("Enroll Student",Arrays.asList(f("Enrollment ID","unique String, 4-30 characters","ID"),
            select("Student","existing Student",()->options(STUDENTS)),select("Course","existing Course",()->options(COURSES)),
            f("Enrollment date","LocalDate, YYYY-MM-DD, not future","DATE"),
            f("Semester","Semester 1 / 2 / 3, optional year","SEMESTER"),
            f("Enrollment status","ACTIVE / DROPPED / COMPLETED","ENROLL_STATUS")));
        Student student=STUDENTS.get(selected(v[1]));Course course=COURSES.get(selected(v[2]));
        String semester=v[4].replaceAll("(?i)^semester\\s*","Semester ");
        for(Enrollment e:ENROLLMENTS)if(e.isActive()&&e.getStudent()==student&&e.getCourse()==course&&e.getSemester().equalsIgnoreCase(semester))
            throw new DuplicateRecordException("This student is already actively enrolled in that course and semester.");
        save(new Enrollment(v[0],student,course,date(v[3]),semester,v[5].toUpperCase()),ENROLLMENTS,null);
    }

    private static void registerAssessment() throws ValidationException,DuplicateRecordException,PrerequisiteMissingException {
        if(activeEnrollments().isEmpty()) {
            System.out.println("No active enrollment is available. Register prerequisites and enroll a student first.");
            ensureStudent();ensureCourse();registerEnrollment();
            if(activeEnrollments().isEmpty())throw new PrerequisiteMissingException("An active enrollment is required.");
        }
        String[] v=form("Record Assessment",Arrays.asList(f("Assessment ID","unique String, 4-30 characters","ID"),
            select("Active enrollment","existing active Enrollment",()->options(activeEnrollments())),
            f("Assessment type","QUIZ / ASSIGNMENT / MIDTERM / FINAL / PROJECT / EXAM","ASSESSMENT"),
            f("Maximum score","positive number","POSITIVE"),f("Obtained score","zero or greater; not above maximum","SCORE"),
            f("Assessment date","LocalDate, YYYY-MM-DD, not future","DATE")));
        double max=Double.parseDouble(v[3]),score=Double.parseDouble(v[4]);
        if(score>max)throw new ValidationException("Obtained score cannot exceed maximum score.");
        save(new Assessment(v[0],activeEnrollments().get(selected(v[1])),v[2].toUpperCase(),max,score,date(v[5])),ASSESSMENTS,null);
    }

    private static void registerPayment() throws ValidationException,DuplicateRecordException {
        ensureStudent();
        String[] v=form("Record Fee Payment",Arrays.asList(f("Payment ID","unique String, 4-30 characters","ID"),
            select("Student","existing Student",()->options(STUDENTS)),f("Amount","positive decimal","POSITIVE"),
            f("Payment date","LocalDate, YYYY-MM-DD, not future","DATE"),
            f("Payment method","CASH / CARD / BANK_TRANSFER / MOBILE_MONEY","METHOD"),
            f("Receipt number","required, unique text","TEXT")));
        for(FeePayment p:PAYMENTS)if(p.getReceiptNumber().equalsIgnoreCase(v[5]))throw new DuplicateRecordException("Receipt number already exists.");
        save(new FeePayment(v[0],STUDENTS.get(selected(v[1])),new BigDecimal(v[2]),date(v[3]),v[4].toUpperCase(),v[5]),PAYMENTS,v[5]);
    }

    private static void registerBook() throws ValidationException,DuplicateRecordException {
        String[] v=form("Register Library Book",Arrays.asList(f("Book ID","unique String, 4-30 characters","ID"),
            f("ISBN","10-digit (last may be X) or 13-digit ISBN","ISBN"),f("Title","String, 2-60 characters","NAME"),
            f("Author","String, 2-60 characters","NAME"),f("Total copies","integer, zero or greater","NONNEG"),
            f("Available copies","integer, zero or greater, no more than total","NONNEG")));
        if(Integer.parseInt(v[5])>Integer.parseInt(v[4]))throw new ValidationException("Available copies cannot exceed total copies.");
        for(LibraryBook b:BOOKS)if(b.getIsbn().equalsIgnoreCase(v[1]))throw new DuplicateRecordException("ISBN already exists.");
        save(new LibraryBook(v[0],v[1],v[2],v[3],Integer.parseInt(v[4]),Integer.parseInt(v[5])),BOOKS,v[1]);
    }

    private static void borrowBook() throws ValidationException,DuplicateRecordException,PrerequisiteMissingException,BookUnavailableException {
        ensureStudent();
        if(BOOKS.isEmpty()){System.out.println("No books are registered; register one first.");registerBook();}
        List<LibraryBook> stocked=new ArrayList<>();for(LibraryBook b:BOOKS)if(b.getAvailableCopies()>0)stocked.add(b);
        if(stocked.isEmpty())throw new BookUnavailableException("No copies are currently available to borrow.");
        String[] v=form("Borrow Library Book",Arrays.asList(f("Loan ID","unique String, 4-30 characters","ID"),
            select("Student","existing Student",()->options(STUDENTS)),select("Library book","book with available copies",()->options(stocked)),
            f("Borrowed date","LocalDate, YYYY-MM-DD, not future","DATE"),
            f("Due date","LocalDate, YYYY-MM-DD, after borrowed date","DATE_ANY"),
            f("Returned date","derived; press ENTER (recorded by Return Book)","DERIVED")));
        LibraryBook book=stocked.get(selected(v[2]));
        BookLoan loan=new BookLoan(v[0],STUDENTS.get(selected(v[1])),book,date(v[3]),date(v[4]),null);
        loan.validate();
        if(book.getAvailableCopies()<1)throw new BookUnavailableException("No copies are currently available to borrow.");
        book.setAvailableCopies(book.getAvailableCopies()-1);LOANS.add(loan);
        System.out.println("Book loan saved. Available copies: "+book.getAvailableCopies()+".");
    }

    private static void returnBook() throws ValidationException,RecordNotFoundException {
        List<BookLoan> open=new ArrayList<>();for(BookLoan l:LOANS)if(l.isActive())open.add(l);
        if(open.isEmpty())throw new RecordNotFoundException("There are no outstanding book loans.");
        BookLoan loan=open.get(selectRecord("Select loan to return:",open));
        LocalDate returned=date(read("Return date (YYYY-MM-DD): "));
        if(returned.isBefore(loan.getBorrowedDate())||returned.isAfter(LocalDate.now()))throw new ValidationException("Return date must be on or after borrowing and not in the future.");
        if(confirm("Return "+loan.getBook().getTitle()+" on "+returned+"?")) {
            loan.setReturnedDate(returned);loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies()+1);
            System.out.println("Book returned. Available copies: "+loan.getBook().getAvailableCopies()+".");
        }
    }

    private static void save(Registrable item,List<? extends Registrable> records,String secondary)
            throws ValidationException,DuplicateRecordException {
        item.validate();uniqueId(item.getId(),allRecords());
        if(secondary!=null) {
            if(item instanceof Course)for(Course c:COURSES)if(c.getCourseCode().equalsIgnoreCase(secondary))throw new DuplicateRecordException("Course code already exists.");
            if(item instanceof Department)for(Department d:DEPARTMENTS)if(d.getDepartmentCode().equalsIgnoreCase(secondary))throw new DuplicateRecordException("Department code already exists.");
            if(item instanceof LibraryBook)for(LibraryBook b:BOOKS)if(b.getIsbn().equalsIgnoreCase(secondary))throw new DuplicateRecordException("ISBN already exists.");
            if(item instanceof FeePayment)for(FeePayment p:PAYMENTS)if(p.getReceiptNumber().equalsIgnoreCase(secondary))throw new DuplicateRecordException("Receipt number already exists.");
        }
        @SuppressWarnings("unchecked") List<Registrable> writable=(List<Registrable>)records;
        writable.add(item);System.out.println(item.getRoleDescription()+" saved: "+item.getId());
    }
    private static void uniqueId(String id,List<Registrable> records) throws DuplicateRecordException {
        for(Registrable r:records)if(r.getId().equalsIgnoreCase(id))throw new DuplicateRecordException("ID '"+id+"' already exists.");
    }
    private static void uniqueAtEntry(String label,String v) throws ValidationException {
        boolean duplicate=false;
        switch(label) {
            case "Person ID": for(Person p:people())if(p.getPersonId().equalsIgnoreCase(v))duplicate=true;break;
            case "Member number":for(Person p:people())if(p instanceof AcademicMember&&((AcademicMember)p).getMemberNumber().equalsIgnoreCase(v))duplicate=true;break;
            case "Department ID":case "Student number":case "Employee number":case "Staff number":case "Course ID":
            case "Classroom ID":case "Enrollment ID":case "Assessment ID":case "Payment ID":case "Book ID":case "Loan ID":
                for(Registrable r:allRecords())if(r.getId().equalsIgnoreCase(v))duplicate=true;break;
            case "Department code":for(Department d:DEPARTMENTS)if(d.getDepartmentCode().equalsIgnoreCase(v))duplicate=true;break;
            case "Course code":for(Course c:COURSES)if(c.getCourseCode().equalsIgnoreCase(v))duplicate=true;break;
            case "ISBN":for(LibraryBook b:BOOKS)if(b.getIsbn().equalsIgnoreCase(v))duplicate=true;break;
            case "Receipt number":for(FeePayment p:PAYMENTS)if(p.getReceiptNumber().equalsIgnoreCase(v))duplicate=true;break;
            default:break;
        }
        if(duplicate)throw new ValidationException("That value is already in use.");
    }
    private static List<Person> people(){List<Person> p=new ArrayList<>();p.addAll(STUDENTS);p.addAll(TEACHERS);p.addAll(ADMINISTRATORS);return p;}
    private static List<Registrable> allRecords(){
        List<Registrable> r=new ArrayList<>();r.addAll(DEPARTMENTS);r.addAll(STUDENTS);r.addAll(TEACHERS);r.addAll(ADMINISTRATORS);
        r.addAll(COURSES);r.addAll(CLASSROOMS);r.addAll(ENROLLMENTS);r.addAll(ASSESSMENTS);r.addAll(PAYMENTS);r.addAll(BOOKS);r.addAll(LOANS);return r;
    }
    private static boolean confirm(String message){System.out.println("Review: "+message);while(true){String v=read("1. Confirm   0. Cancel: ");if("1".equals(v))return true;if("0".equals(v))return false;System.out.println("Choose 1 or 0.");}}
    private static int selectRecord(String prompt,List<? extends Registrable> records){
        if(records.isEmpty())throw new Cancelled();System.out.println(prompt);
        for(int i=0;i<records.size();i++)System.out.println((i+1)+". "+records.get(i).getRegistrationSummary());
        int n=integer("Select (0. Cancel): ",0,records.size());if(n==0)throw new Cancelled();return n-1;
    }

    private static void viewAll(){List<Registrable> r=allRecords();if(r.isEmpty()){System.out.println("No records have been registered.");return;}for(Registrable item:r)item.printDetails();}
    private static void search() throws RecordNotFoundException {
        String id=read("Enter record ID: ");
        for(Registrable r:allRecords()) {
            if(r.getId().equalsIgnoreCase(id)){r.printDetails();return;}
            if(r instanceof Person&&((Person)r).getPersonId().equalsIgnoreCase(id)){r.printDetails();return;}
        }
        throw new RecordNotFoundException("No record found with ID "+id+".");
    }

    private static void update() throws RecordNotFoundException,ValidationException {
        String id=read("Enter record ID to update: ");
        for(Registrable r:allRecords())if(r.getId().equalsIgnoreCase(id)) {
            if(r instanceof Person) {
                Person p=(Person)r;String v=readValid("New email","EMAIL");if(!confirm("Change email to "+v+"?"))return;
                String old=p.getEmail();p.setEmail(v);try{p.validate();}catch(ValidationException e){p.setEmail(old);throw e;}
            } else if(r instanceof Department) {
                Department d=(Department)r;String v=readValid("New department name","NAME");if(!confirm("Change department name to "+v+"?"))return;
                String old=d.getDepartmentName();d.setDepartmentName(v);try{d.validate();}catch(ValidationException e){d.setDepartmentName(old);throw e;}
            } else if(r instanceof Course) {
                Course c=(Course)r;String v=readValid("New course title","NAME");if(!confirm("Change course title to "+v+"?"))return;
                String old=c.getCourseTitle();c.setCourseTitle(v);try{c.validate();}catch(ValidationException e){c.setCourseTitle(old);throw e;}
            } else if(r instanceof Classroom) {
                Classroom c=(Classroom)r;int v=integer("New capacity: ",1,Integer.MAX_VALUE);if(!confirm("Change capacity to "+v+"?"))return;
                int old=c.getCapacity();c.setCapacity(v);try{c.validate();}catch(ValidationException e){c.setCapacity(old);throw e;}
            } else if(r instanceof LibraryBook) {
                LibraryBook b=(LibraryBook)r;String v=readValid("New title","NAME");if(!confirm("Change title to "+v+"?"))return;
                String old=b.getTitle();b.setTitle(v);try{b.validate();}catch(ValidationException e){b.setTitle(old);throw e;}
            } else if(r instanceof Enrollment) {
                Enrollment e=(Enrollment)r;String v=readValid("New status (ACTIVE / DROPPED / COMPLETED)","ENROLL_STATUS").toUpperCase();if(!confirm("Change status to "+v+"?"))return;
                String old=e.getEnrollmentStatus();e.setEnrollmentStatus(v);try{e.validate();}catch(ValidationException ex){e.setEnrollmentStatus(old);throw ex;}
            } else if(r instanceof Assessment) {
                Assessment a=(Assessment)r;double v=Double.parseDouble(readValid("New obtained score","SCORE"));if(!confirm("Change score to "+v+"?"))return;
                double old=a.getObtainedScore();a.setObtainedScore(v);try{a.validate();}catch(ValidationException e){a.setObtainedScore(old);throw e;}
            } else if(r instanceof FeePayment) {
                FeePayment p=(FeePayment)r;BigDecimal v=new BigDecimal(readValid("New amount","POSITIVE"));if(!confirm("Change amount to "+v+"?"))return;
                BigDecimal old=p.getAmount();p.setAmount(v);try{p.validate();}catch(ValidationException e){p.setAmount(old);throw e;}
            } else if(r instanceof BookLoan) {
                BookLoan l=(BookLoan)r;LocalDate v=date(readValid("New due date","DATE_ANY"));if(!confirm("Change due date to "+v+"?"))return;
                LocalDate old=l.getDueDate();l.setDueDate(v);try{l.validate();}catch(ValidationException e){l.setDueDate(old);throw e;}
            } else throw new ValidationException("No editable fields are available for this record.");
            System.out.println("Updated: "+r.getRegistrationSummary());return;
        }
        throw new RecordNotFoundException("No record found with ID "+id+".");
    }
    private static String readValid(String label,String kind) {
        while(true){String v=read(label+": ");try{validate(kind,v);return v;}catch(ValidationException e){System.out.println(e.getMessage());}}
    }
    private static void academicSummary() throws RecordNotFoundException {
        Student s=studentChoice();System.out.println("Academic summary for "+s.getDisplayName());
        for(Enrollment e:ENROLLMENTS)if(e.getStudent()==s){System.out.println(e.getRegistrationSummary());for(Assessment a:ASSESSMENTS)if(a.getEnrollment()==e)System.out.println("  "+a.getAssessmentType()+": "+a.getObtainedScore()+"/"+a.getMaximumScore());}
    }
    private static void paymentHistory() throws RecordNotFoundException {
        Student s=studentChoice();BigDecimal total=BigDecimal.ZERO;System.out.println("Payment history for "+s.getDisplayName());
        for(FeePayment p:PAYMENTS)if(p.getStudent()==s){p.printDetails();total=total.add(p.getAmount());}System.out.println("Total paid: "+total);
    }
    private static Student studentChoice() throws RecordNotFoundException {
        if(STUDENTS.isEmpty())throw new RecordNotFoundException("No student is registered.");
        return STUDENTS.get(selectRecord("Select student:",STUDENTS));
    }
    private static void availableBooks(){boolean found=false;for(LibraryBook b:BOOKS)if(b.getAvailableCopies()>0){b.printDetails();found=true;}if(!found)System.out.println("No books are currently available.");}
    private static void demonstratePolymorphism(){
        List<Person> p=people();if(p.isEmpty()){System.out.println("Register a student, teacher or administrator first.");return;}
        for(Person x:p)System.out.println(x.getRoleDescription()+" | "+x.getDisplayName()+" | academic category: "+x.getAcademicCategory());
    }
}
