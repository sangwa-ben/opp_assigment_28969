package com.auca.classroom;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
public class Classroom implements Registrable {
    private String classroomId, buildingName, roomNumber;
    private int capacity, floorNumber;
    private boolean available;
    public Classroom(String id,String building,String room,int cap,int floor,boolean on){classroomId=id;buildingName=building;roomNumber=room;capacity=cap;floorNumber=floor;available=on;}
    public String getClassroomId(){return classroomId;} public void setClassroomId(String v){classroomId=v;}
    public String getBuildingName(){return buildingName;} public void setBuildingName(String v){buildingName=v;}
    public String getRoomNumber(){return roomNumber;} public void setRoomNumber(String v){roomNumber=v;}
    public int getCapacity(){return capacity;} public void setCapacity(int v){capacity=v;}
    public int getFloorNumber(){return floorNumber;} public void setFloorNumber(int v){floorNumber=v;}
    public boolean isAvailable(){return available;} public void setAvailable(boolean v){available=v;}
    public String getId(){return classroomId;} public String getDisplayName(){return buildingName+" "+roomNumber;} public boolean isActive(){return available;}
    public String getRegistrationSummary(){return "Classroom "+classroomId+": "+buildingName+" room "+roomNumber+", capacity "+capacity+", floor "+floorNumber+", "+(available?"available":"unavailable");}
    public void printDetails(){System.out.println(getRegistrationSummary());}
    public void validate() throws ValidationException {
        if(classroomId==null||classroomId.trim().length()<4||buildingName==null||buildingName.trim().length()<2||roomNumber==null||roomNumber.trim().isEmpty()) throw new ValidationException("Classroom ID, building name (2+ chars), and room number are required.");
        if(capacity<1) throw new ValidationException("Classroom capacity must be greater than zero.");
        if(floorNumber<0) throw new ValidationException("Floor number cannot be negative.");
    }
    public String toString(){return getRegistrationSummary();}
}
