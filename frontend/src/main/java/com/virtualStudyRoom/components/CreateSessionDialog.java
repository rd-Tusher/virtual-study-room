package com.virtualStudyRoom.components;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.virtualStudyRoom.frame.MainFrame;
import com.virtualStudyRoom.utils.DTFormatter;
import com.virtualStudyRoom.utils.ResponseModel.SessionResponse;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;

public class CreateSessionDialog extends JPanel {

    private JTextField sessionTitleField;
    private JTextField sessionDateField;
    private JTextField sessionTimeField;
    private JTextField maxUsersField;
    private JTextField maxTimeField;
    private JRadioButton audioOnly;
    private JRadioButton audioWhiteboard;

    private LandingPage landingPage = new LandingPage();
    public CreateSessionDialog(MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(Color.white);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(landingPage.header(), BorderLayout.NORTH);
        add(formPanel(), BorderLayout.CENTER);
        add(buttonPanel(frame), BorderLayout.SOUTH);
    }

    private JPanel formPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    sessionTitleField = new JTextField();
    sessionDateField = new JTextField("2025-12-31");
    sessionTimeField = new JTextField("14:30");
    maxUsersField = new JTextField("50");
    maxUsersField.setEditable(false);
    maxTimeField = new JTextField("60");
    maxTimeField.setEditable(false);

    panel.add(createRow("Session Title : ","Session Title",sessionTitleField));
    panel.add(Box.createVerticalStrut(10));

    
    panel.add(createRow("Session date : ","2025-12-25",sessionDateField));
    panel.add(Box.createVerticalStrut(10));

    
    panel.add(createRow("Session start time : ","14:30",sessionTimeField));
    panel.add(Box.createVerticalStrut(10));

    
    panel.add(createRow("Max allowed user : ","Max allowed user",maxUsersField));
    panel.add(Box.createVerticalStrut(10));

    
    panel.add(createRow("Max allowed time : ","Max allowed time",maxTimeField));
    panel.add(Box.createVerticalStrut(10));

    
    audioOnly = new JRadioButton("Audio Only");
    audioWhiteboard = new JRadioButton("Audio + Whiteboard", true);
    
    Font radioFont = new Font("Segoe UI", Font.PLAIN, 13);
    audioOnly.setFont(radioFont);
    audioWhiteboard.setFont(radioFont);

    audioOnly.setAlignmentX(Component.LEFT_ALIGNMENT);
    audioWhiteboard.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    
    ButtonGroup group = new ButtonGroup();
    group.add(audioOnly);
    group.add(audioWhiteboard);


    panel.add(createOptionRow("Session Type : ", audioOnly,audioWhiteboard)); // optional: pushes buttons to bottom
    panel.add(Box.createHorizontalGlue());

    return panel;
}


    private JPanel buttonPanel(MainFrame frame) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));
        panel.setOpaque(false);

        JButton cancel = new JButton("Cancel");
        JButton create = new JButton("Create");

        create.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        create.setBackground(new Color(13, 110, 253));
        create.setForeground(Color.WHITE);
        cancel.setBackground(new Color(220, 220, 220));
        cancel.setForeground(Color.BLACK);
        
        create.setFocusPainted(false);
        create.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        cancel.setFocusPainted(false);
        cancel.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        
        create.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));
        cancel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));
        
        
        panel.add(cancel);
        panel.add(create);
        
        create.addActionListener(e -> {
            String title = sessionTitleField.getText().trim();
            String date = sessionDateField.getText().trim();

            String time = sessionTimeField.getText().trim();
            Instant newTime = DTFormatter.convertToUTC(time,date);
            if(newTime == null){
                return;
            }
            String mode = audioOnly.isSelected() ? "single" : "dual";
            
            String response = FrontendToBackend.sendSessionData(title, date, newTime, mode);
            Gson gson = new Gson();
            JsonObject fullRes = gson.fromJson(response, JsonObject.class);
            JsonObject body = fullRes.getAsJsonObject("body");
            SessionResponse jsonResponse = gson.fromJson(body,SessionResponse.class);
            if (jsonResponse.joinCode != null) {
                frame.showSessionInfo(jsonResponse);
            }
            System.out.println("The response from backend  :  " + jsonResponse.joinCode);
        });

        return panel;
    } 



    public JPanel createRow(String labelText,String tootTip,JTextField inputField) {
        JPanel row = new JPanel(new GridBagLayout());

        row.setOpaque(false);
        row.setMaximumSize(new Dimension(500, 40)); 

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 10); 
        c.anchor = GridBagConstraints.LINE_START;


        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;           
        c.fill = GridBagConstraints.NONE;
        row.add(lbl, c);


        if (tootTip != null && !tootTip.isEmpty()) {
            inputField.setToolTipText(tootTip);
        }

        inputField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        inputField.setBackground(new Color(248, 249, 250)); 
        inputField.setDisabledTextColor(Color.DARK_GRAY);


        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1, true),
                BorderFactory.createEmptyBorder(0, 8, 0, 8) 
        ));

        c.gridx = 1;
        c.weightx = 1;              
        c.fill = GridBagConstraints.HORIZONTAL;
        row.add(inputField, c);

        return row;
    }

    private JPanel createOptionRow(String labelText, JRadioButton... options) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setMaximumSize(new Dimension(500, 40));
        row.setOpaque(false);
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 10);
        c.anchor = GridBagConstraints.LINE_START;
        
        // Label on the left
        JLabel lbl = new JLabel(labelText);
        lbl.setOpaque(false);
        lbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        row.add(lbl, c);

        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); // horizontal spacing
        optionPanel.setOpaque(false);
        for (JRadioButton option : options) {
            option.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            optionPanel.add(option);
            option.setOpaque(false);
        }

        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        row.add(optionPanel, c);

        return row;
    }   
}