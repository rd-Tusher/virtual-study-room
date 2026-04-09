package com.virtualstudyroom.backend.Model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "files")
public class FileDocuments {

    @Id
    private String id;
    private String sessionID;
    private List<String> fileName;
    private List<String> fileUrl;

    public FileDocuments(String sessionID, List<String> fileName, List<String> fileUrl){
        this.sessionID = sessionID;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }
    
    @Data
    public static class FileRes{
        private List<String> fileName;
        private List<String> fileUrl;
    }
}