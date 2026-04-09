package com.virtualstudyroom.backend.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.virtualstudyroom.backend.Model.StrokeDTO;
import com.virtualstudyroom.backend.Model.LateUserInfo;
import com.virtualstudyroom.backend.Model.ScrollMessage;
import com.virtualstudyroom.backend.Model.JoinedUserModel.CanvasHeightDTO;
import com.virtualstudyroom.backend.Model.SessionModel.FileUploadNotification;


@Service
public class SessionEventService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void userJoined(String sessionID, String name,String userID) {
        Map<String, Object> payload = Map.of( "event", "USER_JOINED", "name", name,"userID",userID);

        if (payload != null) {
            messagingTemplate.convertAndSend("/topic/session/" + sessionID, payload);
        }
    }

    public void userLeft(String sessionID, String name,String userID) {
        Map<String, Object> payload = Map.of( "event", "USER_LEFT", "name", name,"userID",userID);

        if(payload != null){
            messagingTemplate.convertAndSend("/topic/session/" + sessionID,payload); 
        }
    }

    public void broadcastStroke(String sessionID, StrokeDTO stroke){
        if (stroke != null) {
            messagingTemplate.convertAndSend("/topic/session/" + sessionID + "/whiteboard",stroke);
        }
    }

    public void broadcastHeight(String sessionID, CanvasHeightDTO dto){
        if(dto != null){
            messagingTemplate.convertAndSend("/topic/session/" + sessionID + "/whiteboard/resize",dto);
        }
    }

    public void scrollCanvas(String sessionID, ScrollMessage scroll){
        if (scroll != null) {
            messagingTemplate.convertAndSend("/topic/session/" + sessionID + "/auto-scroll",scroll);
        }
    }
    
    public void handleSingnal(String sessionID, String message){
        if(message != null){
            System.out.println("entered into handleSignal");
            messagingTemplate.convertAndSend("/topic/session/"+ sessionID + "/webrtc",message);
        }
    }

    public void handleFileNotification(String sessionID, FileUploadNotification notification){
        if (notification != null) {
            System.out.println(notification.getMessage());
            messagingTemplate.convertAndSend("/topic/session/"+ sessionID + "/notification",notification);
        }
    }

    public void SendToLateUser(LateUserInfo info,String sessionID){
        if (info !=  null) {
            System.out.println("entered to send late user data");
            System.out.println("user id " + info.getUserID());
            System.out.println("canvas height : " + info.getCanvasHeight());
            messagingTemplate.convertAndSend("/topic/session/" + sessionID + "/info-to-late-user",info );
        }
    }
}