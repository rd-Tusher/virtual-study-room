// package com.virtualStudyRoom.frame;

// import java.awt.CardLayout;
// import java.awt.Dimension;
// import javax.swing.JFrame;
// import javax.swing.JPanel;
// import javax.swing.SwingUtilities;

// import com.virtualStudyRoom.components.ClassroomFileViewer;
// import com.virtualStudyRoom.components.CreateSessionDialog;
// import com.virtualStudyRoom.components.FileInputPanel;
// import com.virtualStudyRoom.components.FilePreviewPopup;
// import com.virtualStudyRoom.components.JoinPage;
// import com.virtualStudyRoom.components.LandingPage;
// import com.virtualStudyRoom.components.ResourcePanel;
// import com.virtualStudyRoom.components.SessionInfoPage;
// import com.virtualStudyRoom.components.SessionPanel;
// import com.virtualStudyRoom.components.WaitingRoom;
// import com.virtualStudyRoom.utils.ResponseModel.FileRes;
// import com.virtualStudyRoom.utils.ResponseModel.SessionCheckModel;
// import com.virtualStudyRoom.utils.ResponseModel.SessionResponse;


// public class MainFrame extends JFrame {

//     public static final String LANDING = "LANDING";
//     public static final String SESSION = "SESSION";
//     public static final String CREATE_SESSION = "CREATE_SESSION";
//     public static final String JOIN_SESSION = "JOIN_SESSION";
//     public static final String SESSION_INFO = "SESSION_INFO";
//     public static final String WAITING_ROOM = "WAITING_ROOM";
//     public static final String RESOURCE = "RESOURCE";
//     public static final String FILE_INPUT = "FILE_INPUT";
//     public static final String FILE_VIEW = "FILE_VIEW"; 

//     private SessionInfoPage sessionInfoPage;
//     private CardLayout cardLayout;
//     private JPanel root;
//     private JoinPage joinPage;
//     private WaitingRoom waitingRoom;
//     private SessionPanel sessionPanel;
//     private ResourcePanel resourcePanel;
//     private FileInputPanel fileInputPanel;
//     private ClassroomFileViewer fileViewer;
//     private static String userID;
//     private static String sessionID;
//     public MainFrame() {

//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setMinimumSize(new Dimension(1200, 800));
//         setLocationRelativeTo(null);

//         cardLayout = new CardLayout();
//         root = new JPanel(cardLayout);
//         setContentPane(root);

//         LandingPage landingPage = new LandingPage(this);
//         sessionPanel = new SessionPanel(this);
//         CreateSessionDialog sDialogue = new CreateSessionDialog(this);
//         joinPage = new JoinPage(this);

//         fileInputPanel = new FileInputPanel(this);
        
         
//         sessionInfoPage = new SessionInfoPage(this);
//         // fileViewer = ClassroomFileViewer.getFileViewer();
//         // fileViewer = new ClassroomFileViewer();

//         FilePreviewPopup filePreviewPopup = new FilePreviewPopup( "File Preview");
//         fileViewer = filePreviewPopup.getFileViewer();
//         resourcePanel = new ResourcePanel(this);

//         root.add(landingPage, LANDING);
//         root.add(sessionPanel, SESSION);
//         root.add(sDialogue, CREATE_SESSION);  
//         root.add(joinPage, JOIN_SESSION);
//         root.add(sessionInfoPage, SESSION_INFO);
//         root.add(resourcePanel,RESOURCE);
//         root.add(fileInputPanel,FILE_INPUT);
//         root.add(filePreviewPopup,FILE_VIEW);
//         cardLayout.show(root, LANDING);
//         setVisible(true);
//     }
     

    
//     public void showSessionInfo(SessionResponse response) {
//         sessionInfoPage.setSessionInfo(response);
//         cardLayout.show(root, SESSION_INFO);
//     }
    
//     public void showWaitingRoom(SessionCheckModel response){
//         userID = response.userID;
//         sessionID = response.sessionID;
//         waitingRoom = new WaitingRoom(response,this);
//         root.add(waitingRoom,WAITING_ROOM);
//         cardLayout.show(root, WAITING_ROOM);
//     }

//     public void showResource(FileRes files) {
//         SwingUtilities.invokeLater(() -> {
//             for (String name : files.fileName) {
//                 resourcePanel.addPdf(name, "http://localhost:8080/files/" + files.sessionID  + "/" +name);
//             }
//             resourcePanel.refreshFrame();
//         });
//         cardLayout.show(root,RESOURCE);
//     }

//     public void showFilePreview(String fileUrl) {
//         fileViewer.openFileFromBackend(fileUrl);
//         // System.out.println("entered into showFilePreview");
//         cardLayout.show(root, FILE_VIEW);
//     }
//     public void showSession() { 
//         cardLayout.show(root, SESSION);
//     }

//     public void showLanding() {
//         cardLayout.show(root, LANDING);
//     }

//     public void createSession() {
//         cardLayout.show(root, CREATE_SESSION);
//     }

//     public void joinSession() {
//         cardLayout.show(root, JOIN_SESSION);
//     }



//     public void showFileInput(){
//         cardLayout.show(root, FILE_INPUT);
//     }

//     public SessionPanel getPanel(){
//         return sessionPanel;
//     }

//     public static String getUserID(){
//         return userID;
//     }

//     public static String getSessionID(){
//         return sessionID;
//     }


// }


package com.virtualStudyRoom.frame;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.virtualStudyRoom.components.ClassroomFileViewer;
import com.virtualStudyRoom.components.CreateSessionDialog;
import com.virtualStudyRoom.components.FileInputPanel;
import com.virtualStudyRoom.components.JoinPage;
import com.virtualStudyRoom.components.LandingPage;
import com.virtualStudyRoom.components.ResourcePanel;
import com.virtualStudyRoom.components.SessionInfoPage;
import com.virtualStudyRoom.components.SessionPanel;
import com.virtualStudyRoom.components.WaitingRoom;
import com.virtualStudyRoom.utils.ResponseModel.FileRes;
import com.virtualStudyRoom.utils.ResponseModel.JoinSessionResponse;
import com.virtualStudyRoom.utils.ResponseModel.SessionCheckModel;
import com.virtualStudyRoom.utils.ResponseModel.SessionResponse;

public class MainFrame extends JFrame {

    public static final String LANDING = "LANDING";
    public static final String SESSION = "SESSION";
    public static final String CREATE_SESSION = "CREATE_SESSION";
    public static final String JOIN_SESSION = "JOIN_SESSION";
    public static final String SESSION_INFO = "SESSION_INFO";
    public static final String WAITING_ROOM = "WAITING_ROOM";
    public static final String RESOURCE = "RESOURCE";
    public static final String FILE_INPUT = "FILE_INPUT";
    public static final String FILE_VIEW = "FILE_VIEW";

    private SessionInfoPage sessionInfoPage;
    private CardLayout cardLayout;
    private JPanel root;
    private JoinPage joinPage;
    private WaitingRoom waitingRoom;
    private SessionPanel sessionPanel;
    private ResourcePanel resourcePanel;
    private FileInputPanel fileInputPanel;
    private ClassroomFileViewer fileViewer;

    private static String userID;
    private static String sessionID;

    private static MainFrame frame;

    public MainFrame() {
        frame = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setMinimumSize(new Dimension(dimension.width, dimension.height));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        cardLayout = new CardLayout();
        root = new JPanel(cardLayout);
        setContentPane(root);

        LandingPage landingPage = new LandingPage(this);
        sessionPanel = new SessionPanel(this);
        CreateSessionDialog sDialogue = new CreateSessionDialog(this);
        joinPage = new JoinPage(this);
        fileInputPanel = new FileInputPanel(this);
        sessionInfoPage = new SessionInfoPage(this);
        fileViewer = ClassroomFileViewer.getFileViewer();

        resourcePanel = new ResourcePanel(this);

        root.add(landingPage, LANDING);
        root.add(sessionPanel, SESSION);
        root.add(sDialogue, CREATE_SESSION);
        root.add(joinPage, JOIN_SESSION);
        root.add(sessionInfoPage, SESSION_INFO);
        root.add(resourcePanel, RESOURCE);
        root.add(fileInputPanel, FILE_INPUT);
        root.add(fileViewer,FILE_VIEW);

        // Set the initial view
        cardLayout.show(root, LANDING);

        // Show the main frame
        setVisible(true);
    }

    public void showSessionInfo(SessionResponse response) {
        sessionInfoPage.setSessionInfo(response);
        cardLayout.show(root, SESSION_INFO);
    }

    public void showWaitingRoom(JoinSessionResponse response) {
        userID = response.userID;
        sessionID = response.sessionID;
        waitingRoom = new WaitingRoom(response, this);
        root.add(waitingRoom, WAITING_ROOM);
        cardLayout.show(root, WAITING_ROOM);
    }

    public void showResource(FileRes files) {
        // SwingUtilities.invokeLater(() -> {
        //     for (String name : files.fileName) {
        //         resourcePanel.addPdf(name, "http://localhost:8080/files/" + files.sessionID + "/" + name);
        //     }
        //     resourcePanel.refreshFrame();
        // });
        resourcePanel.showResources();

        cardLayout.show(root, RESOURCE);
    }

    public void showResources(){
        cardLayout.show(root, RESOURCE);
    }

    public void showFilePreview(String fileUrl) {
        fileViewer.openFileFromBackend(fileUrl);
        // System.out.println("entered into showFilePreview");
        cardLayout.show(root, FILE_VIEW);
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

    public void showFileInput() {
        cardLayout.show(root, FILE_INPUT);
    }

    public SessionPanel getPanel() {
        return sessionPanel;
    }

    public static String getUserID() {
        return userID;
    }

    public static String getSessionID() {
        return sessionID;
    }

    public static MainFrame getMainframe(){
        return frame;
    }
}