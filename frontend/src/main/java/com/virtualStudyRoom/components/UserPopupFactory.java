// package com.virtualStudyRoom.components;

// import java.awt.BorderLayout;
// import java.awt.Color;
// import java.awt.Dimension;
// import java.awt.FlowLayout;
// import java.awt.Font;

// import javax.swing.BorderFactory;
// import javax.swing.JLabel;
// import javax.swing.JPanel;

// import com.virtualStudyRoom.components.WhiteboardOptions.RoundedPanel;

// public class UserList extends JPanel{

//     public UserList(SessionPanel panel){

//         setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
//         setOpaque(false);
        
//         for (int i = 0; i < 20; i++) {
//             add(userCard("rdtusher"));
            
//         }
//     }

//     private RoundedPanel userCard(String userName){
//         RoundedPanel roundedPanel = new RoundedPanel(20, new Color(90, 90, 90));
//         roundedPanel.setLayout(new BorderLayout());
//         roundedPanel.setPreferredSize(new Dimension(100, 40));

//         JLabel label = new JLabel(userName, JLabel.CENTER);
//         label.setForeground(Color.WHITE);
//         label.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));

//         roundedPanel.add(label, BorderLayout.CENTER);
//         roundedPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

//         return roundedPanel;
//     }
// }



// package com.virtualStudyRoom.components;

// import javax.swing.*;

// import com.virtualStudyRoom.components.WhiteboardOptions.RoundedPanel;

// import java.awt.*;
// import java.awt.event.*;

// public class UserPopupFactory {

//     public static JPanel createOverlay( JLayeredPane layeredPane, Dimension frameSize) {
//         JPanel overlay = new JPanel();
//         overlay.setOpaque(false);
//         overlay.setLayout(null);
//         overlay.setBounds(0, 0, frameSize.width, frameSize.height);

//         // popup
//         RoundedPanel popup = new RoundedPanel(20, new Color(50, 50, 50));
//         int w = 500, h = 400;

//         int popupX = (frameSize.width - w) / 2;
//         int popupY = (frameSize.height - h) / 2;
//         popup.setBounds(popupX, popupY, w, h);

//         popup.setLayout(new BorderLayout(10, 10));
//         popup.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//         overlay.add(popup);

//         // popup.add(new UserList(), BorderLayout.CENTER);
//     // Grid panel for users
//     JPanel gridPanel = new JPanel(new GridLayout(0, 5, 10, 10));
//     gridPanel.setBackground(new Color(50, 50, 50)); // same as popup
//     for (int i = 1; i <= 20; i++) {
//         gridPanel.add(createUserCard("User " + i));
//     }

//     // Scroll pane
//     JScrollPane scrollPane = new JScrollPane(gridPanel,
//             JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//             JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//     scrollPane.setOpaque(false);
//     scrollPane.getViewport().setOpaque(false);
//     scrollPane.setBorder(null);
//     scrollPane.getVerticalScrollBar().setUnitIncrement(10);

//     popup.add(scrollPane, BorderLayout.CENTER);

//         // close button
//     ImageIcon imgeIcon = new ImageIcon("assets/cross.png");
//     Image image = imgeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
//     JLabel closeBtn = new JLabel(new ImageIcon(image));
//     closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

//         closeBtn.setBounds(
//                 popup.getX() + w / 2 - 15,
//                 popup.getY() + h + 10,
//                 30,
//                 30
//         );

//         overlay.add(closeBtn);

//         closeBtn.addMouseListener(new MouseAdapter() {
//             @Override
//             public void mouseClicked(MouseEvent e) {
//                 layeredPane.remove(overlay);
//                 layeredPane.repaint();
//             }
//         });

//         // click outside closes popup
//         overlay.addMouseListener(new MouseAdapter() {
//             @Override
//             public void mouseClicked(MouseEvent e) {
//                 layeredPane.remove(overlay);
//                 layeredPane.repaint();
//             }
//         });

//         return overlay;
//     }



//     private static JPanel createUserCard(String userName) {
//         JPanel panel = new JPanel();
//         panel.setPreferredSize(new Dimension(80, 40));
//         panel.setBackground(new Color(90, 90, 90));
//         panel.setLayout(new BorderLayout());

//         JLabel label = new JLabel(userName, JLabel.CENTER);
//         label.setForeground(Color.WHITE);
//         label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
//         panel.add(label, BorderLayout.CENTER);

//         return panel;
//     }
// }


package com.virtualStudyRoom.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.virtualStudyRoom.components.WhiteboardOptions.RoundedPanel;

public class UserPopupFactory {

    public static JPanel createOverlay(
            Runnable onClose,
            Dimension frameSize
    ) {
        JPanel overlay = new JPanel();
        overlay.setOpaque(false);
        overlay.setLayout(null);
        overlay.setBounds(0, 0, frameSize.width, frameSize.height);

        // popup
        RoundedPanel popup = new RoundedPanel(20, new Color(50, 50, 50));
        int w = 500, h = 400;

        int popupX = (frameSize.width - w) / 2;
        int popupY = (frameSize.height - h) / 2;
        popup.setBounds(popupX, popupY, w, h);

        popup.setLayout(new BorderLayout(10, 10));
        popup.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        overlay.add(popup);

        // Grid panel for users
        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        gridPanel.setBackground(new Color(50, 50, 50));

        for (int i = 1; i <= 80; i++) {
            gridPanel.add(createUserCard("User " + i));
        }
 
        JScrollPane scrollPane = new JScrollPane(
                gridPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(1);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        popup.add(scrollPane, BorderLayout.CENTER);

        // close button
        ImageIcon imgeIcon = new ImageIcon("assets/cross.png");
        Image image = imgeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel closeBtn = new JLabel(new ImageIcon(image));
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        closeBtn.setBounds(
                popupX + w / 2 - 15,
                popupY + h + 10,
                30,
                30
        );

        overlay.add(closeBtn);

        // CLOSE ACTIONS → delegate to SessionPanel
        MouseAdapter closeHandler = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClose.run();
            }
        };

        closeBtn.addMouseListener(closeHandler);

        // click outside closes popup
        overlay.addMouseListener(closeHandler);

        return overlay;
    }

    private static JPanel createUserCard(String userName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(80, 40));
        panel.setBackground(new Color(90, 90, 90));

        JLabel label = new JLabel(userName, JLabel.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
