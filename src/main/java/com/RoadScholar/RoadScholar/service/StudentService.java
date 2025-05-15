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
        List<Student> students=getAllStudents();
        students.add(student);
        fileStorageHandler.writeToFile(FILE_PATH, students);
    }
}
