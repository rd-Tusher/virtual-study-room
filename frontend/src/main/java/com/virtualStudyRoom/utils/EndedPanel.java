// package com.virtualStudyRoom.utils;



// import javafx.geometry.Pos;
// import javafx.scene.control.Label;
// import javafx.scene.layout.VBox;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;


// public class Ended {
//     public VBox getEndSession(){
//         Label header = headerTitle("The session has already ended", Color.RED,30);
//         Label subHeader = headerTitle("Try in another session", Color.BLUE,25);
//         VBox titleBox = new VBox(10,header,subHeader);
//         titleBox.setAlignment(Pos.CENTER);
//         titleBox.setStyle("-fx-background-color: black");
//         return titleBox;
//     }
    
//     public  static Label headerTitle(String text,Color color,int size){
//         Label label = new Label(text);
//         label.setFont(Font.font("Arial",size));
//         label.setTextFill(color);
//         return label;
//     }
// }


package com.virtualStudyRoom.utils;

import javax.swing.*;
import java.awt.*;

public class EndedPanel extends JPanel {

    public EndedPanel() {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout()); // perfect centering

        JLabel header = createLabel(
                "The session has already ended",
                Color.RED,
                30,
                Font.BOLD
        );

        JLabel subHeader = createLabel(
                "Try in another session",
                Color.BLUE,
                25,
                Font.PLAIN
        );

        JPanel box = new JPanel();
        box.setBackground(Color.BLACK);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(header);
        box.add(Box.createVerticalStrut(10));
        box.add(subHeader);

        add(box);
    }

    private JLabel createLabel(String text, Color color, int size, int style) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", style, size));
        return label;
    }
}
