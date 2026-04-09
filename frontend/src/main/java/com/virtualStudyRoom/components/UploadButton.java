
package com.virtualStudyRoom.components;

import javax.swing.*;
import com.virtualStudyRoom.frame.MainFrame;

import java.awt.*;

public class UploadButton extends JButton {

    private  ImageIcon uploadIcon;

    public UploadButton(MainFrame frame) {
        uploadIcon = StatusBar.loadIcon("assets/uploadFile.png", 30);

        if (uploadIcon == null) {
            System.err.println("Failed to load icon!");
        }

        setIcon(uploadIcon);
        setBackground(new Color(0,0,0,0));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addActionListener(e -> new UploadPopup(frame));

    }
}
