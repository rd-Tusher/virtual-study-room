package com.virtualStudyRoom.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.virtualStudyRoom.frame.MainFrame;
import com.virtualStudyRoom.utils.Button;
import com.virtualStudyRoom.utils.ValidatorUtil;
import com.virtualStudyRoom.utils.ResponseModel.JoinSessionResponse;
import com.virtualStudyRoom.utils.ResponseModel.SessionCheckModel;

public class JoinPage extends JPanel{

    private JTextField username;
    private JTextField sessionID;
    private LandingPage landingPage = new LandingPage();
    private MainFrame frame;
    private static JoinSessionResponse sessionCheckModel;

    public JoinPage(){}

    public JoinPage(MainFrame frame){
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.white);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(landingPage.header(),BorderLayout.NORTH);
        add(formPanel(), BorderLayout.CENTER);
        add(buttonRow(), BorderLayout.SOUTH);
    }

    private JPanel formPanel(){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        username = new JTextField();
        sessionID = new JTextField();
        panel.add(CreateSessionDialog.createRow("Name : ","Enter your name",username));
        panel.add(Box.createVerticalStrut(10));

        panel.add(CreateSessionDialog.createRow("Session ID : ","Enter session id",sessionID));
        panel.add(Box.createVerticalStrut(10));
        return panel;
    }

    private JPanel buttonRow(){
        JPanel buttonPanel= new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10  ,  25 , 15, 20));
        buttonPanel.setOpaque(false);
        JButton cancelButton = Button.createButton("Cancel", new Color(220, 220, 220), Color.BLACK);
        JButton joinButton = Button.createButton("Join",new Color(13, 110, 253), Color.WHITE);
        buttonPanel.add(cancelButton);
        buttonPanel.add(joinButton);

        joinButton.addActionListener(e -> {
            String name = username.getText();
            if (!ValidatorUtil.checkTtitle(username)) {
                return;
            }

            String id = sessionID.getText().trim();
            if (!ValidatorUtil.checkID(sessionID)) {
                return;
            }
            String response = FrontendToBackend.checkSession(name, id);
            Gson gson = new Gson();
            JsonObject fullJson = gson.fromJson(response, JsonObject.class);
            JoinSessionResponse mainRes = gson.fromJson(fullJson , JoinSessionResponse.class);
            if (mainRes != null) {
                System.out.println(mainRes.remainingSeconds);
                sessionCheckModel = mainRes;
                frame.showWaitingRoom(mainRes);

                if (mainRes.lateUserInfo != null) {
                    SwingUtilities.invokeLater(()-> {
                        Whiteboard whiteboard = Whiteboard.getWhiteboard();
                        System.out.println("new height : "  + mainRes.lateUserInfo.getCanvasHeight());
                        whiteboard.growCanvas(mainRes.lateUserInfo.getCanvasHeight());
                        for (StrokeDTO strokeDTO : mainRes.lateUserInfo.getStrokeDTO()) {
                            Whiteboard.Stroke stroke = new Whiteboard.Stroke(new Color(strokeDTO.colorRGB), strokeDTO.strokeSize);
                            for (StrokeDTO.PointDTO p : strokeDTO.points) {
                                stroke.points.add(new Point(p.x , p.y));
                            }
                            whiteboard.addRemoteStroke(stroke);
                        }
                        WaitingRoom.getSesionPanel().applyRemoteScroll(mainRes.lateUserInfo.getVerticalPercentage());
                    });
                }
            }
        });

        cancelButton.addActionListener(e -> {
            frame.showLanding();
        });

        return buttonPanel;
    }
    public static void getCheckModel(){
        FrontendToBackend.leaveSession(sessionCheckModel.sessionID, sessionCheckModel.name, sessionCheckModel.userID);
    }
}