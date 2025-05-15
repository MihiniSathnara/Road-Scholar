package com.RoadScholar.RoadScholar.model;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String role;

    public User(){
        this.firstName="null";
        this.lastName="null";
        this.email="null";
        this.password="null";
        this.role="null";
    }

    public User(String firstName, String lastName, String email, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role=role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}