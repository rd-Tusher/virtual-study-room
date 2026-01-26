package com.virtualStudyRoom.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Whiteboard extends JPanel {

    private static final int GROW_BY = 1200;
    private static final int BOTTOM_THRESHOLD = 220;

    private int canvasWidth;
    private int canvasHeight;

    private Color penColor = Color.WHITE;
    private float strokeSize = 3f;

    private final JFrame parentFrame;

    public static class Stroke {
        Color color;
        float strokeSize;
        List<Point> points;
        

        Stroke(Color color, float strokeSize) {
            this.color = color;
            this.strokeSize = strokeSize;
            this.points = new ArrayList<>();
        }
    }

    private final List<Stroke> strokes = new ArrayList<>();
    private Stroke currentStroke = null;

    public  Whiteboard(JFrame frame) {
        this.parentFrame = frame;

        setBackground(Color.BLACK);
        setOpaque(true);

        canvasWidth = frame.getWidth();
        canvasHeight = frame.getHeight();
        updatePanelSize();

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentStroke = new Stroke(penColor, strokeSize);
                currentStroke.points.add(new Point(e.getX(), e.getY()));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentStroke != null) {
                    currentStroke.points.add(new Point(e.getX(), e.getY()));

                    if (e.getY() >= canvasHeight - BOTTOM_THRESHOLD) {
                        
                        int oldHeight = canvasHeight;
                        growCanvas(GROW_BY);

                        SessionWebSocketClient client = SessionWebSocketClient.getInstance();
                        if (client != null && canvasHeight > oldHeight) {
                            client.sendCanvasResize(canvasHeight - oldHeight);
                        }
                    }

                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentStroke != null) {
                    strokes.add(currentStroke);
                    if(SessionWebSocketClient.getInstance() != null){
                        SessionWebSocketClient.getInstance().sendStroke(new StrokeDTO(currentStroke));
                    }
                    currentStroke = null;
                    repaint();
                }
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                canvasWidth = frame.getWidth();
                canvasHeight = Math.max(frame.getHeight(), canvasHeight);
                updatePanelSize();
                repaint();
            }
        });
    }

    public void growCanvas(int growBy) {
        canvasHeight += growBy;
        updatePanelSize();
    }

    private void updatePanelSize() {
        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, canvasWidth, canvasHeight);

        for (Stroke s : strokes) {
            drawStroke(g2, s);
        }

        if (currentStroke != null) {
            drawStroke(g2, currentStroke);
        }
    }

    private void drawStroke(Graphics2D g2, Stroke s) {
    if (s.points.size() < 2) return;

    g2.setColor(s.color);
    g2.setStroke(new BasicStroke(
            s.strokeSize,
            BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND
    ));

    Path2D path = new Path2D.Float();

    Point prev = s.points.get(0);
    path.moveTo(prev.x, prev.y);

    for (int i = 1; i < s.points.size(); i++) {
        Point curr = s.points.get(i);

        int midX = (prev.x + curr.x) / 2;
        int midY = (prev.y + curr.y) / 2;

        path.quadTo(prev.x, prev.y, midX, midY);
        prev = curr;
    }

    path.lineTo(prev.x, prev.y);

    g2.draw(path);
}


    public void setPenColor(Color color) {
        this.penColor = color;
    }

    public void setStrokeSize() {
        this.strokeSize += 2;
        if (strokeSize > 12f) {
            strokeSize = 3f;
        }
    }

    public float getStrokeSize() {
        return this.strokeSize;
    }

    public Color getPenColor() {
        return this.penColor;
    }

    public void undo() {
        if (!strokes.isEmpty()) {
            strokes.remove(strokes.size() - 1);
            repaint();
        }
    }

    public void addRemoteStroke(Stroke stroke){
        strokes.add(stroke);
        repaint();
    }

    public void newPage() {
        growCanvas(parentFrame.getHeight());
        repaint();
    }

    public BufferedImage renderToImage() {
        BufferedImage img = new BufferedImage(parentFrame.getWidth(),parentFrame.getHeight(),BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = img.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, parentFrame.getWidth(),parentFrame.getHeight());

        for (Stroke stroke : strokes) {
            if (stroke.points.size() < 2) continue;

            g2.setColor(stroke.color);
            g2.setStroke(new BasicStroke(
                    stroke.strokeSize,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));

            Path2D path = new Path2D.Float();
            Point start = stroke.points.get(0);
            path.moveTo(start.x, start.y);

            for (int i = 1; i < stroke.points.size(); i++) {
                Point p = stroke.points.get(i);
                path.lineTo(p.x, p.y);
            }

            g2.draw(path);
        }

        g2.dispose();
        return img;
    }


    public static class CanvasResizeDTO {
        public int canvasHeight;
        public String senderID;
    }

}
