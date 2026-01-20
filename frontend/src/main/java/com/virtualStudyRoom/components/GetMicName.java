package com.virtualStudyRoom.components;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GetMicName {

    public String getLinuxMic() {
        try {
            Process process = new ProcessBuilder("pactl", "list", "sources", "short")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains("mic1")) { // pick first mic found
                        String[] parts = line.split("\\s+");
                        if (parts.length > 1) {
                            return parts[1]; // device name
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "default"; // fallback
    }


    public String getWindowsMic() {
        try {
            Process process = new ProcessBuilder("ffmpeg", "-list_devices", "true", "-f", "dshow", "-i", "dummy")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                boolean audioSection = false;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.contains("DirectShow audio devices")) {
                        audioSection = true;
                        continue;
                    }
                    if (audioSection && line.startsWith("\"")) {
                        // Device line example: "Microphone (Realtek Audio)"
                        return line.replace("\"", ""); 
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "audio=Microphone"; // fallback
    }


    public String getMacMic() {
        try {
            Process process = new ProcessBuilder("ffmpeg", "-f", "avfoundation", "-list_devices", "true", "-i", "")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                boolean audioSection = false;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.contains("AVFoundation audio devices")) {
                        audioSection = true;
                        continue;
                    }
                    if (audioSection && line.matches("\\[\\d+\\]\\s+.*")) {
                        // line format: [0] Built-in Microphone
                        return line.substring(line.indexOf("]") + 1).trim();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0"; // fallback index
    }

}
