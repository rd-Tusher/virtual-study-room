package com.virtualstudyroom.backend.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.virtualstudyroom.backend.Model.FileDocuments;
import com.virtualstudyroom.backend.Model.JoinSessionResponse;
import com.virtualstudyroom.backend.Model.JoinedUserModel;
import com.virtualstudyroom.backend.Model.LateUserInfo;
import com.virtualstudyroom.backend.Model.SessionModel;

@Service
public class CheckSessionService {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    SessionTracker sTracker;
    @Autowired
    SessionEventService seService; 

    public CompletableFuture<JoinSessionResponse> checkSession(String name, String joinCode, LateUserInfo lateUserInfo) {

    Query query = new Query();
    query.addCriteria(Criteria.where("joinCode").is(joinCode));

    SessionModel session = mongoTemplate.findOne(query, SessionModel.class, "sessionInfo");

    if (session == null) {
        return CompletableFuture.completedFuture(
            JoinSessionResponse.builder()
                .status("NOT_FOUND")
                // .message("No session found with this join code")
                .build()
        );
    }

    Instant now = Instant.now();
    Instant start = session.getStartTime();
    Instant end = session.getEndTime();

    if (now.isBefore(start)) {
        long remainingSeconds = Duration.between(now, start).getSeconds();
        return CompletableFuture.completedFuture(
            JoinSessionResponse.builder()
                .status("WAITING")
                .startTime(start.toString())
                .remainingSeconds(remainingSeconds)
                .build()
        );
    }

    if (now.isAfter(end)) {
        return CompletableFuture.completedFuture(
            JoinSessionResponse.builder()
                .status("ENDED")
                .startTime(start.toString())
                .remainingSeconds(0)
                .build()
        );
    }

    // Session is LIVE
    String userID = UUID.randomUUID().toString().split("-")[0];

    JoinedUserModel userModel = JoinedUserModel.builder()
            .name(name)
            .userID(userID)
            .sessionID(joinCode)
            .joinedAT(now)
            .build();


    if (userModel != null) {
        mongoTemplate.save(userModel);
    }
    sTracker.addUser(joinCode, name, userID);

    if (lateUserInfo != null) {
        lateUserInfo.setUserID(userID);
    }

    if (lateUserInfo != null) {
        System.out.println("canvas height : " + lateUserInfo.getCanvasHeight());
    }
    return CompletableFuture.completedFuture(
        JoinSessionResponse.builder()
            .status("LIVE")
            .startTime(start.toString())
            .remainingSeconds(0)
            .sessionID(joinCode)
            .userID(userID)
            .name(name)
            .lateUserInfo(lateUserInfo) // can be null
            .build()
    );
}

    public CompletableFuture<String> uploadFiles(MultipartFile[] files, String sessionID){
        try {
            String UPLOAD_DIR = "uploads/";
            Path sessionPath = Paths.get(UPLOAD_DIR + sessionID);
            if (!Files.exists(sessionPath)) {
                Files.createDirectories(sessionPath);
            }

            String baseUrl = "http://localhost:8080/files/";
            List<String> fileName = new ArrayList<>();
            List<String> fileList = new ArrayList<>();

            for (MultipartFile file : files) {
                String originalFileName = file.getOriginalFilename();
                Path filePath = sessionPath.resolve(originalFileName);
                Files.write(filePath, file.getBytes());

                String fileUrl = baseUrl + sessionID + "/" + originalFileName;
                fileList.add(fileUrl);

                fileName.add(originalFileName);
            } 

            FileDocuments fileDocuments = new FileDocuments(sessionID, fileName, fileList); 
            
            Query query = new Query();
            query.addCriteria(Criteria.where("sessionID").is(sessionID));
            FileDocuments existingDocs = mongoTemplate.findOne(query, FileDocuments.class);
            
            if (existingDocs != null) {
                Update update = new Update();
                update.push("fileName").each(fileName);
                update.push("fileUrl").each(fileList);
                mongoTemplate.updateFirst(query, update, FileDocuments.class);
            }
            else {
                mongoTemplate.save(fileDocuments);
            }


            return CompletableFuture.completedFuture("Files uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            CompletableFuture<String> failed = new CompletableFuture<>();
            failed.completeExceptionally(e);
            return failed;
        }
    }
}