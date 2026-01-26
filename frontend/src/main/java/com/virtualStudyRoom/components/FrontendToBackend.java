package com.virtualStudyRoom.components;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.virtualStudyRoom.components.BackendToFrontend.User;


public class FrontendToBackend {

    private static String baseUrl = "http://localhost:8080";
    public static String sendSessionData(String title, String date, Instant time, String mode){
        try {

            String json = "{"
                + "\"title\":\"" + title + "\","
                + "\"date\":\"" + date + "\","
                + "\"time\":\"" + time + "\","
                + "\"mode\":\"" + mode + "\""
            + "}";

            String urlStr = baseUrl + "/api/createSession";
            URL url = new URI(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try(OutputStream os = conn.getOutputStream()){
               os.write(json.getBytes("UTF-8"));
               os.flush();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder response  = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error communicating with baakcend";
        }
    }

    public static String checkSession(String name, String id) {
        HttpURLConnection conn = null;

        try {
            String urlStr = baseUrl + "/api/session/" + URLEncoder.encode(name, "UTF-8")+"/" + URLEncoder.encode(id, "UTF-8");
            URL url = new URI(urlStr).toURL();

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();

            InputStream is = (status >= 200 && status < 300)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while trying to get data";
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    public static Set<User> joinSession(String sessionID, String name, String userID) {
        HttpURLConnection conn = null;
        Set<User> users = null;

        try { 
            String urlStr = baseUrl + "/api/session/" + sessionID + "/join";
            URL url = new URI(urlStr).toURL();

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String json = String.format("{\"name\":\"%s\", \"userID\":\"%s\"}", name, userID);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    String responseData = response.toString();

                    Gson gson = new Gson();
                    users = gson.fromJson(responseData, new TypeToken<Set<User>>(){}.getType());
                }
            } else {
                System.out.println("Request failed, response code: " + status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return users;
    }


    public static void leaveSession(String sessionID, String name, String userID) {
        HttpURLConnection conn = null;

        try { 
            String urlStr = baseUrl + "/api/session/" + sessionID + "/leave";
            URL url = new URI(urlStr).toURL();

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String json = String.format("{\"name\":\"%s\", \"userID\":\"%s\"}", name, userID);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                System.out.println("User has successfully left the session.");
            } else {
                System.out.println("Request failed, response code: " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}