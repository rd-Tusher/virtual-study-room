package com.virtualStudyRoom.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.virtualStudyRoom.components.GetMicName;

public class ScreenRecorder {

    private Process ffmpegProcess;
    private final OSType osType = new OSType();
    private final GetMicName getMicName = new GetMicName();

    private String tempFilePath;

    public void startRecording() {
        try {

            // Temporary file for safe recording
            File tempFile = File.createTempFile("session_", ".mp4");
            tempFilePath = tempFile.getAbsolutePath();

            List<String> command = new ArrayList<>();
            command.add("ffmpeg");
            command.add("-y"); // overwrite if exists

            /* ===================== OS-SPECIFIC INPUT ===================== */
            if (osType.isWindows()) {
                command.addAll(List.of(
                        "-f", "gdigrab",
                        "-framerate", "30",
                        "-i", "desktop",
                        "-f", "dshow",
                        "-i", "audio=Microphone"
                ));
            } else if (osType.isMac()) {
                command.addAll(List.of(
                        "-f", "avfoundation",
                        "-framerate", "30",
                        "-i", "1:0" // 1 = screen, 0 = microphone
                ));
            } else if (osType.isLinux()) {
                String micDevice = getMicName.getLinuxMic();
                System.out.println("Using microphone: " + micDevice);
                command.addAll(List.of(
                        "-f", "x11grab",
                        "-framerate", "30",
                        "-i", ":0.0",
                        "-f", "pulse",
                        "-thread_queue_size","1024",
                        "-i", micDevice
                ));
            }

            /* ===================== OUTPUT SETTINGS ===================== */

            command.addAll(List.of(
                "-map", "0:v",    // first input = video
                "-map", "1:a",    // second input = audio
                "-af", "volume=1.0,aresample=async=1",
                "-c:v", "libx264",
                "-preset", "slow",
                "-crf", "18",
                "-pix_fmt", "yuv420p",
                "-c:a", "aac",
                "-b:a", "128k",
                "-ar", "48000",
                "-ac", "2",
                tempFilePath
            ));


            // Start FFmpeg process
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            ffmpegProcess = pb.start();

            // Consume FFmpeg output to prevent blocking
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(ffmpegProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[FFMPEG] " + line);
                    }
                } catch (IOException ignored) {}
            }).start();

            System.out.println("Recording started on " + osType.getOsName());
            System.out.println("Temporary file: " + tempFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording(String outputFile) {
        try {
            File outFile = new File(outputFile);
            outFile.getParentFile().mkdirs();
            String absolutePath = outFile.getAbsolutePath();

            if (ffmpegProcess != null) {
                OutputStream os = ffmpegProcess.getOutputStream();
                os.write("q\n".getBytes()); // gracefully stop FFmpeg
                os.flush();
                ffmpegProcess.waitFor();
                ffmpegProcess = null;

                // Move temp file to final destination
                Path finalPath = Paths.get(absolutePath);
                Files.createDirectories(finalPath.getParent());
                Files.move(Paths.get(tempFilePath), finalPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Recording stopped successfully.");
                System.out.println("Saved to: " + absolutePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

