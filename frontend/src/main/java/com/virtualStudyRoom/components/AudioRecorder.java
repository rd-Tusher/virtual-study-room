package com.virtualStudyRoom.components;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import com.virtualStudyRoom.utils.OSType;

public class AudioRecorder {

    private Process ffmpegProcess;
    private final OSType osType = new OSType();
    private final GetMicName getMicName = new GetMicName();

    private String tempFilePath;

    public void startRecording() {
        try {
            File tempFile = File.createTempFile("audio_", ".wav");
            tempFilePath = tempFile.getAbsolutePath();

            List<String> command = new ArrayList<>();
            command.add("ffmpeg");
            command.add("-y");

            if (osType.isWindows()) {

                command.addAll(List.of(
                        "-f", "dshow",
                        "-i", "audio=default"
                ));
            }

            else if (osType.isMac()) {

                command.addAll(List.of(
                        "-f", "avfoundation",
                        "-i", ":0"
                ));
            }

            else if (osType.isLinux()) {

                String mic = getMicName.getLinuxMic();
                System.out.println("Using microphone: " + mic);

                command.addAll(List.of(
                        "-f", "pulse",
                        "-i", mic
                ));
            }

            command.addAll(List.of(
                    "-ac", "2",
                    "-ar", "44100",
                    "-c:a", "pcm_s16le",
                    tempFilePath
            ));

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            ffmpegProcess = pb.start();

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(ffmpegProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[FFMPEG] " + line);
                    }
                } catch (IOException ignored) {}
            }).start();

            System.out.println("Audio recording started");
            System.out.println("Temp file: " + tempFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording(String outputFile) {
        try {
            if (ffmpegProcess == null) return;

            OutputStream os = ffmpegProcess.getOutputStream();
            os.write("q\n".getBytes());
            os.flush();

            ffmpegProcess.waitFor();
            ffmpegProcess = null;

            Path finalPath = Paths.get(outputFile);
            Files.createDirectories(finalPath.getParent());
            Files.move(Paths.get(tempFilePath), finalPath,
                    StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Audio saved to: " + finalPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
