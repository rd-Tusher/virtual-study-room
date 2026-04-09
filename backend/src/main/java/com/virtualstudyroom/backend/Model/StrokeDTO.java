package com.virtualstudyroom.backend.Model;

import java.util.List;

import lombok.Data;

@Data
public class StrokeDTO {

    private String senderID;
    private float strokeSize;
    private int  colorRGB;
    private List<Point> points;

    public StrokeDTO(){}
    
    public StrokeDTO(int colorRGB, float strokeSize, List<Point> points){
        this.colorRGB = colorRGB;
        this.points = points;
        this.strokeSize = strokeSize;
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
}