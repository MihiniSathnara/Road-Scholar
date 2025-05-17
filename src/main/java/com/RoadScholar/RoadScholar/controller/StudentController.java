package com.RoadScholar.RoadScholar.controller;

import com.RoadScholar.RoadScholar.model.Student;
import com.RoadScholar.RoadScholar.service.*;
import com.RoadScholar.RoadScholar.model.Payment;
import com.RoadScholar.RoadScholar.model.Course;
import com.RoadScholar.RoadScholar.model.Instructor;
import com.RoadScholar.RoadScholar.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private AppointmentService appointmentService;

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

    @GetMapping("/schedule")
    public String showSchedulePage(Model model, HttpSession session){
        Student student=(Student) session.getAttribute("loggedInUser");
        if(appointmentService.hasUpcomingAppointment(student.getStudentId())){
            model.addAttribute("message", "You already have a scheduled appointment. Complete it to book a new one.");
            return "student-schedule";
        }

        List<String> timeSlots= Arrays.asList("09:00 - 11:00", "11:30 - 13:30", "14:00 - 16:00");
        model.addAttribute("timeSlots", timeSlots);
        return "student-schedule";
    }

    @PostMapping("/schedule")
    public String bookAppointment(@RequestParam String date, @RequestParam String timeSlot, HttpSession session,Model model) {
        Student student=(Student) session.getAttribute("loggedInUser");

        List<String> bookedSlots=appointmentService.getBookedTimeSlots(date);
        if (bookedSlots.contains(timeSlot)){
            model.addAttribute("message", "This timeslot is already booked.");
            return "student-schedule";
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(UUID.randomUUID().toString());
        appointment.setStudentId(student.getStudentId());
        appointment.setInstructorId(student.getSelectedInstructorId());
        appointment.setCourseId(student.getEnrolledCourseId());
        appointment.setDate(date);
        appointment.setTimeSlot(timeSlot);
        appointment.setCompleted(false);

        appointmentService.addAppointment(appointment);
        return "redirect:/student/dashboard";
    }

    @GetMapping("/progress")
    public String viewProgress(HttpSession session, Model model){
        Student student=(Student) session.getAttribute("loggedInUser");
        if(student==null){
            return "redirect:/login";
        }
        String studentId=student.getStudentId();
        String courseId=student.getEnrolledCourseId();

        Course course=courseService.getCourseById(courseId);
        int totalAppointments=course.getTotalAppointments();

        List<Appointment> appointments=appointmentService.getAllAppointments().stream()
                .filter(a -> a.getStudentId().equals(studentId)
                && a.getCourseId().equals(courseId)
                && a.isCompleted())
                .collect(Collectors.toList());

        int completedAppointments=appointments.size();

        double progressPercent=(double) completedAppointments/totalAppointments*100;

        model.addAttribute("progressPercent", progressPercent);
        model.addAttribute("completedAppointments", completedAppointments);
        model.addAttribute("totalAppointments", totalAppointments);
        model.addAttribute("courseName", course.getName());

        return "student-progress";
    }


}
