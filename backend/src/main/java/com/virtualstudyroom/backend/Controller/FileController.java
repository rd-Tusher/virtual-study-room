package com.virtualstudyroom.backend.Controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.virtualstudyroom.backend.Model.FileDocuments;


@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    MongoTemplate mongoTemplate;
    @GetMapping("/{sessionID}/{fileName}") 
    public ResponseEntity<?> streamFile(@PathVariable String sessionID, @PathVariable String fileName) throws IOException {
        Path filePath = Paths.get("uploads/" + sessionID + "/" + fileName).normalize();
        Resource resource = new UrlResource( filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.status(404).body("File not found!");
        }

        String contentDisposition = "inline";
        String contentType = "application/octet-stream";

        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
            contentType = "image/jpeg";
        } else if (fileName.endsWith(".pdf")) {
            contentType = "application/pdf";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition + "; filename=\"" + fileName + "\"")
                .contentType(MediaType.valueOf(contentType))
                .body(resource);
    }
 
    @GetMapping("/get/{sessionID}")
    public ResponseEntity<?> getFile(@PathVariable String sessionID){
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("sessionID").is(sessionID));
            FileDocuments files = mongoTemplate.findOne(query, FileDocuments.class);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 