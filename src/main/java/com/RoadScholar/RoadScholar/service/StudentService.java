package com.RoadScholar.RoadScholar.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.RoadScholar.RoadScholar.model.Student;
import com.RoadScholar.RoadScholar.storage.FileStorageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final String FILE_PATH="data/students.json";
    private final FileStorageHandler fileStorageHandler;

    @Autowired
    public StudentService(FileStorageHandler fileStorageHandler){
        this.fileStorageHandler=fileStorageHandler;
    }

    public List<Student> getAllStudents(){
        return fileStorageHandler.readFromFile(FILE_PATH, new TypeReference<List<Student>>() {});
    }
    public void registerStudent(Student student){
        student.setStudentId(generateStudentId());
        List<Student> students=getAllStudents();
        students.add(student);
        fileStorageHandler.writeToFile(FILE_PATH, students);
    }

    public void updateStudent(Student updatedStudent){
        List<Student> students=getAllStudents();
        for(int i=0;i< students.size();i++){
            if(students.get(i).getStudentId().equals(updatedStudent.getStudentId())){
                students.set(i,updatedStudent);
                break;
            }
        }
        fileStorageHandler.writeToFile(FILE_PATH, students);
    }

    public String generateStudentId(){
        List<Student> students=getAllStudents();
        int nextIdNumber= students.size()+1;
        return String.format("S%03d", nextIdNumber);
    }

    public Student getStudentById(String studentId){
        List<Student> students=getAllStudents();
        for(Student student: students){
            if(student.getStudentId().equals(studentId)){
                return student;
            }
        }
        return null;
    }

    public List<Student> getStudentsByInstructorId(String instructorId){
        return getAllStudents().stream()
                .filter(student -> instructorId.equals(student.getSelectedInstructorId()))
                .collect(Collectors.toList());
    }
}
