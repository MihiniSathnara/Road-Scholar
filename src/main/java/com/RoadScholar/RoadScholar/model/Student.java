package com.RoadScholar.RoadScholar.model;

public class Student extends User{
    private String stdId;

    public Student(){
        super();
    }

    public Student(String firstName, String lastName, String email, String password, String role){
        super(firstName, lastName, email, password, role);
    }

    public Student(String firstName, String lastName, String email, String password, String role, String stdId){
        super(firstName, lastName, email, password, role);
        this.stdId=stdId;
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }
}
