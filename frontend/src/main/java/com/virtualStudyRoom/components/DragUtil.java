package com.virtualStudyRoom.components;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;

public class DragUtil {

    public static void makeDraggable(JComponent comp) {
        final Point[] click = new Point[1];

        comp.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                click[0] = e.getPoint();
                comp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                comp.setCursor(Cursor.getDefaultCursor());
            }
        });

        comp.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e){
                Point loc = comp.getLocation();
                comp.setLocation(
                    loc.x + e.getX() - click[0].x,
                    loc.y + e.getY() - click[0].y
                );
            }
        });
    }
}
