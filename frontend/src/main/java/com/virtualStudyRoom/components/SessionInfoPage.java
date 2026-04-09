// // package com.virtualStudyRoom.components;

// // import javax.swing.*;
// // import java.awt.*;
// // import java.awt.datatransfer.StringSelection;

// // import com.virtualStudyRoom.frame.MainFrame;
// // import com.virtualStudyRoom.utils.Button;

// // import com.virtualStudyRoom.utils.ResponseModel.SessionResponse;

// // public class SessionInfoPage extends JPanel {
    
// //     private MainFrame frame;
// //     private JLabel joinCodeLabel;
// //     private LandingPage landingPage = new LandingPage();

// //     public SessionInfoPage(MainFrame frame) {
// //         this.frame = frame;
// //         setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
// //         setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

// //         add(landingPage.header());
// //         // add(Box.createRigidArea(new Dimension(0, 10)));
// //         add(Box.createVerticalStrut(30));

// //         JLabel label1 = new JLabel("Session Created Successfully!");
// //         label1.setAlignmentX(Component.CENTER_ALIGNMENT);
// //         label1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 23));
// //         add(label1);
// //         add(Box.createRigidArea(new Dimension(0, 10)));

// //         JLabel label2 = new JLabel("Share this join code with others to join the session.");
// //         label2.setAlignmentX(Component.CENTER_ALIGNMENT);
// //         label2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 23));
// //         add(label2);
// //         add(Box.createRigidArea(new Dimension(0, 10)));

// //         // Join code panel
// //         JPanel joinCodePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 3));
// //         joinCodeLabel = new JLabel("Loading...");
// //         joinCodeLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
// //         joinCodeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

// //         joinCodePanel.setBorder(BorderFactory.createCompoundBorder(
// //                 BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
// //                 BorderFactory.createEmptyBorder(3, 8, 3, 8)
// //         ));

// //         // Copy button
// //         ImageIcon copyIcon = new ImageIcon(new ImageIcon("assets/link.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
// //         ImageIcon checkIcon = new ImageIcon(new ImageIcon("assets/right.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
// //         JButton copyButton = new JButton(copyIcon);
// //         copyButton.setToolTipText("Copy");
// //         copyButton.setBorderPainted(false);
// //         copyButton.setContentAreaFilled(false);
// //         copyButton.setFocusPainted(false);
// //         copyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

// //         // Copy functionality with icon swap
// //         copyButton.addActionListener(e -> {
// //             // Copy text
// //             StringSelection selection = new StringSelection(joinCodeLabel.getText());
// //             Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

// //             // Swap to check icon
// //             copyButton.setIcon(checkIcon);
// //             copyButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

// //             // Revert back to copy icon after 1 second
// //             Timer timer = new Timer(2000, ev -> {
// //                 copyButton.setIcon(copyIcon);
// //                 copyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
// //             });
// //             timer.setRepeats(false);
// //             timer.start();
// //         });

// //         joinCodePanel.add(joinCodeLabel);
// //         joinCodePanel.add(copyButton);

// //         // Wrapper panel to center joinCodePanel
// //         JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
// //         wrapper.setOpaque(false);
// //         wrapper.add(joinCodePanel);

// //         add(wrapper);
// //         add(buttonRow());

// //         setVisible(true);
// //     }

// //     public void setSessionInfo(SessionResponse response){
// //         joinCodeLabel.setText(response.joinCode);
// //         revalidate();
// //         repaint();
// //     }

// //     private JPanel buttonRow(){
// //         JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT,5,3));
// //         JButton cancelButton = Button.createButton("Cancel", new Color(220, 220, 220), Color.BLACK);
// //         JButton joinButton = Button.createButton("Join",new Color(13, 110, 253), Color.WHITE);
// //         row.add(cancelButton);
// //         row.add(joinButton);

// //         cancelButton.addActionListener(e -> {
// //             frame.showLanding();
// //         });
// //         joinButton.addActionListener(e -> {
// //             frame.joinSession();
// //         });
// //         return row;

// //     }
// // }



// package com.virtualStudyRoom.components;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.datatransfer.StringSelection;

// import com.virtualStudyRoom.frame.MainFrame;
// import com.virtualStudyRoom.utils.Button;
// import com.virtualStudyRoom.utils.ResponseModel.SessionResponse;

// public class SessionInfoPage extends JPanel {

//     private MainFrame frame;
//     private JLabel joinCodeLabel;
//     private LandingPage landingPage = new LandingPage();

//     public SessionInfoPage(MainFrame frame) {
//         this.frame = frame;

//         setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//         setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

//         // ===== HEADER =====
//         JComponent header = landingPage.header();
//         header.setAlignmentX(Component.CENTER_ALIGNMENT);
//         add(header);

//         add(Box.createRigidArea(new Dimension(0, 15)));

//         // ===== TITLE =====
//         JLabel label1 = new JLabel("Session Created Successfully!");
//         label1.setAlignmentX(Component.CENTER_ALIGNMENT);
//         label1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 23));
//         add(label1);

//         add(Box.createRigidArea(new Dimension(0, 20)));

//         // ===== CENTER GROUP (VERTICALLY CENTERED) =====
//         JPanel centerGroup = new JPanel();
//         centerGroup.setLayout(new BoxLayout(centerGroup, BoxLayout.Y_AXIS));
//         centerGroup.setOpaque(false);
//         centerGroup.setAlignmentX(Component.CENTER_ALIGNMENT);

//         // Subtitle
//         JLabel label2 = new JLabel("Share this join code with others to join the session.");
//         label2.setAlignmentX(Component.CENTER_ALIGNMENT);
//         label2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
//         centerGroup.add(label2);

//         centerGroup.add(Box.createRigidArea(new Dimension(0, 15)));

//         // ===== JOIN CODE PANEL (COMPACT) =====
//         JPanel joinCodePanel = new JPanel();
//         joinCodePanel.setLayout(new BoxLayout(joinCodePanel, BoxLayout.X_AXIS));
//         joinCodePanel.setOpaque(false);
//         joinCodePanel.setBorder(BorderFactory.createCompoundBorder(
//                 BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
//                 BorderFactory.createEmptyBorder(2, 6, 2, 6)
//         ));

//         // Join code label
//         joinCodeLabel = new JLabel("Loading...");
//         joinCodeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
//         joinCodeLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

//         // Copy button
//         ImageIcon copyIcon = new ImageIcon(
//                 new ImageIcon("assets/link.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
//         );
//         ImageIcon checkIcon = new ImageIcon(
//                 new ImageIcon("assets/right.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
//         );

//         JButton copyButton = new JButton(copyIcon);
//         copyButton.setToolTipText("Copy");
//         copyButton.setBorderPainted(false);
//         copyButton.setContentAreaFilled(false);
//         copyButton.setFocusPainted(false);
//         copyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//         copyButton.setMargin(new Insets(0, 0, 0, 0));
//         copyButton.setAlignmentY(Component.CENTER_ALIGNMENT);

//         copyButton.addActionListener(e -> {
//             StringSelection selection = new StringSelection(joinCodeLabel.getText());
//             Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

//             copyButton.setIcon(checkIcon);

//             Timer timer = new Timer(2000, ev -> copyButton.setIcon(copyIcon));
//             timer.setRepeats(false);
//             timer.start();
//         });

//         joinCodePanel.add(joinCodeLabel);
//         joinCodePanel.add(Box.createRigidArea(new Dimension(5, 0))); // small space
//         joinCodePanel.add(copyButton);

//         // Set fixed height to match label height
//         Dimension preferred = joinCodePanel.getPreferredSize();
//         joinCodePanel.setMaximumSize(new Dimension(Short.MAX_VALUE, preferred.height));
//         joinCodePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

//         centerGroup.add(joinCodePanel);

//         centerGroup.add(Box.createRigidArea(new Dimension(0, 15)));

//         // ===== BUTTONS =====
//         JPanel buttons = buttonRow();
//         buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
//         centerGroup.add(buttons);

//         // Center vertically
//         add(Box.createVerticalGlue());
//         add(centerGroup);
//         add(Box.createVerticalGlue());
//     }

//     public void setSessionInfo(SessionResponse response) {
//         joinCodeLabel.setText(response.joinCode);
//         revalidate();
//         repaint();
//     }

//     private JPanel buttonRow() {
//         JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

//         JButton cancelButton = Button.createButton("Back", new Color(220, 220, 220), Color.BLACK);
//         JButton joinButton = Button.createButton("Join", new Color(13, 110, 253), Color.WHITE);

//         row.add(cancelButton);
//         row.add(joinButton);

//         cancelButton.addActionListener(e -> frame.showLanding());
//         joinButton.addActionListener(e -> frame.joinSession());

//         return row;
//     }
// } 



package com.virtualStudyRoom.components;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

import com.virtualStudyRoom.frame.MainFrame;
import com.virtualStudyRoom.utils.Button;
import com.virtualStudyRoom.utils.ResponseModel.SessionResponse;

public class SessionInfoPage extends JPanel {

    private MainFrame frame;
    private JLabel joinCodeLabel;
    private LandingPage landingPage = new LandingPage();

    public SessionInfoPage(MainFrame frame) {
        this.frame = frame;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        // ===== HEADER =====
        JComponent header = landingPage.header();
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(header);

        add(Box.createRigidArea(new Dimension(0, 15)));

        // ===== TITLE =====
        JLabel label1 = new JLabel("Session Created Successfully!");
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        label1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 23));
        add(label1);

        add(Box.createRigidArea(new Dimension(0, 20)));

        // ===== CENTER GROUP (VERTICALLY CENTERED) =====
        JPanel centerGroup = new JPanel();
        centerGroup.setLayout(new BoxLayout(centerGroup, BoxLayout.Y_AXIS));
        centerGroup.setOpaque(false);
        centerGroup.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel label2 = new JLabel("Share this join code with others to join the session.");
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 21));
        centerGroup.add(label2);

        centerGroup.add(Box.createRigidArea(new Dimension(0, 15)));

        // ===== JOIN CODE PANEL (COMPACT WIDTH + HEIGHT) =====
        JPanel joinCodePanel = new JPanel();
        joinCodePanel.setLayout(new BoxLayout(joinCodePanel, BoxLayout.X_AXIS));
        joinCodePanel.setOpaque(false);
        joinCodePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));

        // Join code label
        joinCodeLabel = new JLabel("Loading...");
        joinCodeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        joinCodeLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Copy button
        ImageIcon copyIcon = new ImageIcon(
                new ImageIcon("assets/link.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
        );
        ImageIcon checkIcon = new ImageIcon(
                new ImageIcon("assets/right.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
        );

        JButton copyButton = new JButton(copyIcon);
        copyButton.setToolTipText("Copy");
        copyButton.setBorderPainted(false);
        copyButton.setContentAreaFilled(false);
        copyButton.setFocusPainted(false);
        copyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        copyButton.setMargin(new Insets(0, 0, 0, 0));
        copyButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(joinCodeLabel.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

            copyButton.setIcon(checkIcon);

            Timer timer = new Timer(2000, ev -> copyButton.setIcon(copyIcon));
            timer.setRepeats(false);
            timer.start();
        });

        joinCodePanel.add(joinCodeLabel);
        joinCodePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        joinCodePanel.add(copyButton);

        // 🔥 LIMIT WIDTH AND CENTER
        Dimension pref = joinCodePanel.getPreferredSize();
        pref.width = 350; // set fixed width
        joinCodePanel.setMaximumSize(pref); // restrict width
        joinCodePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerGroup.add(joinCodePanel);

        centerGroup.add(Box.createRigidArea(new Dimension(0, 15)));

        // ===== BUTTONS =====
        JPanel buttons = buttonRow();
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerGroup.add(buttons);

        // Center vertically
        add(Box.createVerticalGlue());
        add(centerGroup);
        add(Box.createVerticalGlue());
    }

    public void setSessionInfo(SessionResponse response) {
        joinCodeLabel.setText(response.joinCode);
        revalidate();
        repaint();
    }

    private JPanel buttonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton cancelButton = Button.createButton("Back", new Color(220, 220, 220), Color.BLACK);
        JButton joinButton = Button.createButton("Join", new Color(13, 110, 253), Color.WHITE);

        row.add(cancelButton);
        row.add(joinButton);

        cancelButton.addActionListener(e -> frame.showLanding());
        joinButton.addActionListener(e -> frame.joinSession());

        return row;
    }
} 