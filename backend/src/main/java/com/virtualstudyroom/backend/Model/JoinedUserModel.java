package com.virtualstudyroom.backend.Model;

import java.time.Instant;

// package com.virtualstudyroom.backend.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(value = "user_list")
public class JoinedUserModel {

    @Id
    private String id;
    private String name;
    private String userID;
    private String sessionID;
    private Instant joinedAT;



    @Data
    public static class JoinReq {
        public String name;
        public String userID;
    }

    @Data
    public static class User {
        private String name;
        private String userID;

        public User(String name, String userID){
            this.name = name;
            this.userID = userID;
        }
    }

    @Data 
    public static class CanvasHeightDTO{
        private String senderID;
        private String sessionID;
        private int canvasHeight;

        public CanvasHeightDTO(String senderID,String sessionID, int canvasHeight){
            this.senderID = senderID;
            this.sessionID = sessionID;
            this.canvasHeight = canvasHeight;
        }
    }
}
