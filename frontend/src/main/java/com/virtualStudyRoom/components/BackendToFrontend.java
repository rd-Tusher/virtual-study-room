package com.virtualStudyRoom.components;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class BackendToFrontend {

    private static final String BASE_URL = "http://localhost:8080";
    private final Gson gson = new Gson();

    private final Map<String, List<User>> sessionUserCache = new HashMap<>();


    public void fetchActiveUsers(String sessionID, Runnable onUpdateUI) {
        CompletableFuture.runAsync(() -> {
            try {
                String endpoint = BASE_URL + "/session/" + sessionID + "/users";
                URL url = new URI(endpoint).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                if (connection.getResponseCode() == 200) {
                    List<User> users = gson.fromJson(
                            new InputStreamReader(connection.getInputStream()),
                            new TypeToken<List<User>>(){}.getType()
                    );

                    sessionUserCache.put(sessionID, users);

                    SwingUtilities.invokeLater(onUpdateUI);
                }

                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public List<User> getLocalUserList(String sessionID) {
        return sessionUserCache.getOrDefault(sessionID, Collections.emptyList());
    }


    public void userJoined(String sessionID, User user) {
        sessionUserCache.computeIfAbsent(sessionID, k -> new ArrayList<>()).add(user);
    }

    public void userLeft(String sessionID, String userID) {
        List<User> users = sessionUserCache.get(sessionID);
        if (users != null) {
            users.removeIf(u -> u.getUserID().equals(userID));
        }
    }

    public static class User {
    private String userID;
    private String name;

    public User() {} 

    public User(String userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userID != null && userID.equals(user.userID);
    }

    @Override
    public int hashCode() {
        return userID != null ? userID.hashCode() : 0;
    }
}

}
