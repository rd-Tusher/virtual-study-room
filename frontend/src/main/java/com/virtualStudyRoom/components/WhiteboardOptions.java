package com.virtualStudyRoom.components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import com.virtualStudyRoom.utils.Circle;

public class WhiteboardOptions extends JPanel {
    private FileChooser fileChooser = new FileChooser();

    public WhiteboardOptions(Whiteboard whiteboard) {
        setLayout(new GridBagLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(120, 300));


        RoundedPanel optionsPanel = new RoundedPanel(20, new Color(60, 60, 60, 220));
        optionsPanel.setLayout(new GridLayout(0, 1, 0, 20));
        optionsPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        );

        // ===== TOOL ITEMS =====


        JPanel thickPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        thickPanel.setOpaque(false);
        thickPanel.setToolTipText("Thickness");
        Circle thickCircle = new Circle(whiteboard.getStrokeSize(), 25);
        thickPanel.add(thickCircle);
        thickPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        colorPanel.setOpaque(false);
        colorPanel.setToolTipText("color");
        Circle colorCircle = new Circle(whiteboard.getPenColor(), 25);
        colorPanel.add(colorCircle);
        colorPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        JPanel undoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        undoPanel.setOpaque(false);
        undoPanel.setToolTipText("Undo");
        undoPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ImageIcon undoIcon = new ImageIcon("assets/undo.png");
        Image img = undoIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel undoLabel = new JLabel(new ImageIcon(img));
        undoPanel.add(undoLabel);


        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        savePanel.setOpaque(false);
        savePanel.setToolTipText("Save");
        savePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ImageIcon savImageIcon = new ImageIcon("assets/download.png");
        Image img2 = savImageIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        JLabel saveLabel = new JLabel(new ImageIcon(img2));
        savePanel.add(saveLabel);


        JPanel eraserPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        eraserPanel.setOpaque(false);
        eraserPanel.setToolTipText("Eraser");
        eraserPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ImageIcon erasImageIcon = new ImageIcon("assets/eraser.png");
        Image img3 = erasImageIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        JLabel eraseLabel = new JLabel(new ImageIcon(img3));
        eraserPanel.add(eraseLabel);


        // ===== ADD TO PANEL =====
        optionsPanel.add(colorPanel);
        optionsPanel.add(thickPanel);
        optionsPanel.add(undoPanel);
        optionsPanel.add(savePanel);
        optionsPanel.add(eraserPanel);


        // ===== ACTIONS =====
        colorPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Color c = JColorChooser.showDialog(
                    WhiteboardOptions.this,
                    "Choose Color",
                    Color.BLACK
                );
                if (c != null) {
                    whiteboard.setPenColor(c);
                    colorCircle.setFillColor(c);
                }
            }
        });

        thickCircle.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                whiteboard.setStrokeSize();
                thickCircle.setStrokeSize((whiteboard.getStrokeSize()));
            }
        });

        undoPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                whiteboard.undo();
            }
        });

        savePanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                fileChooser.saveWhiteboardAsPDF(whiteboard);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        add(optionsPanel, gbc);
    }

    // ================= ROUNDED PANEL =================
    static class RoundedPanel extends JPanel {
        private final int arc;
        private final Color bgColor;

        public RoundedPanel(int arc, Color bgColor) {
            this.arc = arc;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2.setColor(bgColor);
            g2.fillRoundRect(
                0, 0, getWidth(), getHeight(), arc, arc
            );
            g2.dispose();
        }
    }
}