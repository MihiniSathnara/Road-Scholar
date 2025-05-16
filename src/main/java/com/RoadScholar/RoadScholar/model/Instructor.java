package com.RoadScholar.RoadScholar.model;

import java.time.LocalDate;

public class Instructor extends User{
    private String instructorId;
    private int experienceYears;

    public Instructor(){
        super();
        this.experienceYears=0;
        this.instructorId="null";
    }

    public Instructor(String firstName, String lastName, String email, String password, String role, int experienceYears){
        super(firstName, lastName, email, password, role);
        this.experienceYears=experienceYears;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }
}
