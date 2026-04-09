package com.virtualstudyroom.backend.Controller;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.virtualstudyroom.backend.Model.LateUserInfo;
import com.virtualstudyroom.backend.Model.ScrollMessage;
import com.virtualstudyroom.backend.Model.JoinedUserModel.CanvasHeightDTO;
import com.virtualstudyroom.backend.Model.JoinedUserModel.JoinReq;
// import com.virtualstudyroom.backend.Model.JoinedUserModel.StrokeDTO;
import com.virtualstudyroom.backend.Model.JoinedUserModel.User;
import com.virtualstudyroom.backend.Model.SessionModel.FileUploadNotification;
import com.virtualstudyroom.backend.Model.StrokeDTO;
import com.virtualstudyroom.backend.Service.CheckSessionService;
import com.virtualstudyroom.backend.Service.SessionEventService;
import com.virtualstudyroom.backend.Service.SessionTracker;

@RestController
@RequestMapping("/api/session")
public class CheckSession {

    @Autowired
    private CheckSessionService chkService;
    @Autowired 
    private SessionTracker sTracker;
    @Autowired
    private SessionEventService seService;

    private ConcurrentMap<String, LateUserInfo> lateUserInfo = new ConcurrentHashMap<>();

    @GetMapping("/{name}/{sessionID}")
    public CompletableFuture<ResponseEntity<?>> checkSession(@PathVariable String name,@PathVariable String sessionID) {
        System.out.println("trying to login");
        System.out.println(lateUserInfo.size());
        return chkService.checkSession(name,sessionID,lateUserInfo.get(sessionID))
                .handle((result, ex) -> {
                    if (ex != null) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                            .body(Map.of("status", "ERROR", "message", "Error occurred while getting info from DB"));
                    }
                    return ResponseEntity.ok(result);
                });
    }

    @Async
    @GetMapping("/{sessionID}/users")
    public CompletableFuture<Set<User>> getActiveUsers(@PathVariable String sessionID){
     Set<User> users = sTracker.getActiveUsers(sessionID);
     return CompletableFuture.completedFuture(users);
    }

    @PostMapping("/{sessionID}/join")
    public ResponseEntity<?> joinSession(@PathVariable String sessionID, @RequestBody JoinReq req) {
        seService.userJoined(sessionID, req.name,req.userID);
        sTracker.addUser(sessionID, req.getName(), req.getUserID());
        Set<User> users = sTracker.getActiveUsers(sessionID);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{sessionID}/leave")
    public ResponseEntity<?> leaveSession(@PathVariable String sessionID, @RequestBody JoinReq req) {
        seService.userLeft(sessionID, req.getName(),req.getUserID());
        sTracker.removeUser(sessionID, req.getUserID());
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/session/{sessionID}/whiteboard")
    public void broadcastStroke(@DestinationVariable String sessionID, StrokeDTO strokeDTO){
        System.out.println("entered into broadcasting storke");
        lateUserInfo.computeIfAbsent(sessionID, key -> LateUserInfo.builder().strokeDTO(new CopyOnWriteArrayList<>()).build()).getStrokeDTO().add(strokeDTO);
        seService.broadcastStroke(sessionID, strokeDTO);
    }
 
    @MessageMapping("/session/{sessionID}/whiteboard/resize")
    public void receiveCanvasResize(@DestinationVariable String sessionID, CanvasHeightDTO dto){
        System.out.println("entered into canvas height");
        lateUserInfo.compute(sessionID, (key, existing) -> {
            if (existing == null) {
                return LateUserInfo.builder().canvasHeight(dto.getCanvasHeight()).build();
            }
            if (dto.getCanvasHeight() > existing.getCanvasHeight()) {
                existing.setCanvasHeight(dto.getCanvasHeight());
            }
            return existing;
        });
 
        seService.broadcastHeight(sessionID, dto);
    }

    @PostMapping("/simulate/{sessionID}/{height}")
    public void getSim(@PathVariable String sessionID, @PathVariable int height){
        System.out.println(height);
        CanvasHeightDTO dto = new CanvasHeightDTO(sessionID, sessionID, height);
        seService.broadcastHeight(sessionID,dto);
    }

    @MessageMapping("/session/{sessionID}/remote-scroll")
    public void broadcastScrolling(@DestinationVariable String sessionID,ScrollMessage scroll ){
        System.out.println("percentage : " + scroll.getVerticalPercent());
        Double p = Double.parseDouble(scroll.getVerticalPercent());
        lateUserInfo.compute(sessionID, (key, existing) -> {
            if (existing == null) {
                return LateUserInfo.builder().verticalPercentage(p).build();
            }
            else {
                existing.setVerticalPercentage(p);
                return existing;
            }
        });
        seService.scrollCanvas(sessionID, scroll);
    }

    @PostMapping("/scroll/{sessionID}")
    public ResponseEntity<String> testScroll (@PathVariable String sessionID, @RequestBody ScrollMessage scroll){
        seService.scrollCanvas(sessionID, scroll);
        return ResponseEntity.ok().build(); 
    }
    
    @MessageMapping("/session/{sessionID}/webrtc")
    public void handleSignal(@DestinationVariable String sessionID, String message){
        System.out.println("entered into the backend.");
        seService.handleSingnal(sessionID, message);
    }

    @PostMapping("/{sessionID}/uploadFile")
    public CompletableFuture<ResponseEntity<String>> uploadFiles(
            @PathVariable String sessionID,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("userID") String userID) {

        return chkService.uploadFiles(files, sessionID)
                .thenApply(result -> {
                    seService.handleFileNotification(sessionID, new FileUploadNotification(sessionID, "Some one has uploaded some files",userID));
                    return ResponseEntity.ok(result);
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to upload files");
                });
    }

    
}