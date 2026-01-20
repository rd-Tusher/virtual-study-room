package com.virtualStudyRoom.utils;

import java.awt.*;
import javax.swing.border.Border;

public class RoundedBorder implements Border {

    private int radius;
    private Color color;
    private int thickness;

    public RoundedBorder(int radius, Color color, int thickness) {
        this.radius = radius;
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(thickness, thickness, thickness, thickness);
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(
            Component c, Graphics g,
            int x, int y, int width, int height) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

        g2.drawRoundRect(
                x + thickness / 2,
                y + thickness / 2,
                width - thickness,
                height - thickness,
                radius,
                radius
        );
    }
}
