package com.virtualStudyRoom.components;

import javax.swing.*;
import java.awt.*;
import com.virtualStudyRoom.utils.ResponseModel.SessionResponse;

public class SessionInfoPage extends JPanel {

    private JLabel joinCodeLabel;
    private LandingPage landingPage = new LandingPage();
    public SessionInfoPage() {

        // (new );
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        add(landingPage.header(),Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0, 10)));

        
        // Show session info
        JLabel label1 = new JLabel("Session Created Successfully!");
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        label1.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,23));
        add(label1);
        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel label2 = new JLabel("Share this join code with others to join the session.");
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,23));
        add(label2);
        add(Box.createRigidArea(new Dimension(0, 10)));

        joinCodeLabel = new JLabel("Loading...");
        
        joinCodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinCodeLabel.setFont(new Font(Font.SANS_SERIF,Font.ITALIC,15));
        add(joinCodeLabel);

        setVisible(true);
    }

    public void setSessionInfo(SessionResponse response){
        joinCodeLabel.setText(response.joinCode);
    }
}