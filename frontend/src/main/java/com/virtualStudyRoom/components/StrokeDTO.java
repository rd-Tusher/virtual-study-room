package com.virtualStudyRoom.components;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class StrokeDTO {
    public String senderID;
    public float strokeSize;
    public int colorRGB;
    public List<PointDTO> points;

    public StrokeDTO() {}

    public StrokeDTO(Whiteboard.Stroke stroke) {
        this.strokeSize = stroke.strokeSize;
        this.colorRGB = stroke.color.getRGB();
        this.points = new ArrayList<>();
        for (Point p : stroke.points) {
            this.points.add(new PointDTO(p));
        }
    }

    public static class PointDTO {
        public int x;
        public int y;

        public PointDTO() {}

        public PointDTO(Point p) {
            this.x = p.x;
            this.y = p.y;
        }
    }
}