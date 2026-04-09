package com.virtualstudyroom.backend.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data  
@Builder  
@NoArgsConstructor  
@AllArgsConstructor  
public class JoinSessionResponse {  
    private String status;  
    private String startTime;  
    private long remainingSeconds;  
    private String sessionID;  
    private String userID;  
    private String name;  
    private LateUserInfo lateUserInfo;
}