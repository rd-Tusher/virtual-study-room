package com.virtualStudyRoom.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.virtualStudyRoom.components.WhiteboardOptions.RoundedPanel;
import com.virtualStudyRoom.utils.ResponseModel.FileUploadNotification;


public class FileUploadMessage extends JPanel{

    public static JPanel createOverlay(Runnable onClose, Dimension frameSize, FileUploadNotification notification) {
        JPanel overlay = new JPanel();
        overlay.setOpaque(false);
        overlay.setLayout(null);
        overlay.setBounds(0, 0, frameSize.width, frameSize.height);

        RoundedPanel popup = new RoundedPanel(20, new Color(20, 20, 20));
        int w = 300, h = 80;
 
        int popupX = frameSize.width - w - 10;
        int popupY = frameSize.height - h - 10;
        popup.setBounds(popupX, popupY, w, h);

        popup.setLayout(new BorderLayout(10, 10));
        popup.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel label = new JLabel(notification.message);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        popup.add(label, BorderLayout.WEST);
        overlay.add(popup);
  
        new Timer(5000, e-> {
            overlay.remove(popup);
            overlay.revalidate();
            overlay.repaint();
            WaitingRoom.getSesionPanel().hidePop();
        }).start();

        return overlay;
    }
} 