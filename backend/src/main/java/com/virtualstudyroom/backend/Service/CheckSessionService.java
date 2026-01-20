package com.virtualstudyroom.backend.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.virtualstudyroom.backend.Model.JoinedUserModel;
import com.virtualstudyroom.backend.Model.SessionModel;

@Service
public class CheckSessionService {

    @Autowired
    MongoTemplate mongoTemplate;

    public CompletableFuture<Map<String, Object>> checkSession(String name,String joinCode) {

        Query query = new Query();
        query.addCriteria(Criteria.where("joinCode").is(joinCode));

        SessionModel res = mongoTemplate.findOne(query, SessionModel.class, "sessionInfo");

        if (res == null) {
            return CompletableFuture.completedFuture(
                    Map.of("status", "NOT_FOUND", "message", "No session found with this join code")
            );
        }

        Instant now = Instant.now();
        Instant start = res.getStartTime();
        System.out.println(now);
        System.out.println(start);
        Instant end = res.getEndTime();

        if (now.isBefore(start) ) {
            long remainingSeconds = java.time.Duration.between(now, start).getSeconds();

            return CompletableFuture.completedFuture(
                    Map.of(
                            "status", "WAITING",
                            "startTime", start.toString(),
                            "remainingSeconds", remainingSeconds
                    )
            );
        }

        if (now.isAfter(end)) {
            return CompletableFuture.completedFuture(
                    Map.of(
                            "status", "ENDED",
                            "startTime", start.toString(),
                            "remainingSeconds", 0
                    )
            );
        }

        if(now.isAfter(start) && now.isBefore(end)){
            JoinedUserModel userModel = JoinedUserModel.builder()
                    .name(name).build();
                    if (userModel != null) {
                        mongoTemplate.save(userModel);
                    }
            return CompletableFuture.completedFuture(
                    Map.of(
                            "status", "LIVE",
                            "startTime", start.toString(),
                            "remainingSeconds", 0
                    )
            );
        }

        return CompletableFuture.completedFuture(Map.of("status","Not found any session"));
    }
}
