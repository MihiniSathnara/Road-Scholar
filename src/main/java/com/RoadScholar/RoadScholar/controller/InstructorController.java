package com.RoadScholar.RoadScholar.controller;

import com.RoadScholar.RoadScholar.model.*;
import com.RoadScholar.RoadScholar.service.AdminService;
import com.RoadScholar.RoadScholar.service.StudentService;
import com.RoadScholar.RoadScholar.service.PaymentService;
import com.RoadScholar.RoadScholar.service.CourseService;
import com.RoadScholar.RoadScholar.service.AppointmentService;
import com.RoadScholar.RoadScholar.service.InstructorService;
import com.RoadScholar.RoadScholar.util.AppointmentQueue;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/dashboard")
    public String showInstructorDashboard(HttpSession session){
        Instructor instructor=(Instructor) session.getAttribute("loggedInUser");
        if(instructor==null){
            return "redirect:/login";
        }
        return "instructor-dashboard";
    }

    @GetMapping("/schedule")
    public String viewSchedule(HttpSession session, Model model){
        Instructor instructor=(Instructor) session.getAttribute("loggedInUser");
        if(instructor==null){
            return "redirect:/login";
        }

        List<Appointment>  allAppointments=appointmentService.getAllAppointmentsSortedByDate();
        AppointmentQueue<Appointment> scheduleQueue=new AppointmentQueue<>();

        for(Appointment appointment: allAppointments){
            if(appointment.getInstructorId().equals(instructor.getInstructorId()) && !appointment.isCompleted()){
                scheduleQueue.enqueue(appointment);
            }
        }
        model.addAttribute("appointments", scheduleQueue.getAllElements());
        return "instructor-schedule";
    }

    @GetMapping("/students")
    public String viewStudents(HttpSession session, Model model) {
        Instructor instructor = (Instructor) session.getAttribute("loggedInUser");
        if (instructor == null) {
            return "redirect:/login";
        }

        List<Student> students = studentService.getStudentsByInstructorId(instructor.getInstructorId());

        List<Map<String, Object>> studentProgressList = students.stream().map(student -> {
            Course course = courseService.getCourseById(student.getEnrolledCourseId());
            int totalAppointments = course.getTotalAppointments();
            List<Appointment> completedAppointments = appointmentService.getAllAppointments().stream()
                    .filter(a -> a.getStudentId().equals(student.getStudentId())
                            && a.getCourseId().equals(course.getCourseId())
                            && a.isCompleted())
                    .collect(Collectors.toList());

            double progress = (double) completedAppointments.size() / totalAppointments * 100;

            Map<String, Object> studentMap = new HashMap<>();
            studentMap.put("firstName", student.getFirstName());
            studentMap.put("email", student.getEmail());
            studentMap.put("courseName", course.getName());
            studentMap.put("paymentVerified", student.isPaymentVerified() ? "Yes" : "No");
            studentMap.put("progress", progress);

            return studentMap;
        }).collect(Collectors.toList());

        model.addAttribute("students", studentProgressList);
        return "instructor-students";
    }

    @GetMapping("/profile")
    public String showInstructorProfile(HttpSession session, Model model){
        Instructor instructor=(Instructor) session.getAttribute("loggedInUser");
        if(instructor==null){
            return "redirect:/login";
        }
        model.addAttribute("instructor", instructor);
        return "instructor-profile";
    }
}
