package com.RoadScholar.RoadScholar.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.RoadScholar.RoadScholar.storage.FileStorageHandler;
import com.RoadScholar.RoadScholar.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    private final String FILE_PATH="data/admins.json";
    private final FileStorageHandler fileStorageHandler;

    @Autowired
    public AdminService(FileStorageHandler fileStorageHandler){
        this.fileStorageHandler=fileStorageHandler;
    }

    public List<Admin> getAllAdmins(){
        return fileStorageHandler.readFromFile(FILE_PATH, new TypeReference<List<Admin>>() {});
    }

    public void registerAdmin(Admin admin){
        List<Admin> admins=getAllAdmins();
        admins.add(admin);
        fileStorageHandler.writeToFile(FILE_PATH, admins);
    }
}
