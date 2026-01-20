package com.virtualStudyRoom.components;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Instant;



public class FrontendToBackend {

    public static String sendSessionData(String title, String date, Instant time, String mode){
        try {

            String json = "{"
                + "\"title\":\"" + title + "\","
                + "\"date\":\"" + date + "\","
                + "\"time\":\"" + time + "\","
                + "\"mode\":\"" + mode + "\""
            + "}";

            String urlStr = "http://localhost:8080/api/createSession";
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
        String urlStr = "http://localhost:8080/api/session/" + URLEncoder.encode(name, "UTF-8")+"/" + URLEncoder.encode(id, "UTF-8");
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






}