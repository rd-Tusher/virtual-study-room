package com.virtualStudyRoom.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.virtualStudyRoom.utils.Cross;

import java.awt.*;

public class FilePreviewPopup extends JPanel {

    private ClassroomFileViewer fileViewer;
    private JLayeredPane layeredPane;
    private Cross cross;

    public FilePreviewPopup(String titleText) {
        System.out.println(titleText);
        setLayout(new BorderLayout());
        setOpaque(false);

        // JPanel card = new JPanel(new BorderLayout()) {
        //     protected void paintComponent(Graphics g) {
        //         super.paintComponent(g);
        //         Graphics2D g2 = (Graphics2D) g;
        //         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        //                 RenderingHints.VALUE_ANTIALIAS_ON);

        //         g2.setColor(Color.BLACK);
        //         g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        //         g2.setColor(Color.WHITE);
        //         g2.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);
        //     }
        // };
        JPanel card = new JPanel(new BorderLayout());

        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setOpaque(false);

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        cross = new Cross();
        layeredPane.add(cross,JLayeredPane.PALETTE_LAYER);

        
        // ===== VIEWER =====
        fileViewer = new ClassroomFileViewer();

        card.add(fileViewer, BorderLayout.CENTER);

        card.add(layeredPane,BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
        SwingUtilities.invokeLater(this::updateLayout);
    }

    public ClassroomFileViewer getFileViewer() {
        return fileViewer;
    }

    private void updateLayout(){
        Dimension d = getSize();

        if (d.width <= 0 || d.height <= 0) {
            return;
        }

        cross.setBounds((d.width - 30)/2, d.height - 90, 30, 30);
    }
}