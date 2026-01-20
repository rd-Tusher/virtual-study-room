package com.virtualStudyRoom.components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.virtualStudyRoom.utils.CreateLabel;
import com.virtualStudyRoom.utils.ScreenRecorder;

public class StatusBar extends JPanel{
    private final int arc = 20;

    private JLabel recordLabel;
    private JLabel joinedUsers;
    private boolean isRecording = false;
    private final ScreenRecorder recorder = new ScreenRecorder();
    private RecordingBar recordingBar;;
    public StatusBar(RecordingBar recordingBar,SessionPanel sessionPanel){
        this.recordingBar = recordingBar;
        setLayout(new FlowLayout(FlowLayout.CENTER,20,15));
        setBackground(new Color(60, 60, 60, 220));

        recordLabel = CreateLabel.createIcon("assets/startRecord.png", "Start Recording");
        add(recordLabel);
        recordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                toggleRecording();
            }
        });
        add(CreateLabel.createIcon("assets/microphone.png","Mute/Unmute"));

        joinedUsers = CreateLabel.createIcon("assets/users.png","See Joined Users");
        add(joinedUsers);
        joinedUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                sessionPanel.showUserPopup();
            }
        });
        add(CreateLabel.createIcon("assets/maximize.png","Maximize"));
        add(CreateLabel.createIcon("assets/leave.png","Leave"));
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0 , 0, getWidth(), getHeight(),arc,arc);
    }



    private void toggleRecording(){
        if(!isRecording) {
            new Thread(()->{
                recorder.startRecording();
                isRecording = true;
                recordingBar.startTimer();
            }).start();
            recordLabel.setToolTipText("Stop Recording");
        }
        else {
            recorder.stopRecording("recording/session.mp4");
            isRecording = false;
            recordLabel.setToolTipText("Start Recording");
            recordingBar.stopTimer();
        }
    }

}