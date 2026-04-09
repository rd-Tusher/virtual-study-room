// package com.virtualStudyRoom.utils;

// import java.awt.Color;
// import java.awt.Cursor;
// import javax.swing.ImageIcon;
// import javax.swing.JButton;


// import com.virtualStudyRoom.components.StatusBar;

// public class Cross extends JButton{

//     private ImageIcon crossIcon;

//     public Cross(){
//         crossIcon = StatusBar.loadIcon("assets/cross.png", 30);
//         System.out.println(crossIcon);
//         if (crossIcon == null) {
//             System.out.println("Failed to load cross icon.");
//         }

//         setIcon(crossIcon);
//         setBackground(new Color(0,0,0,0));
//         setBorderPainted(false);
//         setContentAreaFilled(false);
//         setFocusPainted(false);
        
//         setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//         setVisible(true);
//     }
// }


// package com.virtualStudyRoom.utils;

// import java.awt.Cursor;
// import java.awt.Image;
// import javax.swing.ImageIcon;
// import javax.swing.JButton;

// public class Cross extends JButton {

//     public Cross() {
//         // Load directly from filesystem
//         ImageIcon imgeIcon = new ImageIcon("assets/cross.png"); 
//         Image image = imgeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
//         setIcon(new ImageIcon(image));
//         System.out.println(image);
//         System.out.println("Width: " + imgeIcon.getIconWidth() + ", Height: " + imgeIcon.getIconHeight());

//         setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//         setBorderPainted(false);
//         setContentAreaFilled(false);
//         setFocusPainted(false);
//         setVisible(true);

//         setSize(30, 30);
//         setPreferredSize(getSize());
//     }
// }



package com.virtualStudyRoom.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.virtualStudyRoom.components.StatusBar;
import com.virtualStudyRoom.frame.MainFrame;

public class Cross extends JPanel {

    private JButton backwardButton;
    private JButton crossButton;
    private JButton forwardButton;


    public Cross() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0)); 
        setOpaque(false);

        ImageIcon backwardIcon = StatusBar.loadIcon("assets/backward.png", 30);
        ImageIcon crossIcon = StatusBar.loadIcon("assets/cross-black.png", 32);
        ImageIcon forwardIcon = StatusBar.loadIcon("assets/forward.png", 30);

        backwardButton = createButton(backwardIcon);
        crossButton = createButton(crossIcon);
        forwardButton = createButton(forwardIcon);

        backwardButton.setToolTipText("Previous");
        crossButton.setToolTipText("Close");
        forwardButton.setToolTipText("Next");

        crossButton.addActionListener(e -> {
            MainFrame.getMainframe().showResources();
        });


        add(backwardButton);
        add(crossButton);
        add(forwardButton);
    }

    public JButton createButton(ImageIcon icon) {
        JButton button = new JButton(icon);
        button.setBackground(new Color(0, 0, 0, 0));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public JButton getBackwardButton() { return backwardButton; }
    public JButton getCrossButton() { return crossButton; }
    public JButton getForwardButton() { return forwardButton; }
}