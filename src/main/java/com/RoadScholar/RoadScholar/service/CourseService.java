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
}
