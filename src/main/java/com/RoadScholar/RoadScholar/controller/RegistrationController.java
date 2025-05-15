package com.RoadScholar.RoadScholar.controller;

import com.RoadScholar.RoadScholar.model.User;
import com.RoadScholar.RoadScholar.model.Student;
import com.RoadScholar.RoadScholar.model.Instructor;
import com.RoadScholar.RoadScholar.model.Admin;
import com.RoadScholar.RoadScholar.service.InstructorService;
import com.RoadScholar.RoadScholar.service.StudentService;
import com.RoadScholar.RoadScholar.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
public class RegistrationController {

    @GetMapping("/register-role-selection")
    public String showRoleSelection(){
        return "register-role-selection";
    }

    @PostMapping("/register-role-selection")
    public String handleRoleSelection(@RequestParam String role){
        switch(role){
            case "student":
                return "redirect:/register/student";
            case "instructor":
                return "redirect:/register/instructor";
            case "admin":
                return "redirect:/register/admin";
            default:
                return "redirect:/register-role-selection?error";
        }
    }

    @GetMapping("/register/student")
    public String showStudentForm(){
        return "register-student";
    }

    @GetMapping("/register/instructor")
    public String showInstructorForm(){
        return "register-instructor";
    }

    @GetMapping("/register/admin")
    public String showAdminForm(){
        return "register-admin";
    }

    @Autowired
    private StudentService studentService;

    @PostMapping("/register/student")
    public String registerStudent(@RequestParam Map<String, String> params){
        Student student=new Student();
        student.setFirstName(params.get("firstname"));
        student.setLastName(params.get("lastname"));
        student.setEmail(params.get("email"));
        student.setPassword(params.get("password"));
        student.setRole("Student");
        studentService.registerStudent(student);
        return "registration-success";
    }

    @Autowired
    private InstructorService instructorService;

    @PostMapping("/register/instructor")
    public String registerInstructor(@RequestParam Map<String, String> params){
        Instructor instructor=new Instructor();
        instructor.setFirstName(params.get("firstname"));
        instructor.setLastName(params.get("lastname"));
        instructor.setEmail(params.get("email"));
        instructor.setPassword(params.get("password"));
        instructor.setRole("instructor");
        instructor.setExperienceYears(Integer.parseInt(params.get("experienceyears")));
        instructorService.registerInstructor(instructor);
        return "registration-success";
    }

    @Autowired
    private AdminService adminService;

    @PostMapping("/register/admin")
    public String registerAdmin(@RequestParam Map<String, String> params){
        Admin admin=new Admin();
        admin.setFirstName(params.get("firstname"));
        admin.setLastName(params.get("lastname"));
        admin.setEmail(params.get("email"));
        admin.setPassword(params.get("password"));
        admin.setRole("admin");
        adminService.registerAdmin(admin);
        return "registration-success";
    }
}
