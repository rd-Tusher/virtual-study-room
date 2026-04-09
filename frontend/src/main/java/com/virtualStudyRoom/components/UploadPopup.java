package com.virtualStudyRoom.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.virtualStudyRoom.frame.MainFrame;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadPopup extends JDialog {

    private JPanel fileListPanel;
    private List<File> fileList = new ArrayList<>();

    public UploadPopup(MainFrame frame) {
        super(frame, true);

        setSize(420,420);
        setLocationRelativeTo(frame);
        setUndecorated(true);
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JPanel card = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            // Fill background (BLACK)
            // g2.setColor(Color.BLACK);
            // g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            // Draw border (WHITE)
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);
        }
    };
        card.setLayout(new BorderLayout(10,10));
        card.setBorder(new EmptyBorder(15,15,15,15));
        card.setBackground(Color.BLACK);

        add(card);

        JLabel title = new JLabel("Upload Files");
        title.setFont(new Font("Segoe UI",Font.BOLD,18));
        title.setForeground(Color.WHITE);

        card.add(title,BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10,10));
        centerPanel.setOpaque(false);

        centerPanel.setBackground(Color.BLACK);
        card.add(centerPanel,BorderLayout.CENTER);

        JPanel dropPanel = createDropArea();
        centerPanel.add(dropPanel,BorderLayout.NORTH);

        fileListPanel = new JPanel();
        fileListPanel.setLayout(new BoxLayout(fileListPanel,BoxLayout.Y_AXIS));
        fileListPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(fileListPanel);
        scroll.setBorder(null);
        // scroll.setBackground(Color.BLACK);
        scroll.getViewport().setBackground(Color.BLACK);

        centerPanel.add(scroll,BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);

        JButton cancel = new JButton("Cancel");
        JButton submit = new JButton("Submit");
        cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        bottom.add(cancel);
        bottom.add(submit);

        card.add(bottom,BorderLayout.SOUTH);

        cancel.addActionListener(e->dispose());
        submit.addActionListener(e->submitFiles());

        setVisible(true);
    }

    private JPanel createDropArea(){

        JPanel drop = new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                Graphics2D g2=(Graphics2D)g;
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(5,5,getWidth()-10,getHeight()-10,20,20);
            }
        };

        drop.setPreferredSize(new Dimension(300,100));
        drop.setLayout(new GridBagLayout());
        drop.setOpaque(false);

        JLabel uploadIcon = new JLabel("+");
        uploadIcon.setFont(new Font("Segoe UI",Font.BOLD,28));
        uploadIcon.setForeground(Color.WHITE);

        JLabel text = new JLabel("Click or Drag Files Here");
        text.setForeground(Color.WHITE);

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner,BoxLayout.Y_AXIS));

        uploadIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(uploadIcon);
        inner.add(text);
        drop.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        drop.add(inner);

        drop.setTransferHandler(new TransferHandler(){

            public boolean canImport(TransferSupport support){
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            public boolean importData(TransferSupport support){
                try{
                    List<File> files = (List<File>)support.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);

                    for(File file:files){
                        addFile(file);
                    }

                    return true;

                }catch(Exception e){
                    e.printStackTrace();
                }

                return false;
            }
        });

        drop.addMouseListener(new java.awt.event.MouseAdapter(){

            public void mouseClicked(java.awt.event.MouseEvent e){

                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);

                int result = chooser.showOpenDialog(drop);

                if(result == JFileChooser.APPROVE_OPTION){

                    for(File f:chooser.getSelectedFiles()){
                        addFile(f);
                    }

                }
            }
        });

        return drop;
    }

    private void addFile(File file){

        JPanel item = new JPanel(new BorderLayout());
        item.setBorder(new EmptyBorder(5,5,5,5));
        item.setBackground(Color.BLACK);

        JLabel icon = new JLabel(getFileIcon(file));

        JLabel name = new JLabel(file.getName());
        name.setForeground(Color.WHITE);

        fileList.add(file);

        JButton remove = new JButton("✕");
        remove.setBackground(Color.BLACK);
        remove.setForeground(Color.WHITE);
        remove.setOpaque(false);
        remove.setBorder(null);
        remove.setFocusPainted(false);
        remove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        remove.addActionListener(e->{
            fileListPanel.remove(item);
            item.setBorder(null);
            fileListPanel.revalidate();
            fileListPanel.repaint();
            fileList.remove(file);
        });

        item.add(icon,BorderLayout.WEST);
        item.add(name,BorderLayout.CENTER);
        item.add(remove,BorderLayout.EAST);

        fileListPanel.add(item);

        fileListPanel.revalidate();
        fileListPanel.repaint();
    }

    private Icon getFileIcon(File file){

        String name=file.getName().toLowerCase();

        if(name.endsWith(".png") || name.endsWith(".jpg"))
            return UIManager.getIcon("FileView.imageIcon");

        if(name.endsWith(".pdf"))
            return UIManager.getIcon("FileView.fileIcon");

        return UIManager.getIcon("FileView.fileIcon");
    }
   
    private void submitFiles(){
        if (fileList.size() == 0) {
            JOptionPane.showMessageDialog(this,"You have not uploaded any file");
            System.out.println("No files to send!");
            return;
        }

        System.out.println("Total selected files : " +  fileList.size());
 
        for (File file : fileList) {
            System.out.println(file);
        }

        File[] files = fileList.toArray(new File[0]);
        FrontendToBackend.sendFile(MainFrame.getSessionID(),MainFrame.getUserID(), files);
        JOptionPane.showMessageDialog(this, "Files submitted!");

        dispose();
    }
} 