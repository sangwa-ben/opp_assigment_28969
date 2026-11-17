package com.auca.librarybook;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
public class LibraryBook implements Registrable {
    private String bookId, isbn, title, author;
    private int totalCopies, availableCopies;
    public LibraryBook(String id,String isbn,String title,String author,int total,int available){bookId=id;this.isbn=isbn;this.title=title;this.author=author;totalCopies=total;availableCopies=available;}
    public String getBookId(){return bookId;} public void setBookId(String v){bookId=v;}
    public String getIsbn(){return isbn;} public void setIsbn(String v){isbn=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getAuthor(){return author;} public void setAuthor(String v){author=v;}
    public int getTotalCopies(){return totalCopies;} public void setTotalCopies(int v){totalCopies=v;}
    public int getAvailableCopies(){return availableCopies;} public void setAvailableCopies(int v){availableCopies=v;}
    public String getId(){return bookId;} public String getDisplayName(){return title+" by "+author;} public boolean isActive(){return availableCopies>0;}
    public String getRegistrationSummary(){return "Book "+bookId+": "+title+" by "+author+", ISBN "+isbn+", stock "+availableCopies+"/"+totalCopies;}
    public void printDetails(){System.out.println(getRegistrationSummary());}
    public void validate() throws ValidationException {
        if(bookId==null||bookId.trim().length()<4||isbn==null||!isbn.matches("(?:\\d{9}[\\dXx]|\\d{13})")) throw new ValidationException("Book ID and a 10- or 13-digit ISBN are required.");
        if(title==null||title.trim().length()<2||author==null||author.trim().length()<2) throw new ValidationException("Book title and author must contain at least 2 characters.");
        if(totalCopies<0||availableCopies<0||availableCopies>totalCopies) throw new ValidationException("Copy counts must be nonnegative and available copies cannot exceed total.");
    }
    public String toString(){return getRegistrationSummary();}
}
