package com.virtualStudyRoom.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class TitleBar extends JPanel {

    private Point clickOffset;
    public TitleBar(JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(0, 40));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        leftPanel.setOpaque(false);

        JLabel title = CreateLabel("Virtual Study Room");
        JLabel status = CreateLabel(" | Connected");
        JLabel roomID = CreateLabel(" | Room-3219");

        leftPanel.add(title);
        leftPanel.add(status);
        leftPanel.add(roomID);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,3));
        
        JButton btnClose = createButton("✕");
        btnClose.addActionListener(e -> System.exit(0));

        JButton btnMin = createButton("_");
        btnMin.addActionListener(e -> frame.setState(Frame.ICONIFIED));

        JButton btnMax = createButton("☐");
        btnMax.addActionListener(e ->{
            if((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH){
                frame.setExtendedState(JFrame.NORMAL);
            }
            else{
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        rightPanel.add(btnMin);
        rightPanel.add(btnMax);
        rightPanel.add(btnClose);
        rightPanel.setOpaque(false);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                Window window = SwingUtilities.getWindowAncestor(TitleBar.this);
                Point windowPos = window.getLocation();
                Point mousePos = e.getLocationOnScreen();

                clickOffset = new Point(
                    mousePos.x - windowPos.x,
                    mousePos.y - windowPos.y
                );
            }

            @Override
            public void mouseReleased(MouseEvent e){
                setCursor(Cursor.getDefaultCursor());
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                Window window = SwingUtilities.getWindowAncestor(TitleBar.this);
                Point mousePos = e.getLocationOnScreen();

                window.setLocation(
                    mousePos.x - clickOffset.x,
                    mousePos.y - clickOffset.y
                );
            }
        });
    }

    public JLabel CreateLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setBackground(Color.BLACK);
        return label;
    }

    public JButton createButton(String icon){
        JButton button = new JButton(icon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Dialog", Font.BOLD, 15));
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}