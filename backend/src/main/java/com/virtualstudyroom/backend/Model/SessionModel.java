package com.virtualstudyroom.backend.Model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(value = "sessionInfo")
public class SessionModel{

    @Id 
    String id;

    private String hostID;
    private String title;
    private String date;

    private Instant startTime;
    private Instant endTime;

    private String joinCode;
    // private String status;
    private String mode;

    private Instant createdAt;

    @Data
    public static class Request {
        private String title;
        private String date;
        private Instant time;
        private String mode;
    }

    @Data
    @Builder
    public static class Response{
        // private String sessionID;
        private String joinCode;
        // private String QRCode;
    }
}