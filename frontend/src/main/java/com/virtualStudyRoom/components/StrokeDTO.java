package com.virtualStudyRoom.components;

import java.util.List;

import lombok.Data;

@Data
public class StrokeDTO {

    public String senderID;
    public String strokeID;
    public float strokeSize;
    public int colorRGB;
    public List<PointDTO> points;
    public StrokeType type;

    public StrokeDTO() {}

    public StrokeDTO(String strokeID, float strokeSize, int colorRGB, List<PointDTO> points, StrokeType type) {
        // this.senderID = senderID;
        this.strokeID = strokeID;
        this.strokeSize = strokeSize;
        this.colorRGB = colorRGB;
        this.points = points;
        this.type = type;
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