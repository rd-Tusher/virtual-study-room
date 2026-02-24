package com.virtualStudyRoom.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import com.virtualStudyRoom.components.BackendToFrontend.User;
import com.virtualStudyRoom.components.WhiteboardOptions.RoundedPanel;

public class UserPopupFactory {
    protected static String sessionID;
    protected static List<User> users = new ArrayList<>();    

    public static JPanel createOverlay( Runnable onClose, Dimension frameSize) {
        JPanel overlay = new JPanel();
        overlay.setOpaque(false);
        overlay.setLayout(null);
        overlay.setBounds(0, 0, frameSize.width, frameSize.height);

        RoundedPanel popup = new RoundedPanel(20, new Color(50, 50, 50));
        int w = 500, h = 400;

        int popupX = (frameSize.width - w) / 2;
        int popupY = (frameSize.height - h) / 2;
        popup.setBounds(popupX, popupY, w, h);

        popup.setLayout(new BorderLayout(10, 10));
        popup.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        overlay.add(popup);

        // Grid panel for users
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setBackground(new Color(50, 50, 50));

        users = SessionWebSocketClient.getUsers(sessionID);


        for (User user : users) {
            gridPanel.add(createUserCard(user.getName()));
        }
 
        JScrollPane scrollPane = new JScrollPane(
                gridPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(1);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        popup.add(scrollPane, BorderLayout.CENTER);

        ImageIcon imgeIcon = new ImageIcon("assets/cross.png");
        Image image = imgeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel closeBtn = new JLabel(new ImageIcon(image));
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        closeBtn.setBounds(
                popupX + w / 2 - 15,
                popupY + h + 10,
                30,
                30
        );

        overlay.add(closeBtn);

        MouseAdapter closeHandler = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClose.run();
            }
        };

        closeBtn.addMouseListener(closeHandler);

        overlay.addMouseListener(closeHandler);

        return overlay;
    }

    private static JPanel createUserCard(String userName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(80, 40));
        panel.setBackground(new Color(90, 90, 90));

        JLabel label = new JLabel(userName, JLabel.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    public static void setSessionID(String id){
        sessionID = id;
    }

}