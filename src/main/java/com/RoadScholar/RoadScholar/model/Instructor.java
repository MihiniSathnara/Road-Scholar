package com.RoadScholar.RoadScholar.model;

import java.time.LocalDate;

public class Instructor extends User{
    private int experienceYears;

    public Instructor(){
        super();
        this.experienceYears=0;
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
}
