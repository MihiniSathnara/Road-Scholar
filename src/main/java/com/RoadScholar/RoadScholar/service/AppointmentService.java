package com.RoadScholar.RoadScholar.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.RoadScholar.RoadScholar.model.Appointment;
import com.RoadScholar.RoadScholar.storage.FileStorageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final String FILE_PATH="data/appointments.json";
    private final FileStorageHandler fileStorageHandler;

    public AppointmentService(FileStorageHandler fileStorageHandler){
        this.fileStorageHandler=fileStorageHandler;
    }

    public List<Appointment> getAllAppointments(){
        return fileStorageHandler.readFromFile(FILE_PATH, new TypeReference<List<Appointment>>() {});

    }

    public void saveAppointments(List<Appointment> appointments){
        fileStorageHandler.writeToFile(FILE_PATH, appointments);
    }

    public void addAppointment(Appointment appointment){
        List<Appointment> appointments=getAllAppointments();
        appointments.add(appointment);
        saveAppointments(appointments);
    }

    public List<String> getBookedTimeSlots(String date){
        return getAllAppointments().stream()
                .filter(a -> a.getDate().equals(date))
                .map(Appointment::getTimeSlot)
                .collect(Collectors.toList());
    }

    public boolean hasUpcomingAppointment(String studentId){
        return getAllAppointments().stream()
                .anyMatch(a -> a.getStudentId().equals(studentId) && !a.isCompleted());
    }

    public void markAppointmentCompleted(String studentId){
        List<Appointment> appointments=getAllAppointments();
        for(Appointment a: appointments){
            if(a.getStudentId().equals(studentId) && !a.isCompleted()){
                a.setCompleted(true);
                break;
            }
        }
        saveAppointments(appointments);
    }

    public List<Appointment> getAllAppointmentsSortedByDate(){
        return getAllAppointments().stream()
                .sorted(Comparator.comparing(Appointment::getDate))
                .collect(Collectors.toList());
    }

    public void markAppointmentCompletedById(String appointmentId){
        List<Appointment> appointments=getAllAppointments();
        for(Appointment appointment: appointments){
            if(appointment.getAppointmentId().equals(appointmentId)){
                appointment.setCompleted(true);
                break;
            }
        }
        saveAppointments(appointments);
    }
}
