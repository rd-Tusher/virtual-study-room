package com.virtualStudyRoom.components;

import javax.swing.*;

import com.virtualStudyRoom.frame.MainFrame;
import com.virtualStudyRoom.utils.ResponseModel.FileUploadNotification;

import java.awt.*;
import java.awt.event.*;

public class SessionPanel extends JPanel {

    private JLayeredPane layeredPane;
    private JScrollPane scrollPane;
    private Whiteboard whiteboard;
    private WhiteboardOptions options;
    private RecordingBar recordingBar;
    private StatusBar status;
    private JPanel userOverlay;
    private JPanel fileUploadOverlay;
    private static JcefEngine jcefEngine;
    private boolean isRemoteScroll = false;
    private double lastSendPercent = -1;
    private UploadButton uploadButton;
    private static SessionPanel sessionPanel;

    public SessionPanel(MainFrame frame) {
        sessionPanel = this;
        setLayout(new BorderLayout());
        
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        add(layeredPane, BorderLayout.CENTER);

        if (jcefEngine == null) {
            jcefEngine = new JcefEngine("http://localhost:8080/Browser.html");
        }

        whiteboard = new Whiteboard(frame, this);
        whiteboard.setDoubleBuffered(true);

        scrollPane = new JScrollPane(whiteboard);

        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {

            if (isRemoteScroll) return;

            if (!e.getValueIsAdjusting()) {
                sendScrollPosition();
            }
        });


        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(40);
        layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);

        options = new WhiteboardOptions(whiteboard);
        layeredPane.add(options, JLayeredPane.PALETTE_LAYER);

        recordingBar = new RecordingBar();
        layeredPane.add(recordingBar, JLayeredPane.PALETTE_LAYER);

        status = new StatusBar(recordingBar,this, frame);
        layeredPane.add(status, JLayeredPane.PALETTE_LAYER);

        // uploadPopup = new UploadPopup(frame);
        // layeredPane.add(uploadPopup, JLayeredPane.PALETTE_LAYER);
        uploadButton = new UploadButton(frame);
        layeredPane.add(uploadButton, JLayeredPane.PALETTE_LAYER);
        

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLayout();
            }
        });

        SwingUtilities.invokeLater(this::updateLayout);
    }


    private void updateLayout() {
        Dimension d = getSize();

        if (d.width <= 0 || d.height <= 0) {
            return; 
        }

        layeredPane.setBounds(0, 0, d.width, d.height);

        scrollPane.setBounds(0, 0, d.width, d.height);
        
        options.setBounds( 0,(d.height - 300) / 2, 200, 300);
        
        status.setBounds( (d.width - 300) / 2,  d.height - 90,  300,  60);
        
        recordingBar.setBounds( 50, d.height - 90, 200, 60);
        uploadButton.setBounds(d.width-60, 20,45,45);

        layeredPane.revalidate();
        layeredPane.repaint();
    }    


    public void showUserPopup() {
        if (userOverlay != null) return;

        userOverlay = UserPopupFactory.createOverlay(
                this::hideUserPopup,
                getSize()
        );

        layeredPane.add(userOverlay, JLayeredPane.MODAL_LAYER);
        layeredPane.repaint();
    }

    public void hideUserPopup() {
        if (userOverlay != null) {
            layeredPane.remove(userOverlay);
            userOverlay = null;
            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }

    public void showFileNotification(FileUploadNotification notification){
        if(fileUploadOverlay != null) return;
        System.out.println("hi hello inside showFileNotification");
        fileUploadOverlay = FileUploadMessage.createOverlay(this::hidePop, getSize(), notification);

        layeredPane.add(fileUploadOverlay, JLayeredPane.MODAL_LAYER);
        layeredPane.repaint();
    }

    public void hidePop() {
        if (fileUploadOverlay != null) {
            layeredPane.remove(fileUploadOverlay);
            fileUploadOverlay = null;   
            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }

    public Whiteboard getWhiteboard(){
        return whiteboard;
    }

    private void sendScrollPosition() {

        JScrollBar bar = scrollPane.getVerticalScrollBar();

        int max = bar.getMaximum() - bar.getVisibleAmount();
        if (max <= 0) return;

        double percent = bar.getValue() / (double) max;

        if(Math.abs(percent - lastSendPercent) < 0.01){
            return;
        }

        lastSendPercent = percent;

        SessionWebSocketClient client = SessionWebSocketClient.getInstance();
        if (client != null) {
            client.sendScroll(new ScrollMessage(lastSendPercent));
        }
    }

    public void applyRemoteScroll(double percent) {

        if (scrollPane == null) {
            System.out.println("ScrollPane not ready yet");
            return;
        }
        
        System.out.println("percent for scrolling :" + percent);
        JScrollBar bar = scrollPane.getVerticalScrollBar();

        int max = bar.getMaximum() - bar.getVisibleAmount();

        if (max <= 0) return;

        int newValue = (int) (percent * max);

        isRemoteScroll = true;
        bar.setValue(newValue);
        isRemoteScroll = false;

    }

    public  void refresh(){
        if (scrollPane != null) {
            scrollPane.revalidate();
            scrollPane.repaint();
        }

        if (layeredPane != null) {
            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }

    public static JcefEngine getJcefEngine(){
        return jcefEngine;
    }
    public static SessionPanel getSessionPanel(){
        return sessionPanel;
    }
}