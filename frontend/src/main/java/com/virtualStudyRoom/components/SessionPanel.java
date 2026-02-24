package com.virtualStudyRoom.components;

import javax.swing.*;
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



    private boolean isRemoteScroll = false;
    private double lastSendPercent = -1;


    public SessionPanel(JFrame frame) {
        setLayout(new BorderLayout());

        
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        add(layeredPane, BorderLayout.CENTER);

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

        status = new StatusBar(recordingBar,this);
        layeredPane.add(status, JLayeredPane.PALETTE_LAYER);

        // userList = new UserList(this);
        // layeredPane.add(userList, JLayeredPane.PALETTE_LAYER);

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

        // userList.setBounds(0, 0, d.width, 60);

        options.setBounds( 0,(d.height - 300) / 2, 200, 300);

        status.setBounds( (d.width - 300) / 2,  d.height - 90,  300,  60);

        recordingBar.setBounds( 50, d.height - 90, 200, 60);

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

    public Whiteboard getWhiteboard(){
        return whiteboard;
    }



    // addedd later

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

}
