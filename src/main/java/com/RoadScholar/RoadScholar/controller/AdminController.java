package com.RoadScholar.RoadScholar.controller;

import com.RoadScholar.RoadScholar.model.*;
import com.RoadScholar.RoadScholar.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private InstructorService instructorService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(HttpSession session){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        return "admin-dashboard";
    }

    @GetMapping("/verify-payments")
    public String showUnverifiedPayments(Model model, HttpSession session){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        if(admin==null){
            return "redirect:/login";
        }
        List<Payment> allPayments=paymentService.getAllPayments();
        List<Payment> unverifiedPayments=allPayments.stream()
                .filter(p -> !p.isPaymentVerified())
                .collect(Collectors.toList());
        model.addAttribute("payments", unverifiedPayments);
        return "admin-verify-payments";
    }

    @PostMapping("/verify-payments")
    public String verifyPayment(@RequestParam String paymentId, HttpSession session){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        if(admin==null){
            return "redirect:/login";
        }
        paymentService.verifyPayment(paymentId);

        Payment payment=paymentService.getAllPayments().stream()
                .filter(p -> p.getPaymentId().equals(paymentId))
                .findFirst()
                .orElse(null);

        if(payment !=null){
            Student student=studentService.getStudentById(payment.getStudentId());
            if(student!=null){
                student.setPaymentVerified(true);
                studentService.updateStudent(student);
            }
        }
        return "redirect:/admin/verify-payments";
    }

    @GetMapping("/appointments")
    public String viewAppointments(Model model, HttpSession session){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        if(admin==null){
            return "redirect:/login";
        }
        List<Appointment> appointments=appointmentService.getAllAppointmentsSortedByDate();
        model.addAttribute("appointments", appointments);
        return "admin-appointments";
    }

    @PostMapping("/appointments")
    public String completeAppointment(@RequestParam String appointmentId, HttpSession session){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        if(admin==null){
            return "redirect:/login";
        }
        appointmentService.markAppointmentCompletedById(appointmentId);
        return "redirect:/admin/appointments";
    }

    @GetMapping("/users")
    public String viewAllUsers(Model model, HttpSession session){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        if(admin==null){
            return "redirect:/login";
        }
        List<Student> students=studentService.getAllStudents();
        List<Instructor> instructors=instructorService.getAllInstructors();

        model.addAttribute("students", students);
        model.addAttribute("instructors", instructors);

        return "admin-users";
    }

    @GetMapping("/courses")
    public String showManageCoursePage(Model model, HttpSession session){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        if(admin==null){
            return "redirect:/login";
        }
        List<Course> courses=courseService.getAllCourses();
        model.addAttribute("courses", courses);
        model.addAttribute("newCourse", new Course());

        return "admin-courses";
    }

    @PostMapping("/courses/add")
    public String addCourse(@ModelAttribute("newCourse") Course course, HttpSession session){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        if(admin==null){
            return "redirect:/login";
        }
        course.setCourseId(courseService.generateNextCourseId());
        courseService.addCourse(course);
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/update")
    public String updateCourse(@ModelAttribute Course updatedCourse, HttpSession session){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        if(admin==null){
            return "redirect:/login";
        }
        courseService.updateCourse(updatedCourse);
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/delete")
    public String deleteCourse(@RequestParam String courseId, HttpSession session){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        if(admin==null){
            return "redirect:/login";
        }
        courseService.deleteCourse(courseId);
        return "redirect:/admin/courses";
    }

    @GetMapping("/reports")
    public String generateReport(Model model, HttpSession session){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        if(admin==null){
            return "redirect:/login";
        }
        List<Student> students=studentService.getAllStudents();
        List<Instructor> instructors=instructorService.getAllInstructors();
        List<Course> courses=courseService.getAllCourses();
        List<Appointment> appointments=appointmentService.getAllAppointments();
        List<Payment> payments=paymentService.getAllPayments();

        double totalCollected=payments.stream()
                .filter(Payment::isPaymentVerified)
                .mapToDouble(Payment::getAmount)
                .sum();

        model.addAttribute("totalStudents", students.size());
        model.addAttribute("totalInstructors", instructors.size());
        model.addAttribute("totalCourses", courses.size());
        model.addAttribute("totalAppointments", appointments.size());
        model.addAttribute("totalPayments",totalCollected);

        return "admin-report";
    }

    @GetMapping("/profile")
    public String showAdminProfile(HttpSession session, Model model){
        Admin admin=(Admin) session.getAttribute("loggedInUser");
        if(admin==null){
            return "redirect:/login";
        }
        model.addAttribute("admin",admin);
        return "admin-profile";
    }

}
