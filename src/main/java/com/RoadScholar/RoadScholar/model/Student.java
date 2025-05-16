package com.RoadScholar.RoadScholar.model;

public class Student extends User{
    private String stdId;
    private String enrolledCourseId;
    private String selectedInstructorId;
    private boolean paymentVerified;


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

    public String getEnrolledCourseId() {
        return enrolledCourseId;
    }

    public void setEnrolledCourseId(String enrolledCourseId) {
        this.enrolledCourseId = enrolledCourseId;
    }

    public String getSelectedInstructorId() {
        return selectedInstructorId;
    }

    public void setSelectedInstructorId(String selectedInstructorId) {
        this.selectedInstructorId = selectedInstructorId;
    }

    public boolean isPaymentVerified() {
        return paymentVerified;
    }

    public void setPaymentVerified(boolean paymentVerified) {
        this.paymentVerified = paymentVerified;
    }
}
