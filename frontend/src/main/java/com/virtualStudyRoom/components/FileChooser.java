package com.virtualStudyRoom.components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FileChooser {

    public void saveWhiteboardAsPDF(Whiteboard whiteboard) {
        try {
            // Ask user where to save PDF
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Whiteboard as PDF");
            fileChooser.setSelectedFile(new File("whiteboard.pdf"));
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File pdfFile = fileChooser.getSelectedFile();
            File tempImage = new File("whiteboard_temp.png");

            BufferedImage canvas = whiteboard.renderToImage() ;
            ImageIO.write(canvas, "PNG", tempImage);

            // Step 2: Call Python script to convert PNG -> multi-page PDF using ProcessBuilder
            ProcessBuilder pb = new ProcessBuilder(
                "python3",
                "/home/rdtusher/Documents/Project/VirtualStudyRoom/frontend/virtual-study-room/src/com/virtualStudyRoom/components/exportToPDF.py",
                tempImage.getAbsolutePath(),
                pdfFile.getAbsolutePath()
            );



            pb.inheritIO(); // optional: shows Python output in console
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                JOptionPane.showMessageDialog(null, "Python script failed!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "PDF saved successfully:\n" + pdfFile.getAbsolutePath());
            }

            // Step 3: Clean up temporary PNG
            tempImage.delete();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error saving PDF: " + ex.getMessage());
        }
    }


}