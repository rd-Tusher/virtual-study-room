package com.virtualStudyRoom.components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


public class RecordingBar extends JPanel {

    private JLabel statusLabel;
    private Timer timer;
    private int elapsedSec = 0;
    private final int arc = 20;
    private final Color bgColor = new Color(60, 60, 60, 220);

    private boolean showDot = true;
    private Timer blinkTimer;
    private final int dotSize = 12;
    private final Color dotColor = Color.RED;

    public RecordingBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setOpaque(false);

        statusLabel = new JLabel("Recording ... 00:00");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        statusLabel.setOpaque(false);

        add(statusLabel);
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 5)); 
        setVisible(false);
    }

    public void startTimer() {
        elapsedSec = 0;
        setVisible(true);

        timer = new Timer(1000, e -> {
            elapsedSec++;
            int minutes = elapsedSec / 60;
            int seconds = elapsedSec % 60;
            statusLabel.setText(String.format("Recording ... %02d:%02d", minutes, seconds));
        });
        timer.start();

        blinkTimer = new Timer(500, e -> {
            showDot = !showDot;
            repaint(); 
        });
        blinkTimer.start();
    }

    public void stopTimer() {
        if (timer != null) timer.stop();
        if (blinkTimer != null) blinkTimer.stop();
        elapsedSec = 0;
        repaint();
        statusLabel.setText("Recording ... 00:00");
        setVisible(false);

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        if (showDot) {
            int dotX = 10;
            int dotY = getHeight() / 2 - dotSize / 2;
            g2.setColor(dotColor);
            g2.fillOval(dotX, dotY, dotSize, dotSize);
        }

        g2.dispose();
    }
}