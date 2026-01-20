package com.virtualStudyRoom.frame;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.virtualStudyRoom.components.CreateSessionDialog;
import com.virtualStudyRoom.components.JoinPage;
import com.virtualStudyRoom.components.LandingPage;
import com.virtualStudyRoom.components.SessionInfoPage;
import com.virtualStudyRoom.components.SessionPanel;
import com.virtualStudyRoom.components.WaitingRoom;
import com.virtualStudyRoom.utils.ResponseModel.SessionCheckModel;
import com.virtualStudyRoom.utils.ResponseModel.SessionResponse;


public class MainFrame extends JFrame {

    public static final String LANDING = "LANDING";
    public static final String SESSION = "SESSION";
    public static final String CREATE_SESSION = "CREATE_SESSION";
    public static final String JOIN_SESSION = "JOIN_SESSION";
    public static final String SESSION_INFO = "SESSION_INFO";
    public static final String WAITING_ROOM = "WAITING_ROOM";

    private SessionInfoPage sessionInfoPage;
    private CardLayout cardLayout;
    private JPanel root;
    private JoinPage joinPage;
    private WaitingRoom waitingRoom;
    public MainFrame() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        root = new JPanel(cardLayout);
        setContentPane(root);

        LandingPage landingPage = new LandingPage(this);
        SessionPanel sessionPanel = new SessionPanel(this);
        CreateSessionDialog sDialogue = new CreateSessionDialog(this);
        joinPage = new JoinPage(this);
        
         
        sessionInfoPage = new SessionInfoPage();
        
        root.add(landingPage, LANDING);
        root.add(sessionPanel, SESSION);
        root.add(sDialogue, CREATE_SESSION);
        root.add(joinPage, JOIN_SESSION);
        root.add(sessionInfoPage, SESSION_INFO);
        cardLayout.show(root, LANDING);
        setVisible(true);
    }
    

    
    public void showSessionInfo(SessionResponse response) {
        sessionInfoPage.setSessionInfo(response);
        cardLayout.show(root, SESSION_INFO);
    }
    
    public void showWaitingRoom(SessionCheckModel response){
        waitingRoom = new WaitingRoom(response,this);
        root.add(waitingRoom,WAITING_ROOM);
        cardLayout.show(root, WAITING_ROOM);
    }

    public void showSession() {
        cardLayout.show(root, SESSION);
    }

    public void showLanding() {
        cardLayout.show(root, LANDING);
    }

    public void createSession() {
        cardLayout.show(root, CREATE_SESSION);
    }

    public void joinSession() {
        cardLayout.show(root, JOIN_SESSION);
    }
}