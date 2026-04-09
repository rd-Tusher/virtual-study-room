package com.virtualStudyRoom;

import javax.swing.SwingUtilities;

import com.virtualStudyRoom.components.JcefEngine;
import com.virtualStudyRoom.frame.MainFrame;

public class App {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JcefEngine.initialize();
            new MainFrame();
        });
    }
}