package com.virtualStudyRoom.components;

import javax.swing.*;
import java.awt.*;

import com.virtualStudyRoom.frame.MainFrame;

public class LandingPage extends JPanel {

    private MainFrame frame;

    public LandingPage(MainFrame frame) {
        this.frame = frame;
        initUI();
    }
    public LandingPage(){}

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));
        setBackground(Color.WHITE);

        add(header());
        add(Box.createVerticalStrut(40));
        add(Box.createVerticalStrut(30));
        add(actionButtons());
        add(Box.createVerticalStrut(30));
        add(footer());
    }

    public JPanel header() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel title = new JLabel("Virtual Study Room");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        title.setForeground(Color.blue);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Study together in real-time");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitle);

        return panel;
    }

    private JPanel actionButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 0, 15));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(300, 120));

        JButton createBtn = new JButton("Create Study Session");
        createBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JButton joinBtn = new JButton("Join with Session ID");
        joinBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        stylePrimaryButton(createBtn);
        styleSecondaryButton(joinBtn);

        createBtn.addActionListener(e -> {
                frame.createSession();
                // frame.showSession();
        });

        joinBtn.addActionListener(e -> {
            frame.joinSession();
        });

        panel.add(createBtn);
        panel.add(joinBtn);

        return panel;
    }

    private JPanel footer() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);

        JButton micTest = new JButton("🎤 Test Mic");
        micTest.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JButton settings = new JButton("⚙ Settings");
        settings.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.add(micTest);
        panel.add(settings);

        return panel;
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.WHITE);
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
}