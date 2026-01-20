package com.virtualStudyRoom;

import javax.swing.SwingUtilities;

import com.virtualStudyRoom.frame.MainFrame;

public class App {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}

