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


    public SessionPanel(JFrame frame) {
        setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        add(layeredPane, BorderLayout.CENTER);

        whiteboard = new Whiteboard(frame);
        whiteboard.setDoubleBuffered(true);

        scrollPane = new JScrollPane(whiteboard);
        
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
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
    
    
// public void showUserPopup() {
//     if (userOverlay != null) return;

//     userOverlay = UserPopupFactory.createOverlay(
//             layeredPane,
//             getSize()
//     );

//     layeredPane.add(userOverlay, JLayeredPane.MODAL_LAYER);
//     layeredPane.repaint();
// }

public void showUserPopup() {
    if (userOverlay != null) return;

    userOverlay = UserPopupFactory.createOverlay(
            this::hideUserPopup,   // 🔥 callback
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

}