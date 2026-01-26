package com.virtualstudyroom.backend.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.virtualstudyroom.backend.Model.JoinedUserModel.User;

@Service
public class SessionTracker {

    // sessionID -> Set of userIDs
    private final Map<String, Set<User>> activeUsers = new ConcurrentHashMap<>();

    // user joins session
    public void addUser(String sessionID, String name,String userID) {

        activeUsers.computeIfAbsent(sessionID, k -> ConcurrentHashMap.newKeySet())
                   .add(new User(name, userID)); 
    }

    // user leaves session
    public void removeUser(String sessionID, String userID) {
        Set<User> users = activeUsers.get(sessionID);
        if (users != null) {
            users.removeIf(user -> user.getUserID().equals(userID));
            if (users.isEmpty()) {
                activeUsers.remove(sessionID);
            }
        }
    }   
 
    // get active users for a session
    public Set<User> getActiveUsers(String sessionID) {
        return activeUsers.getOrDefault(sessionID, Set.of());
    }

    // check if user is in session
    public boolean isUserActive(String sessionID, String userID) {
        Set<User> users = activeUsers.get(sessionID);
        if (users == null) {
            return false;
        }
        // return users.stre(sessionID, Set.of()).contains(userID);
        return users.stream().anyMatch(user -> user.getUserID().equals(userID));
    }
}
