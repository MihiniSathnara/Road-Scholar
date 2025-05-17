package com.RoadScholar.RoadScholar.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.RoadScholar.RoadScholar.model.Course;
import com.RoadScholar.RoadScholar.storage.FileStorageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private final String FILE_PATH="data/courses.json";
    private final FileStorageHandler fileStorageHandler;

    @Autowired
    public CourseService(FileStorageHandler fileStorageHandler){
        this.fileStorageHandler=fileStorageHandler;
    }

    public List<Course> getAllCourses(){
        return fileStorageHandler.readFromFile(FILE_PATH, new TypeReference<List<Course>>() {});

    }

    public void addCourse(Course course){
        List<Course> courses=getAllCourses();
        courses.add(course);
        fileStorageHandler.writeToFile(FILE_PATH, courses);
    }

    public Course getCourseById(String courseId){
        return  getAllCourses().stream()
                .filter(c -> c.getCourseId().equals(courseId))
                .findFirst()
                .orElse(null);
    }

    public void deleteCourse(String courseId){
        List<Course> courses=getAllCourses();
        courses.removeIf(c -> c.getCourseId().equals(courseId));
        fileStorageHandler.writeToFile(FILE_PATH, courses);
    }

    public void updateCourse(Course updatedCourse){
        List<Course> courses=getAllCourses();
        for(int i=0;i<courses.size(); i++){
            if(courses.get(i).getCourseId().equals(updatedCourse.getCourseId())){
                courses.set(i, updatedCourse);
                break;
            }
        }
        fileStorageHandler.writeToFile(FILE_PATH, courses);
    }

    public String generateNextCourseId(){
        List<Course> courses=getAllCourses();
        int maxId=0;

        for(Course c: courses){
            String id=c.getCourseId();
            try{
                int num=Integer.parseInt(id.substring(1));
                if(num>maxId){
                    maxId=num;
                }
            }catch (NumberFormatException ignored){
            }
        }
        return String.format("C%03d", maxId+1);
    }
}
