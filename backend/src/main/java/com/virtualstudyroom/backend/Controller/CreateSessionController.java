package com.virtualstudyroom.backend.Controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.virtualstudyroom.backend.Model.SessionModel.Request;
import com.virtualstudyroom.backend.Service.CreateSessionService;

@RestController
@RequestMapping("/api")
public class CreateSessionController {

    @Autowired
    CreateSessionService sessionService;

    @PostMapping("/createSession")
    public CompletableFuture<ResponseEntity<?>> createSession(@RequestBody Request req) {
        return sessionService.createSession(req)
            .handle((result, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to save session data");
                }
                return ResponseEntity.ok(result);
            });
    }

}