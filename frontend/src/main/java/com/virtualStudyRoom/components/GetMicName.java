// package com.virtualStudyRoom.components;

// import java.io.BufferedReader;
// import java.io.InputStreamReader;

// public class GetMicName {

//     public String getLinuxMic() {
//         try {
//             Process process = new ProcessBuilder("pactl", "list", "sources", "short")
//                     .redirectErrorStream(true)
//                     .start();

//             try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                 String line;
//                 while ((line = reader.readLine()) != null) {
//                     if (line.toLowerCase().contains("mic1")) { // pick first mic found
//                         String[] parts = line.split("\\s+");
//                         if (parts.length > 1) {
//                             return parts[1]; // device name
//                         }
//                     }
//                 }
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return "default"; // fallback
//     }


//     public String getWindowsMic() {
//         try {
//             Process process = new ProcessBuilder("ffmpeg", "-list_devices", "true", "-f", "dshow", "-i", "dummy")
//                     .redirectErrorStream(true)
//                     .start();

//             try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                 String line;
//                 boolean audioSection = false;
//                 while ((line = reader.readLine()) != null) {
//                     line = line.trim();
//                     if (line.contains("DirectShow audio devices")) {
//                         audioSection = true;
//                         continue;
//                     }
//                     if (audioSection && line.startsWith("\"")) {
//                         // Device line example: "Microphone (Realtek Audio)"
//                         return line.replace("\"", ""); 
//                     }
//                 }
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return "audio=Microphone"; // fallback
//     }


//     public String getMacMic() {
//         try {
//             Process process = new ProcessBuilder("ffmpeg", "-f", "avfoundation", "-list_devices", "true", "-i", "")
//                     .redirectErrorStream(true)
//                     .start();

//             try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                 String line;
//                 boolean audioSection = false;
//                 while ((line = reader.readLine()) != null) {
//                     line = line.trim();
//                     if (line.contains("AVFoundation audio devices")) {
//                         audioSection = true;
//                         continue;
//                     }
//                     if (audioSection && line.matches("\\[\\d+\\]\\s+.*")) {
//                         // line format: [0] Built-in Microphone
//                         return line.substring(line.indexOf("]") + 1).trim();
//                     }
//                 }
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return "0"; // fallback index
//     }

// }


package com.virtualStudyRoom.components;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GetMicName {

    private String cachedLinuxMic = null;
    private String cachedWindowsMic = null;
    private String cachedMacMic = null;

    /** ---------------- LINUX ---------------- */
    public String getLinuxMic() {
        if (cachedLinuxMic != null) return cachedLinuxMic;

        try {
            Process process = new ProcessBuilder("pactl", "list", "sources", "short")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[Linux Mic] " + line); // optional debug

                    String[] parts = line.split("\\s+");
                    if (parts.length >= 2) {
                        String deviceName = parts[1];
                        String type = parts.length > 2 ? parts[2] : "";

                        // Pick first input source (usually a microphone)
                        if ("input".equalsIgnoreCase(type)) {
                            cachedLinuxMic = deviceName;
                            return cachedLinuxMic;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cachedLinuxMic = "default"; // fallback
        return cachedLinuxMic;
    }

    /** ---------------- WINDOWS ---------------- */
    public String getWindowsMic() {
        if (cachedWindowsMic != null) return cachedWindowsMic;

        try {
            Process process = new ProcessBuilder("ffmpeg", "-list_devices", "true", "-f", "dshow", "-i", "dummy")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                boolean audioSection = false;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    System.out.println("[Windows Mic] " + line); // optional debug

                    if (line.contains("DirectShow audio devices")) {
                        audioSection = true;
                        continue;
                    }

                    if (audioSection && line.startsWith("\"")) {
                        cachedWindowsMic = line.replace("\"", "").trim();
                        return cachedWindowsMic;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cachedWindowsMic = "audio=Microphone"; // fallback
        return cachedWindowsMic;
    }

    /** ---------------- MAC ---------------- */
    public String getMacMic() {
        if (cachedMacMic != null) return cachedMacMic;

        try {
            Process process = new ProcessBuilder("ffmpeg", "-f", "avfoundation", "-list_devices", "true", "-i", "")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                boolean audioSection = false;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    System.out.println("[Mac Mic] " + line); // optional debug

                    if (line.contains("AVFoundation audio devices")) {
                        audioSection = true;
                        continue;
                    }

                    if (audioSection && line.matches("\\[\\d+\\]\\s+.*")) {
                        cachedMacMic = line.substring(line.indexOf("]") + 1).trim();
                        return cachedMacMic;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cachedMacMic = "0"; // fallback index
        return cachedMacMic;
    }
}
