
package com.virtualStudyRoom.utils;

import java.util.List;

import com.virtualStudyRoom.components.LateUserInfo;

public class ResponseModel {

    public static class SessionResponse {
    
        public String joinCode;
    }

    public static class SessionCheckModel{
        public String status;
        public String startTime;
        public Long remainingSeconds;
        public String sessionID;
        public String userID;
        public String name;
    }


    public static class FileUploadNotification {
        public String userID;
        public String message;
    }

    public static class FileRes {
        public String id;
        public String sessionID;
        public List<String> fileName;
        public List<String> fileUrl;

    }

    public static class JoinSessionResponse {  
        public String status;  
        public String startTime;  
        public long remainingSeconds;  
        public String sessionID;  
        public String userID;  
        public String name;  
        public LateUserInfo lateUserInfo;
    }
} 