package com.virtualstudyroom.backend.Model;

import java.time.Instant;

// package com.virtualstudyroom.backend.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(value = "user_list")
public class JoinedUserModel {

    @Id
    private String id;
    private String name;
    private String sessionID;
    private Instant joinedAT;
} 