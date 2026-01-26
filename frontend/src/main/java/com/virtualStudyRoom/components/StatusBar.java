package com.virtualStudyRoom.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.virtualStudyRoom.frame.MainFrame;
import com.virtualStudyRoom.utils.CreateLabel;
import com.virtualStudyRoom.utils.MuteMic;
import com.virtualStudyRoom.utils.ScreenRecorder;
import com.virtualStudyRoom.utils.ResponseModel.SessionCheckModel;

public class StatusBar extends JPanel{
    private final int arc = 20;

    private JLabel recordLabel;
    private JLabel joinedUsers;
    private JLabel leavLabel;
    private MuteMic micLabel;
    private boolean isRecording = false;
    private boolean isMute = false;
    private MainFrame mainFrame;

    private final ImageIcon stopIcon = loadIcon("assets/stopRecord.png",30);
    private final ImageIcon startIcon = loadIcon("assets/startRecord.png",30);
    private final ImageIcon mic = loadIcon("assets/microphone.png", 30);
    private final ImageIcon leave = loadIcon("assets/leave.png", 30);
    private final ImageIcon users = loadIcon("assets/users.png", 30);

    private final ScreenRecorder recorder = new ScreenRecorder();
    private RecordingBar recordingBar;

    public StatusBar(RecordingBar recordingBar,SessionPanel sessionPanel){
        this.recordingBar = recordingBar;
        setLayout(new FlowLayout(FlowLayout.CENTER,20,15));
        setBackground(new Color(60, 60, 60, 220));

        recordLabel = new JLabel(startIcon);
        recordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(recordLabel);
        recordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                toggleRecording();
            }
        }); 

        micLabel = new MuteMic(mic);
        micLabel.setToolTipText("Mute");
        micLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(micLabel);

        micLabel.addMouseListener((new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                toggleMute();
            }
        }));

        joinedUsers = new JLabel(users);
        add(joinedUsers);
        joinedUsers.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        joinedUsers.setToolTipText("User list");
        joinedUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                sessionPanel.showUserPopup();
            }
        });

        add(CreateLabel.createIcon("assets/maximize.png","Maximize"));

        // leave icon
        leavLabel = new JLabel(leave);
        leavLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(leavLabel);
        leavLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                leaveSession();
                mainFrame = new MainFrame();
                mainFrame.showLanding();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0 , 0, getWidth(), getHeight(),arc,arc);
    }

        private void toggleRecording() {
        if (!isRecording) {
            new Thread(() -> {
                recorder.startRecording();
                SwingUtilities.invokeLater(() -> {
                    isRecording = true;
                    recordLabel.setIcon(stopIcon);
                    recordLabel.setToolTipText("Stop Recording");
                    recordingBar.startTimer();
                });
            }).start();
        } else {
            String filename = UUID.randomUUID().toString().split("-")[0];
            recorder.stopRecording("recording/"+filename+".mp4");
            SwingUtilities.invokeLater(() -> {
                isRecording = false;
                recordLabel.setIcon(startIcon);
                recordLabel.setToolTipText("Start Recording");
                recordingBar.stopTimer();
            });
        }
    }

    private static ImageIcon loadIcon(String path, int size){
        Image img = new ImageIcon(path).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void toggleMute(){
        isMute = !isMute;

        micLabel.setMuted(isMute);
        micLabel.setToolTipText(isMute ? "Unmute" : "Mute");
    }

    private void leaveSession(){

       SessionCheckModel info =  JoinPage.getCheckModel();
       FrontendToBackend.leaveSession(info.sessionID, info.name, info.userID);
    }
}    