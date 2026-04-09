package com.virtualStudyRoom.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;


public class Button {

    public static JButton createButton(String name, Color color, Color fontColor ){
        JButton button = new JButton(name);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(color);
        button.setForeground(fontColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6,16,6,16));
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD,15));
        return button;
    }
}