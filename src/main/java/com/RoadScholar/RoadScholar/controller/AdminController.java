package com.RoadScholar.RoadScholar.controller;

import com.RoadScholar.RoadScholar.model.Admin;
import com.RoadScholar.RoadScholar.service.AdminService;
import com.RoadScholar.RoadScholar.model.Student;
import com.RoadScholar.RoadScholar.service.StudentService;
import com.RoadScholar.RoadScholar.model.Payment;
import com.RoadScholar.RoadScholar.service.PaymentService;
import com.RoadScholar.RoadScholar.model.Course;
import com.RoadScholar.RoadScholar.service.CourseService;
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

    @GetMapping("/dashboard")
    public String showAdminDashboard(){
        return "admin-dashboard";
    }

    @GetMapping("/verify-payments")
    public String showUnverifiedPayments(Model model){
        List<Payment> allPayments=paymentService.getAllPayments();
        List<Payment> unverifiedPayments=allPayments.stream()
                .filter(p -> !p.isPaymentVerified())
                .collect(Collectors.toList());
        model.addAttribute("payments", unverifiedPayments);
        return "admin-verify-payments";
    }

    @PostMapping("/verify-payments")
    public String verifyPayment(@RequestParam String paymentId){
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
}
