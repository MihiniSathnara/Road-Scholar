package com.RoadScholar.RoadScholar.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class FileStorageHandler {
    private static final Logger logger=Logger.getLogger(FileStorageHandler.class.getName());
    private final ObjectMapper objectMapper=new ObjectMapper();

    public <T> List<T> readFromFile(String path, TypeReference<List<T>> typeRef){
        try{
            File file=new File(path);
            if(!file.exists()) return new ArrayList<>();
            return objectMapper.readValue(file, typeRef);
        }catch (IOException e){
            logger.log(Level.SEVERE,"Error reading from file: " +path, e);
            return new ArrayList<>();
        }
    }

    public <T> void writeToFile(String path, List<T> dataList){
        try{
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path),dataList);
        }catch (IOException e){
            logger.log(Level.SEVERE, "Error writing to file: " +path, e);
        }
    }

}
