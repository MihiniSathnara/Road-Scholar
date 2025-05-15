package com.RoadScholar.RoadScholar.service;

import com.RoadScholar.RoadScholar.model.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.RoadScholar.RoadScholar.model.Instructor;
import com.RoadScholar.RoadScholar.storage.FileStorageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorService {
    private final String FILE_PATH="data/instructors.json";
    private final FileStorageHandler fileStorageHandler;

    @Autowired
    public InstructorService(FileStorageHandler fileStorageHandler){
        this.fileStorageHandler=fileStorageHandler;
    }


    public List<Instructor> getAllInstructors(){
        return fileStorageHandler.readFromFile(FILE_PATH, new TypeReference<List<Instructor>>() {});
    }

    public void registerInstructor(Instructor instructor){
        List<Instructor> instructors=getAllInstructors();
        instructors.add(instructor);
        fileStorageHandler.writeToFile(FILE_PATH, instructors);
    }
}
