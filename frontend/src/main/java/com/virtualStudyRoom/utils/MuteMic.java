package com.virtualStudyRoom.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MuteMic extends JLabel{

    private boolean muted = false;
    public MuteMic(ImageIcon img){
        super(img);
    }
    
    public void setMuted(boolean muted){
        this.muted = muted;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        if(muted){
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            g2.setColor(Color.RED);

            int pad = 3;

            g2.drawLine(pad, getHeight()-pad, getWidth()-pad, pad);

            g2.dispose();
        }
    }
} 