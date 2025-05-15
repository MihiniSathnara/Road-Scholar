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
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam String role,
                        Model model,
                        HttpSession session){
        if(role.equalsIgnoreCase("student")){
            Student student=null;
            for (Student s: studentService.getAllStudents()){
                if(s.getEmail().equalsIgnoreCase(email) && s.getPassword().equals(password)){
                    student=s;
                    break;
                }
            }
            if(student!=null){
                session.setAttribute("loggedInUser", student);
                return "redirect:/student/home";
            }
        }else if(role.equalsIgnoreCase("instructor")){
            Instructor instructor=null;
            for(Instructor i: instructorService.getAllInstructors()){
                if(i.getEmail().equalsIgnoreCase(email) && i.getPassword().equals(password)){
                    instructor=i;
                    break;
                }
            }
            if(instructor!=null){
                session.setAttribute("loggedInUser", instructor);
                return "redirect:/instructor/home";
            }
        }else if(role.equalsIgnoreCase("admin")){
            Admin admin=null;
            for(Admin a : adminService.getAllAdmins()){
                if(a.getEmail().equalsIgnoreCase(email) && a.getPassword().equals(password)){
                    admin=a;
                    break;
                }
            }
            if(admin !=null){
                session.setAttribute("loggedInUser",admin);
                return "redirect:/admin/home";
            }
        }
        model.addAttribute("error", "Invalid credentials or role mismatch.");
        return "login";
    }

    @GetMapping("/student/home")
    public String StudentHome(){
        return "student-home";
    }

    @GetMapping("/instructor/home")
    public String instructorHome(){
        return "instructor-home";
    }

    @GetMapping("/admin/home")
    public String adminHome(){
        return "admin-home";
    }
}
