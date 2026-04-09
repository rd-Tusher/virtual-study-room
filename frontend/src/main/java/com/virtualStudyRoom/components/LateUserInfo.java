package com.virtualStudyRoom.components;

import java.util.List;

public class LateUserInfo {

    private List<StrokeDTO> strokeDTO;
    private int canvasHeight;
    private double verticalPercentage;
    private String userID;

    public List<StrokeDTO> getStrokeDTO() { return strokeDTO; }
    public void setStrokeDTO(List<StrokeDTO> strokeDTO) { this.strokeDTO = strokeDTO; }

    public int getCanvasHeight() { return canvasHeight; }
    public void setCanvasHeight(int canvasHeight) { this.canvasHeight = canvasHeight; }

    public double getVerticalPercentage() { return verticalPercentage; }
    public void setVerticalPercentage(double verticalPercentage) { this.verticalPercentage = verticalPercentage; }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
}