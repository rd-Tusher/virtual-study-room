
package com.virtualStudyRoom.utils;

public class ResponseModel {

    public static class SessionResponse {
    
        public String joinCode;
    }

    public static class SessionCheckModel{
        public String status;
        public String startTime;
        public Long remainingSeconds;
    }
}