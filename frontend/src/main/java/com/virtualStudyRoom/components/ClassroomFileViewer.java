package com.virtualStudyRoom.components;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.virtualStudyRoom.utils.Cross;

public class ClassroomFileViewer extends JPanel {

    private static ClassroomFileViewer fileViewer;
    private JPanel viewerPanel;
    private Cross cross;

    private java.util.Map<String, byte[]> fileCache = new java.util.HashMap<>();

    public ClassroomFileViewer() {
        setLayout(new BorderLayout());

        viewerPanel = new JPanel(new BorderLayout());
        viewerPanel.add(
            new JLabel("Select a file to preview", SwingConstants.CENTER),
            BorderLayout.CENTER
        );
        viewerPanel.setPreferredSize(new Dimension(800, 600));  
        add(viewerPanel, BorderLayout.CENTER);
    }

    public void openFileFromBackend(String fileName) {
        System.out.println(fileName);
        showLoading();

        new SwingWorker<byte[], Void>() {
            @Override
            protected byte[] doInBackground() throws Exception {
                if (fileCache.containsKey(fileName)) {
                    return fileCache.get(fileName);
                } else {
                    InputStream in = new URI(fileName).toURL().openStream();
                    byte[] data = in.readAllBytes();
                    fileCache.put(fileName, data); 
                    return data;
                }
            }

            @Override
            protected void done() {
                try {
                    byte[] data = get();
                    InputStream stream = new ByteArrayInputStream(data);

                    System.out.println("inside done function");
                    renderFile(stream, fileName); 

                } catch (Exception e) {
                    e.printStackTrace();
                    setViewer(new JLabel("Failed to load file", SwingConstants.CENTER)); 
                }
            }
        }.execute(); 
    }

    private void showLoading() {
        JPanel loading = new JPanel(new BorderLayout());
        loading.add(new JLabel("Loading...", SwingConstants.CENTER), BorderLayout.CENTER);
        setViewer(loading);
    }

    private void setViewer(Component comp) {
        viewerPanel.removeAll();
        viewerPanel.add(comp, BorderLayout.CENTER);
        cross = new Cross();
        viewerPanel.add(cross,BorderLayout.SOUTH);
        viewerPanel.revalidate();
        viewerPanel.repaint();
    }

    private void renderFile(InputStream stream, String fileName) throws IOException {
        JPanel panel = new JPanel(new BorderLayout());
        String name = fileName.toLowerCase();

        if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            BufferedImage img = ImageIO.read(stream);
            JLabel label = new JLabel(new ImageIcon(img));
            label.setHorizontalAlignment(JLabel.CENTER);
            panel.add(new JScrollPane(label), BorderLayout.CENTER);

        } else if (name.endsWith(".txt")) {
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.read(new InputStreamReader(stream), null);
            panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
            System.out.println("rendred text file content : " + textArea.getText());

        } 

        else if (name.endsWith(".pdf")) {
            byte[] pdfData = stream.readAllBytes();
            PDDocument document = Loader.loadPDF(pdfData);
            PDFRenderer renderer = new PDFRenderer(document);

            JPanel pdfPanel = new JPanel();
            pdfPanel.setLayout(new BoxLayout(pdfPanel, BoxLayout.Y_AXIS));

            JScrollPane scrollPane = new JScrollPane(pdfPanel);
            scrollPane.getVerticalScrollBar().setUnitIncrement(30);
            panel.add(scrollPane, BorderLayout.CENTER);

            int totalPages = document.getNumberOfPages();

            JLabel globalLoading = new JLabel("Loading...", SwingConstants.CENTER);
            globalLoading.setForeground(Color.GRAY);
            globalLoading.setPreferredSize(new Dimension(800, 100));
            pdfPanel.add(globalLoading);

            java.util.List<JLabel> pageLabels = new ArrayList<>();
            final int ESTIMATED_HEIGHT = 1100;

            for (int i = 0; i < totalPages; i++) {
                JLabel label = new JLabel();
                label.setPreferredSize(new Dimension(800, ESTIMATED_HEIGHT));
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                pageLabels.add(label);
                pdfPanel.add(label);
            }

            // ✅ Store original images for resizing
            java.util.List<BufferedImage> originalPages = new ArrayList<>(Collections.nCopies(totalPages, null));
            java.util.Set<Integer> loadedPages = new HashSet<>();

            Runnable loadVisiblePages = () -> {
                // int panelWidth = scrollPane.getViewport().getWidth();
                int viewY = scrollPane.getVerticalScrollBar().getValue();
                int viewHeight = scrollPane.getViewport().getHeight();

                int yOffset = 0;

                for (int i = 0; i < totalPages; i++) {
                    JLabel label = pageLabels.get(i);
                    int currentHeight = label.getHeight() > 0 ? label.getHeight() : ESTIMATED_HEIGHT;

                    boolean isVisible = (yOffset + currentHeight >= viewY - 300) &&
                                        (yOffset <= viewY + viewHeight + 300);

                    if (isVisible && !loadedPages.contains(i)) {
                        loadedPages.add(i);
                        final int pageIndex = i;

                        new SwingWorker<BufferedImage, Void>() {
                            @Override
                            protected BufferedImage doInBackground() throws Exception {
                                return renderer.renderImageWithDPI(pageIndex, 120);
                            }

                            @Override
                            protected void done() {
                                try {
                                    BufferedImage image = get();
                                    originalPages.set(pageIndex, image); // store original

                                    int newWidth = scrollPane.getViewport().getWidth() - 10;
                                    int newHeight = (int) ((double) image.getHeight() / image.getWidth() * newWidth);

                                    BufferedImage scaledImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                                    Graphics2D g2 = scaledImg.createGraphics();
                                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                    g2.drawImage(image, 0, 0, newWidth, newHeight, null);
                                    g2.dispose();

                                    label.setIcon(new ImageIcon(scaledImg));
                                    label.setPreferredSize(new Dimension(newWidth, newHeight));

                                    pdfPanel.revalidate();
                                    pdfPanel.repaint();

                                    // ✅ Remove global loading after first page renders
                                    if (globalLoading.getParent() != null) {
                                        pdfPanel.remove(globalLoading);
                                        pdfPanel.revalidate();
                                        pdfPanel.repaint();
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }.execute();
                    }

                    yOffset += currentHeight;
                }
            };

            javax.swing.Timer timer = new javax.swing.Timer(150, e -> loadVisiblePages.run());
            timer.setRepeats(false);

            scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> timer.restart());

            scrollPane.getViewport().addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        int panelWidth = scrollPane.getViewport().getWidth() - 10;

                        for (int i = 0; i < totalPages; i++) {
                            BufferedImage original = originalPages.get(i);
                            JLabel label = pageLabels.get(i);

                            if (original != null && label.getIcon() != null) {
                                int newHeight = (int) ((double) original.getHeight() / original.getWidth() * panelWidth);

                                BufferedImage scaledImg = new BufferedImage(panelWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                                Graphics2D g2 = scaledImg.createGraphics();
                                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2.drawImage(original, 0, 0, panelWidth, newHeight, null);
                                g2.dispose();

                                label.setIcon(new ImageIcon(scaledImg));
                                label.setPreferredSize(new Dimension(panelWidth, newHeight));
                            }
                        }

                        pdfPanel.revalidate();
                        pdfPanel.repaint();
                        timer.restart();
                    });
                }
            });

            // Initial load
            SwingUtilities.invokeLater(loadVisiblePages);
        } 
         else if (name.endsWith(".c") || name.endsWith(".cpp") || name.endsWith(".py") || name.endsWith(".java")) {
            RSyntaxTextArea codeArea = new RSyntaxTextArea();
            codeArea.setEditable(false);
            codeArea.setCodeFoldingEnabled(true);

            if (name.endsWith(".c") || name.endsWith(".cpp")) {
                codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
            } else if (name.endsWith(".py")) {
                codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
            } else if (name.endsWith(".java")) {
                codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
            }
            
            codeArea.read(new InputStreamReader(stream), null);
            panel.add(new RTextScrollPane(codeArea), BorderLayout.CENTER); 

        } else {
            panel.add(new JLabel("Unsupported file type", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        setViewer(panel);  
    }
 
    
    public static ClassroomFileViewer getFileViewer() {
        
        if (fileViewer == null) {
            fileViewer = new ClassroomFileViewer();
        }
        return fileViewer;
    }

    // private BufferedImage scaleImage(BufferedImage originaImage, int maxWidth, int maxHeight){
    //     int width = originaImage.getWidth();
    //     int height = originaImage.getHeight();

    //     double scaleX = (double) maxWidth / width;
    //     double scaleY = (double) maxHeight / height;

    //     double scale = Math.min(scaleX, scaleY);

    //     int newWidth = (int) (width * scale);
    //     int newHeight = (int) (height * scale);

    //     Image scaledImage = originaImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    //     BufferedImage buferedScaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
    //     Graphics2D g2D = buferedScaledImage.createGraphics();
    //     g2D.drawImage(scaledImage, 0,0, null);
    //     g2D.dispose();
        
    //     return buferedScaledImage;
    // }
}