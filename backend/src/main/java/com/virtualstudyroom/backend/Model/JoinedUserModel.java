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
    public String senderID;
    public String strokeID;
    public float strokeSize;
    public int colorRGB;
    public List<PointDTO> points;
    public StrokeType type;

        public StrokeDTO(int colorRGB, float strokeSize, List<PointDTO> points){
            this.colorRGB = colorRGB;
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



    @Data
    public static class PointDTO {
        public int x;
        public int y;

        public PointDTO() {}

        public PointDTO(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    
    public static enum StrokeType {
        START,
        DRAW,
        END
    }
}