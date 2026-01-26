package com.virtualstudyroom.backend.Model;

import java.time.Instant;
import java.util.List;

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
    public static class Point {
        private int x;
        private int y;

        public Point(){}
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }


    @Data 
    public static class StrokeDTO{
        private String senderID;
        private String color;
        private float strokeSize;
        private List<Point> points;

        public StrokeDTO(String color, float strokeSize, List<Point> points){
            this.color = color;
            this.points = points;
            this.strokeSize = strokeSize;
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
