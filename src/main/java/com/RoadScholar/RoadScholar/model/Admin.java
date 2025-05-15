package com.RoadScholar.RoadScholar.model;

public class Admin extends User{
    public Admin(){
        super();
    }
    public Admin(String firstName, String lastName, String email, String password, String role){
        super(firstName,lastName,email,password,role);
    }
}
