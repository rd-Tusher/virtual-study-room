package com.virtualstudyroom.backend.Controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.virtualstudyroom.backend.Service.CheckSessionService;

@RestController
@RequestMapping("/api/session")
public class CheckSession {


    @Autowired
    private CheckSessionService chkService;

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

    
}