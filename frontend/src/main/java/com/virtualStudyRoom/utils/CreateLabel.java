package com.virtualStudyRoom.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public  class CreateLabel {

    public JLabel createLabel(String text){
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return label;
    }

    public static JLabel createIcon(String url,String toolTip){
        ImageIcon imageIcon = new ImageIcon(url);
        Image image = imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel jLabel = new JLabel(new ImageIcon(image));
        jLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel.setToolTipText(toolTip);
        return jLabel;
    }
}