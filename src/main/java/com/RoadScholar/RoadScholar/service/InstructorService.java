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
        instructor.setInstructorId(generateInstructorId());
        List<Instructor> instructors=getAllInstructors();
        instructors.add(instructor);
        fileStorageHandler.writeToFile(FILE_PATH, instructors);
    }
    public List<Instructor> sortInstructorByExperience(List<Instructor> instructors){
        int n=instructors.size();
        for(int i=0;i<n-1;i++){
            for(int j=0;j<n-i-1;j++){
                if(instructors.get(j).getExperienceYears()<instructors.get(j+1).getExperienceYears()){
                    Instructor temp=instructors.get(j);
                    instructors.set(j, instructors.get(j+1));
                    instructors.set(j+1, temp);
                }
            }
        }
        return instructors;
    }

    public String generateInstructorId(){
        List<Instructor> instructors=getAllInstructors();
        int nextIdNumber=instructors.size()+1;
        return String.format("I%03d",nextIdNumber);
    }
}
