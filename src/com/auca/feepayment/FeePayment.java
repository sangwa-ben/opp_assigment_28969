package com.auca.feepayment;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;
import java.math.BigDecimal;
import java.time.LocalDate;
public class FeePayment implements Registrable {
    private String paymentId;
    private Student student;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod, receiptNumber;
    public FeePayment(String id,Student s,BigDecimal a,LocalDate d,String method,String receipt){paymentId=id;student=s;amount=a;paymentDate=d;paymentMethod=method;receiptNumber=receipt;}
    public String getPaymentId(){return paymentId;} public void setPaymentId(String v){paymentId=v;}
    public Student getStudent(){return student;} public void setStudent(Student v){student=v;}
    public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
    public LocalDate getPaymentDate(){return paymentDate;} public void setPaymentDate(LocalDate v){paymentDate=v;}
    public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String v){paymentMethod=v;}
    public String getReceiptNumber(){return receiptNumber;} public void setReceiptNumber(String v){receiptNumber=v;}
    public String getId(){return paymentId;} public String getDisplayName(){return receiptNumber+" - "+student.getDisplayName();} public boolean isActive(){return true;}
    public String getRegistrationSummary(){return "Payment "+paymentId+": "+student.getDisplayName()+", "+amount+", "+paymentMethod+", receipt "+receiptNumber+" ("+paymentDate+")";}
    public void printDetails(){System.out.println(getRegistrationSummary());}
    public void validate() throws ValidationException {
        if(paymentId==null||paymentId.trim().length()<4||student==null) throw new ValidationException("Payment ID and existing student are required.");
        if(amount==null||amount.signum()<=0) throw new ValidationException("Payment amount must be greater than zero.");
        if(paymentDate==null||paymentDate.isAfter(LocalDate.now())) throw new ValidationException("Payment date must not be in the future.");
        if(paymentMethod==null||!paymentMethod.matches("CASH|CARD|BANK_TRANSFER|MOBILE_MONEY")) throw new ValidationException("Method must be CASH, CARD, BANK_TRANSFER or MOBILE_MONEY.");
        if(receiptNumber==null||receiptNumber.trim().isEmpty()) throw new ValidationException("Receipt number is required.");
    }
    public String toString(){return getRegistrationSummary();}
}
