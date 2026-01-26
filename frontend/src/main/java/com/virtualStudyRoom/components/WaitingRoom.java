package com.virtualStudyRoom.components;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import com.virtualStudyRoom.frame.MainFrame;
import com.virtualStudyRoom.utils.AnimatedTimerPanel;
import com.virtualStudyRoom.utils.EndedPanel;
import com.virtualStudyRoom.utils.ResponseModel.SessionCheckModel;

public class WaitingRoom extends JPanel {

    private LandingPage landingPage = new LandingPage();
    private SessionPanel sessionPanel;
    protected SessionWebSocketClient wsClient;

    public WaitingRoom(SessionCheckModel response, MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);


        if ("LIVE".equals(response.status)) {
            wsClient = new SessionWebSocketClient("ws://localhost:8080/session-websocket", response.sessionID,response.name,response.userID);
            SessionWebSocketClient.setInstance(wsClient);
            showLiveSession(frame);
            System.out.println(response.sessionID);  
            UserPopupFactory.setSessionID(response.sessionID);
        } else {
            showNonLiveState(response);
        }
    }

    private void showLiveSession(MainFrame frame) {
        sessionPanel = new SessionPanel(frame);
        wsClient.setWhiteboard(sessionPanel.getWhiteboard());
        add(sessionPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
 
    private void showNonLiveState(SessionCheckModel response) {

        add(landingPage.header(), BorderLayout.NORTH);

        if ("WAITING".equals(response.status)) {
            AnimatedTimerPanel timerPanel = new AnimatedTimerPanel(response);
            add(timerPanel,BorderLayout.CENTER);
        } 
        else if ("ENDED".equals(response.status)) {
            add(new EndedPanel(), BorderLayout.CENTER);
        }
    }
}
