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

import java.util.List;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/dashboard")
    public String showInstructorDashboard(){
        return "instructor-dashboard";
    }

    @GetMapping("/schedule")
    public String viewSchedule(HttpSession session, Model model){
        Instructor instructor=(Instructor) session.getAttribute("loggedInUser");

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
}
