package com.RoadScholar.RoadScholar.controller;

import com.RoadScholar.RoadScholar.model.Student;
import com.RoadScholar.RoadScholar.service.StudentService;
import com.RoadScholar.RoadScholar.model.Course;
import com.RoadScholar.RoadScholar.service.CourseService;
import com.RoadScholar.RoadScholar.model.Instructor;
import com.RoadScholar.RoadScholar.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    @GetMapping("/enroll/course")
    public String showCourseSelection(Model model, HttpSession session){
        Student student=(Student) session.getAttribute("loggedInUser");

        if(student.getEnrolledCourseId()!=null && student.isPaymentVerified() && student.getSelectedInstructorId()!=null ){
            return "redirect:/dashboard";
        }
        List<Course> courses=courseService.getAllCourses();
        model.addAttribute("courses",courses);
        return "enroll-course";
    }

    @PostMapping("/enroll/course")
    public String selectCourse(@RequestParam String courseId, HttpSession session){
        Student student=(Student) session.getAttribute("loggedInUser");
        student.setEnrolledCourseId(courseId);
        studentService.updateStudent(student);
        session.setAttribute("loggedInUser", student);
        return "redirect:/student/enroll/instructor";
    }

    @GetMapping("/enroll/instructor")
    public String showInstructorSelection(Model model,HttpSession session){
        Student student=(Student) session.getAttribute("loggedInUser");

        if(student==null || student.getEnrolledCourseId()==null){
            return "redirect:/student/enroll/course";
        }
        List<Instructor> instructors=instructorService.getAllInstructors();
        instructors=instructorService.sortInstructorByExperience(instructors);
        model.addAttribute("instructors", instructors);

        return "enroll-instructor";
    }

    @PostMapping("/enroll/instructor")
    public String selectInstructor(@RequestParam String instructorId, HttpSession session){
        Student student=(Student) session.getAttribute("loggedInUser");
        student.setSelectedInstructorId(instructorId);
        studentService.updateStudent(student);
        session.setAttribute("loggedInUser", student);

        return "redirect:/student/enroll/payment";
    }
}
