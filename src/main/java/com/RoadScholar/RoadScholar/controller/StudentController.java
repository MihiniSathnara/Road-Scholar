package com.RoadScholar.RoadScholar.controller;

import com.RoadScholar.RoadScholar.model.Student;
import com.RoadScholar.RoadScholar.service.StudentService;
import com.RoadScholar.RoadScholar.model.Payment;
import com.RoadScholar.RoadScholar.service.PaymentService;
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
import java.util.UUID;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/enroll/course")
    public String showCourseSelection(Model model, HttpSession session){
        Student student=(Student) session.getAttribute("loggedInUser");

        if(student.getEnrolledCourseId()!=null && student.isPaymentVerified() && student.getSelectedInstructorId()!=null ){
            return "redirect:/student/dashboard";
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

    @GetMapping("/enroll/payment")
    String showPaymentForm(Model model, HttpSession session) {
        Student student = (Student) session.getAttribute("loggedInUser");
        if (student == null || student.getEnrolledCourseId() == null || student.getSelectedInstructorId() == null){
            return "redirect:/student/enroll/course";
        }
        Course selectedCourse=courseService.getCourseById(student.getEnrolledCourseId());
        model.addAttribute("course", selectedCourse);
        return "enroll-payment";
    }

    @PostMapping("/enroll/payment")
    public String processPayment(HttpSession session){
        Student student=(Student) session.getAttribute("loggedInUser");
        if(student==null){
            return "redirect:/login";
        }

        Course course=courseService.getCourseById(student.getEnrolledCourseId());

        Payment payment=new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setStudentId(student.getStudentId());
        payment.setCourseId(course.getCourseId());
        payment.setAmount(course.getPrice());
        payment.setPaymentVerified(false);

        paymentService.addPayment(payment);

        return "redirect:/student/waiting";
    }

    @GetMapping("/waiting")
    public String waitingForApproval(){
        return "waiting-for-verification";
    }

    @GetMapping("/dashboard")
    public String showStudentDashboard(Model model, HttpSession session){
        Student student=(Student) session.getAttribute("loggedInUser");
        if(student==null){
            return "redirect:/login";
        }
        model.addAttribute("student", student);
        return "student-dashboard";
    }


}
