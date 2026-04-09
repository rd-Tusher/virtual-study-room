package com.virtualStudyRoom.components;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.virtualStudyRoom.frame.MainFrame;
import com.virtualStudyRoom.utils.Button;
import com.virtualStudyRoom.utils.ValidatorUtil;
import com.virtualStudyRoom.utils.ResponseModel.FileRes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
public class FileInputPanel extends JPanel{

    private LandingPage landingPage;
    private JTextField sessionID;
    private MainFrame frame;

    public FileInputPanel(MainFrame frame){
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        landingPage = new LandingPage();
        add(landingPage.header(), BorderLayout.NORTH);
        add(formPanel(), BorderLayout.CENTER);
        add(buttonRow(), BorderLayout.SOUTH);
    }

    private JPanel formPanel(){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        sessionID = new JTextField();

        panel.add(CreateSessionDialog.createRow("Session ID : ","Enter session id",sessionID));
        panel.add(Box.createVerticalStrut(10));
        return panel;
    }

    private JPanel buttonRow(){
        JPanel buttonPanel= new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10  ,  25 , 15, 20));
        buttonPanel.setOpaque(false);
        JButton cancelButton = Button.createButton("Back", new Color(220, 220, 220), Color.BLACK);
        JButton enterButton = Button.createButton("Enter",new Color(13, 110, 253), Color.WHITE);
        buttonPanel.add(cancelButton);
        buttonPanel.add(enterButton);

        enterButton.addActionListener(e -> {

            String id = sessionID.getText().trim();
            if (!ValidatorUtil.checkID(sessionID)) {
                return;
            }

            System.out.println("entered into the file form" + id);
            FileRes files = BackendToFrontend.getSesisonFile(id);

            if (files != null) {
                ResourcePanel.getResourcePanel().setFiles(files);
                frame.showResource(files);
            }
        });

        cancelButton.addActionListener(e -> {
            frame.showLanding();
        });

        return buttonPanel;
    }
} 