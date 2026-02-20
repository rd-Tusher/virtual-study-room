package com.virtualStudyRoom.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.virtualStudyRoom.frame.MainFrame;
import com.virtualStudyRoom.utils.ResponseModel.SessionCheckModel;

public class JoinPage extends JPanel{

    private JTextField username;
    private JTextField sessionID;
    private LandingPage landingPage = new LandingPage();
    private CreateSessionDialog sDialogue = new CreateSessionDialog(null);
    private MainFrame frame;
    private static SessionCheckModel sessionCheckModel;

    public JoinPage(){}

    public JoinPage(MainFrame frame){
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.white);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(landingPage.header(),BorderLayout.NORTH);
        add(formPanel(), BorderLayout.CENTER);
    }

    private JPanel formPanel(){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        username = new JTextField();
        sessionID = new JTextField();
        panel.add(sDialogue.createRow("Name : ","Enter your name",username));
        panel.add(Box.createVerticalStrut(10));

        panel.add(sDialogue.createRow("Session ID : ","Enter session id",sessionID));
        panel.add(Box.createVerticalStrut(10));


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 0));
        buttonPanel.setOpaque(false);

        JButton backHome = new JButton("Back to Home");
        backHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backHome.setBackground(new Color(220, 220, 220));
        backHome.setFocusPainted(false);
        backHome.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JButton joinButton = new JButton("Join Session");
        joinButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        joinButton.setBackground(new Color(13, 110, 253));
        joinButton.setForeground(Color.WHITE);
        joinButton.setFocusPainted(false);
        joinButton.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        buttonPanel.add(backHome);
        buttonPanel.add(joinButton);
         
        panel.add(buttonPanel);

 

        joinButton.addActionListener(e -> {
            String name = username.getText();
            String id = sessionID.getText();
            String response = FrontendToBackend.checkSession(name, id);
            Gson gson = new Gson();
            JsonObject fullJson = gson.fromJson(response, JsonObject.class);
            SessionCheckModel mainRes = gson.fromJson(fullJson , SessionCheckModel.class);
            if (mainRes != null) {
                System.out.println(mainRes.remainingSeconds);
                sessionCheckModel = mainRes;
                frame.showWaitingRoom(mainRes);
            }
        });

        backHome.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                MainFrame frame = MainFrame.getMainFrame();
                if(frame != null){
                    frame.showLanding();
                }
            }
        });
        return panel;
    }
    public static SessionCheckModel getCheckModel(){
        System.out.println(sessionCheckModel.name + sessionCheckModel.userID + sessionCheckModel.sessionID);
        return sessionCheckModel;
    }
}