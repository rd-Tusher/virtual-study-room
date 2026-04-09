// package com.virtualStudyRoom.components;

// import javax.swing.*;
// import javax.swing.border.*;
// import java.awt.*;
// import java.awt.event.*;

// public class ResourcePanel extends JPanel {

//     private FilePreviewPopup filePreviewPopup;
//     private JPanel pdfGrid;
//     private JPanel linkGrid;
//     private JPanel imageGrid;

//     private boolean isHoverDisabled = false;

//     // Constructor accepts FilePreviewPopup as a parameter
//     public ResourcePanel(FilePreviewPopup filePreviewPopup) {
//         this.filePreviewPopup = filePreviewPopup;
//         setLayout(new BorderLayout());

//         JPanel mainContainer = new JPanel();
//         mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
//         mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

//         pdfGrid = createGrid();
//         linkGrid = createGrid();
//         imageGrid = createGrid();

//         mainContainer.add(createHeader("PDF FILES"));
//         mainContainer.add(pdfGrid);
//         mainContainer.add(Box.createVerticalStrut(25));

//         mainContainer.add(createHeader("LINKS"));
//         mainContainer.add(linkGrid);
//         mainContainer.add(Box.createVerticalStrut(25));

//         mainContainer.add(createHeader("IMAGES"));
//         mainContainer.add(imageGrid);


//         JScrollPane scrollPane = new JScrollPane(mainContainer);
//         scrollPane.setBorder(null);
//         scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

//         add(scrollPane, BorderLayout.CENTER);
//     }

//     // Helper method to create the grid layout
//     private JPanel createGrid() {
//         JPanel grid = new JPanel(new WrapLayout(FlowLayout.LEFT, 15, 15));
//         grid.setAlignmentX(Component.LEFT_ALIGNMENT);
//         return grid;
//     }

//     // Helper method to create header labels for sections (PDF, LINKS, IMAGES)
//     private JLabel createHeader(String text) {
//         JLabel label = new JLabel(text);
//         label.setFont(new Font("SansSerif", Font.BOLD, 20));
//         label.setBorder(new EmptyBorder(10, 0, 15, 0));
//         return label;
//     }

//     // Method to create a resource card (for PDFs, Links, Images)
//     private JPanel createCard(String icon, String title, String url) {
//         JPanel card = new JPanel();
//         card.setLayout(new BorderLayout());
//         card.setPreferredSize(new Dimension(160, 130));

//         // Card border style
//         card.setBorder(new CompoundBorder(
//                 new LineBorder(new Color(220, 220, 220)),
//                 new EmptyBorder(10, 10, 10, 10)
//         ));

//         JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
//         iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 32));

//         JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
//         titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

//         JButton openBtn = new JButton("Open");
//         openBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

//         openBtn.addActionListener(e -> {
//             filePreviewPopup.setVisible(true);
//             filePreviewPopup.getFileViewer().openFileFromBackend(url);
//             filePreviewPopup.repaint();
//             disableHoberEffect(true);
//         });

//         card.add(iconLabel, BorderLayout.NORTH);
//         card.add(titleLabel, BorderLayout.CENTER);
//         card.add(openBtn, BorderLayout.SOUTH);

//         // Hover effect to highlight card border when mouse enters/exits
//         card.addMouseListener(new MouseAdapter() {
//             public void mouseEntered(MouseEvent e) {
//                 if (!isHoverDisabled) {
//                     card.setBorder(new CompoundBorder(
//                         new LineBorder(new Color(100, 150, 255), 2),
//                         new EmptyBorder(10, 10, 10, 10)
//                     ));
//                 }
//             }

//             public void mouseExited(MouseEvent e) {
//                 if (!isHoverDisabled) {
//                     card.setBorder(new CompoundBorder(
//                         new LineBorder(new Color(220, 220, 220)),
//                         new EmptyBorder(10, 10, 10, 10)
//                     ));
//                 }
//             }
//         });

//         return card;
//     }

//     // Methods to add resources (PDFs, Links, Images) to their respective grids
//     public void addPdf(String title, String url) {
//         pdfGrid.add(createCard("📄", title, url));
//     }

//     public void addLink(String title, String url) {
//         linkGrid.add(createCard("🔗", title, url));
//     }

//     public void addImage(String title, String url) {
//         imageGrid.add(createCard("🖼", title, url));
//     }

//     // Refresh the frame after adding resources
//     public void refreshFrame() {
//         revalidate();
//         repaint();
//     }

//     public void disableHoberEffect(boolean disabled){
//         this.isHoverDisabled = disabled;
//     }
// }


package com.virtualStudyRoom.components;

import javax.swing.*;
import javax.swing.border.*;

import com.virtualStudyRoom.frame.MainFrame;
import com.virtualStudyRoom.utils.Cross;
import com.virtualStudyRoom.utils.ResponseModel.FileRes;

import java.awt.*;
import java.awt.event.*;

public class ResourcePanel extends JPanel {

    // private FilePreviewPopup filePreviewPopup;
    private MainFrame frame;
    private JPanel pdfGrid;
    private JPanel linkGrid;
    private JPanel imageGrid;
    private  FileRes files;
    private Cross cross;

    private static ResourcePanel resourcePanel;

    private boolean isHoverDisabled = false;

    public ResourcePanel(MainFrame frame) {
        resourcePanel = this;
        this.frame = frame;
        setLayout(new BorderLayout());

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        pdfGrid = createGrid();
        linkGrid = createGrid();
        imageGrid = createGrid();

        mainContainer.add(createHeader("PDF FILES"));
        mainContainer.add(pdfGrid);
        mainContainer.add(Box.createVerticalStrut(25));

        mainContainer.add(createHeader("LINKS"));
        mainContainer.add(linkGrid);
        mainContainer.add(Box.createVerticalStrut(25));

        mainContainer.add(createHeader("IMAGES"));
        mainContainer.add(imageGrid);


        cross = new Cross();
        ImageIcon crossIcon = StatusBar.loadIcon("assets/cross-black.png", 32);
        JButton returButton = cross.createButton(crossIcon);
        returButton.addActionListener(e -> {
            frame.showLanding();
        });

        JScrollPane scrollPane = new JScrollPane(mainContainer);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);

        add(scrollPane, BorderLayout.CENTER); 
        add(returButton, BorderLayout.SOUTH);
    }

    private JPanel createGrid() {
        JPanel grid = new JPanel(new WrapLayout(FlowLayout.LEFT, 15, 15));
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        return grid;
    }

    private JLabel createHeader(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setBorder(new EmptyBorder(10, 0, 15, 0));
        return label;
    }

    private JPanel createCard(String icon, String title, String url) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(160, 130));

        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 32));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JButton openBtn = new JButton("Open");
        openBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        openBtn.addActionListener(e -> {
            frame.showFilePreview(url);
        });

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(titleLabel, BorderLayout.CENTER);
        card.add(openBtn, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!isHoverDisabled) {
                    card.setBorder(new CompoundBorder(
                        new LineBorder(new Color(100, 150, 255), 2),
                        new EmptyBorder(10, 10, 10, 10)
                    ));
                }
            }

            public void mouseExited(MouseEvent e) {
                if (!isHoverDisabled) {
                    card.setBorder(new CompoundBorder(
                        new LineBorder(new Color(220, 220, 220)),
                        new EmptyBorder(10, 10, 10, 10)
                    ));
                }
            }
        });

        return card;
    }

    public void addPdf(String title, String url) {
        pdfGrid.add(createCard("📄", title, url));
    }

    public void addLink(String title, String url) {
        linkGrid.add(createCard("🔗", title, url));
    }

    public void addImage(String title, String url) {
        imageGrid.add(createCard("🖼", title, url));
    }

    public void refreshFrame() {
        revalidate();
        repaint();
    }

    public void disableHoberEffect(boolean disabled){
        this.isHoverDisabled = disabled;
    }

    public  void setFiles(FileRes files){
        this.files = files;
    }

    public static ResourcePanel getResourcePanel(){
        return resourcePanel;
    }

    public void showResources(){
        SwingUtilities.invokeLater(() -> {
            for (String name : files.fileName) {
                addPdf(name, "http://localhost:8080/files/" + files.sessionID + "/" + name);
            }
            refreshFrame();
        });
    }
}