package com.virtualStudyRoom.utils;

import java.awt.*;

import javax.swing.JComponent;


public class Circle extends JComponent {

    private float strokeSize;
    private Color fillColor = Color.BLACK;
    private boolean showNumber ;
    // private boolean pressed = false;

    public Circle(float strokeSize, int diameter) {
        this.strokeSize = strokeSize;
        this.showNumber = true;
        setPreferredSize(new Dimension(diameter, diameter));
        setOpaque(false);
        // enableClickEffect();
    }

        public Circle(Color fillColor, int diameter) {
        this.fillColor = fillColor;
        this.showNumber = false;
        setPreferredSize(new Dimension(diameter, diameter));
        setOpaque(false);
        // enableClickEffect();
    }

    // ---- optional: set fill color ----
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        this.showNumber = false;
        repaint();
    }

    // ---- optional: set stroke size number visibility ----
    public void setShowNumber(boolean showNumber) {
        this.showNumber = showNumber;
        repaint();
    }

    public void setStrokeSize(float strokeSize) {
        this.strokeSize = strokeSize;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight());
        int offsetX = (getWidth() - size) / 2;
        int offsetY = (getHeight() - size) / 2;

        // ---- Fill circle if color is set ----
        if (!showNumber) {
            g2.setColor(fillColor);
            g2.fillOval(offsetX, offsetY, size - 1, size - 1);
        }

        // ---- Draw border ----
        float borderThickness = 1.8f;
        g2.setStroke(new BasicStroke(borderThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(Color.WHITE);
        g2.drawOval(offsetX, offsetY, size - 1, size - 1);

        // ---- Draw stroke number if enabled ----
        if (showNumber) {
            String text = String.valueOf((int)(strokeSize / 2));
            g2.setFont(getFont().deriveFont(Font.BOLD, 14f));
            FontMetrics fm = g2.getFontMetrics();

            int textX = offsetX + (size - fm.stringWidth(text)) / 2;
            int textY = offsetY + (size + fm.getAscent() - fm.getDescent()) / 2;

            g2.drawString(text, textX, textY);
        }
    }


    // private void enableClickEffect(){
    //     addMouseListener(new MouseAdapter() {
    //         @Override
    //         public void mousePressed(MouseEvent e){
    //             pressed = true;
    //             repaint();
    //         }
    //         @Override
    //         public void mouseReleased(MouseEvent e){
    //             pressed = false;
    //             repaint();
    //         }
    //         @Override
    //         public void mouseExited(MouseEvent e){
    //             pressed = false;
    //             repaint();
    //         }
    //     });
    // }

}
