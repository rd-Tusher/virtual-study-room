package com.virtualStudyRoom.components;

import java.awt.BorderLayout;
import java.awt.Color;

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

        JButton joinButton = new JButton("Join");
        panel.add(joinButton);

        joinButton.addActionListener(e -> {
            String name = username.getText();
            String id = sessionID.getText();
            String response = FrontendToBackend.checkSession(name, id);
            Gson gson = new Gson();
            JsonObject fullJson = gson.fromJson(response, JsonObject.class);
            SessionCheckModel mainRes = gson.fromJson(fullJson , SessionCheckModel.class);
            if (mainRes != null) {
                System.out.println(mainRes.remainingSeconds);
                frame.showWaitingRoom(mainRes);
            }
        });
        return panel;
    }
}