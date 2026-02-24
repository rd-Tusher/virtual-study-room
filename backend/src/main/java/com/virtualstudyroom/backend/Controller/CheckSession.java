package com.virtualstudyroom.backend.Controller;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
import org.springframework.web.bind.annotation.RestController;

import com.virtualstudyroom.backend.Model.ScrollMessage;
import com.virtualstudyroom.backend.Model.JoinedUserModel.CanvasHeightDTO;
import com.virtualstudyroom.backend.Model.JoinedUserModel.JoinReq;
import com.virtualstudyroom.backend.Model.JoinedUserModel.StrokeDTO;
import com.virtualstudyroom.backend.Model.JoinedUserModel.User;
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

    @GetMapping("/{name}/{sessionID}")
    public CompletableFuture<ResponseEntity<?>> checkSession(@PathVariable String name,@PathVariable String sessionID) {
        return chkService.checkSession(name,sessionID)
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
        seService.broadcastStroke(sessionID, strokeDTO);
    }

    @MessageMapping("/session/{sessionID}/whiteboard/resize")
    public void receiveCanvasResize(@DestinationVariable String sessionID, CanvasHeightDTO dto){
        seService.broadcastHeight(sessionID, dto);
    }

    @PostMapping("/simulate/{sessionID}/{height}")
    public void getSim(@PathVariable String sessionID, @PathVariable int height){
        System.out.println(height);
        CanvasHeightDTO dto = new CanvasHeightDTO(sessionID, sessionID, height);
        seService.broadcastHeight(sessionID,dto);
    }

    @MessageMapping("/session/{sessionID}/canvas-scroll")
    public void broadcastScrolling(@DestinationVariable String sessionID,ScrollMessage scroll ){
        seService.scrollCanvas(sessionID, scroll);
    }

    @PostMapping("/scroll/{sessionID}")
    public ResponseEntity<String> testScroll (@PathVariable String sessionID, @RequestBody ScrollMessage scroll){
        seService.scrollCanvas(sessionID, scroll);
        return ResponseEntity.ok().build();
    }
}