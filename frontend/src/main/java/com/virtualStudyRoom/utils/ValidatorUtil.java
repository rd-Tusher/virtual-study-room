package com.virtualStudyRoom.utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class ValidatorUtil {

    private static Border defaultBorder = new JTextField().getBorder();
    private static Border errorBorder = BorderFactory.createLineBorder(Color.RED, 2);
    private static Border successBorder = BorderFactory.createLineBorder(Color.GREEN, 3);

    public static void markInvalid(JComponent field) {
        field.setBorder(errorBorder);
    }

    public static boolean checkTtitle(JTextField field){
        String title = field.getText().trim();
        if (title.isEmpty()) {
            field.setBorder(errorBorder);
            return false;
        }
        else {
            if (title.matches("^[A-Za-z\s]+$")) {
                field.setBorder(successBorder);
                return true;
            }
            else {
                field.setBorder(errorBorder);
                return false;
            }
        }
    }

    public static boolean checkDate(JTextField field){
        String dateStr = field.getText().trim();
        if (dateStr.isEmpty()) {
            field.setBorder(errorBorder);
            return false;
        }
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
            try {
                LocalDate date = LocalDate.parse(dateStr,formatter);
                field.setBorder(successBorder);
                return true;
            } catch (Exception e) {
                field.setBorder(errorBorder);
                return false;
            }
        }
    }

    public static boolean checkTime(JTextField field){
        String timeStr = field.getText().trim();
        if (timeStr.isEmpty()) {
            field.setBorder(errorBorder);
            return false;
        }
        else {
            if (timeStr.matches("^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$")) {
                field.setBorder(successBorder);
                return true;
            }
            else {
                field.setBorder(errorBorder);
                return false;
            }
        }
    }

    public static boolean checkID(JTextField field){
        String id = field.getText().trim();
        if (id.isEmpty()) {
            field.setBorder(errorBorder);
            return false;
        }
        else {
            if (id.matches("^[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}$")) {
                field.setBorder(successBorder);
                return true;
            }
            else {
                field.setBorder(errorBorder);
                return false;
            }
        }
    }

    public static void markValid(JComponent field) {
        field.setBorder(defaultBorder);
    }
}