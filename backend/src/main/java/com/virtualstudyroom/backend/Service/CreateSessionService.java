package com.virtualstudyroom.backend.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.virtualstudyroom.backend.Model.SessionModel;
import com.virtualstudyroom.backend.Model.SessionModel.Request;
import com.virtualstudyroom.backend.Model.SessionModel.Response;

@Service
public class CreateSessionService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Async
    public CompletableFuture<ResponseEntity<?>> createSession(Request req) {

        Instant startTime = req.getTime();
        Instant endTime = startTime.plusMillis(60*60*1000);

        SessionModel session = SessionModel.builder()
                .hostID(UUID.randomUUID().toString().split("-")[0])  
                .title(req.getTitle())
                .startTime(startTime)
                .endTime(endTime)
                .joinCode(UUID.randomUUID().toString())             
                .mode(req.getMode())
                .createdAt(Instant.now())
                .build();

        if (session == null) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save session info. Session not created"));
        }
        SessionModel savedSession = mongoTemplate.save(session);
            // System.out.println(UUID.randomUUID().toString());
        
        Response response = Response.builder()
        .joinCode(savedSession.getJoinCode())
        .build();
        return CompletableFuture.completedFuture(ResponseEntity.ok(response));
    }
}
