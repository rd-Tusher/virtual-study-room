// package com.virtualStudyRoom.utils;

// import com.virtualStudyRoom.utils.ResponseModel.SessionCheckModel;

// import javafx.animation.KeyFrame;
// import javafx.animation.Timeline;
// import javafx.geometry.Pos;
// import javafx.scene.control.Label;
// import javafx.scene.layout.VBox;
// import javafx.scene.layout.HBox;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;
// import javafx.util.Duration;

// public class AnimatedTimerApp {

//     // Example: 2 days, 5 hours, 30 minutes, 20 seconds
//     private long remainingSeconds = 0;

//     private Label daysLabel = createTimeLabel();
//     private Label hoursLabel = createTimeLabel();
//     private Label minutesLabel = createTimeLabel();
//     private Label secondsLabel = createTimeLabel();

//     public VBox getTimerVBox(SessionCheckModel response) {
//         this.remainingSeconds = response.remainingSeconds;
//         Label daysText = createUnitLabel("DAYS");
//         Label hoursText = createUnitLabel("HOURS");
//         Label minutesText = createUnitLabel("MINUTES");
//         Label secondsText = createUnitLabel("SECONDS");

//         VBox daysBox = createTimeBox(daysLabel, daysText);
//         VBox hoursBox = createTimeBox(hoursLabel, hoursText);
//         VBox minutesBox = createTimeBox(minutesLabel, minutesText);
//         VBox secondsBox = createTimeBox(secondsLabel, secondsText);

//         Label title = new Label("The session is not started yet.");
//         title.setFont(Font.font("Arial", 30));
//         title.setTextFill(Color.WHITE);
        
//         Label subTitle = new Label("Remaining time to start");
//         subTitle.setFont(Font.font("Arial", 15));
//         subTitle.setTextFill(Color.WHITE);
        
//         VBox header = new VBox(20,title,subTitle);
//         header.setAlignment(Pos.CENTER);

//         HBox timerBox = new HBox(20, daysBox, hoursBox, minutesBox, secondsBox);
//         timerBox.setAlignment(Pos.CENTER);

//         startTimer();

//         VBox container = new VBox(50,header,timerBox);
//         container.setStyle("-fx-background-color : black");
//         container.setAlignment(Pos.CENTER);
//         return container;
//     }

//     private void startTimer() {
//         updateAllLabels();

//         Timeline timeline = new Timeline(
//             new KeyFrame(Duration.seconds(1), e -> {
//                 if (remainingSeconds > 0) {
//                     remainingSeconds--;
//                     updateAllLabels();
//                 }
//             })
//         );
//         timeline.setCycleCount(Timeline.INDEFINITE);
//         timeline.play();
//     }

//     private void updateAllLabels() {
//         long days = remainingSeconds / 86400;
//         long hours = (remainingSeconds % 86400) / 3600;
//         long minutes = (remainingSeconds % 3600) / 60;
//         long seconds = remainingSeconds % 60;

//         daysLabel.setText(String.format("%02d", days));
//         hoursLabel.setText(String.format("%02d", hours));
//         minutesLabel.setText(String.format("%02d", minutes));
//         secondsLabel.setText(String.format("%02d", seconds));
//     }

//     private Label createTimeLabel() {
//         Label label = new Label("00");
//         label.setFont(Font.font("Arial", 50));
//         label.setTextFill(Color.WHITE);
//         return label;
//     }

//     private Label createUnitLabel(String text) {
//         Label label = new Label(text);
//         label.setFont(Font.font("Arial", 12));
//         label.setTextFill(Color.GRAY);
//         return label;
//     }

//     private VBox createTimeBox(Label time, Label unit) {
//         VBox box = new VBox(5, time, unit);
//         box.setAlignment(Pos.CENTER);
//         return box;
//     }
// }



package com.virtualStudyRoom.utils;

import com.virtualStudyRoom.utils.ResponseModel.SessionCheckModel;

import javax.swing.*;
import java.awt.*;

public class AnimatedTimerPanel extends JPanel {

    private long remainingSeconds;

    private JLabel daysLabel;
    private JLabel hoursLabel;
    private JLabel minutesLabel;
    private JLabel secondsLabel;

    private Timer timer;

    public AnimatedTimerPanel(SessionCheckModel response) {
        this.remainingSeconds = response.remainingSeconds;

        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createTimerBox(), BorderLayout.CENTER);

        startTimer();
    }

    // ---------------- Header ----------------
    private JPanel createHeader() {
        JLabel title = new JLabel("The session is not started yet.");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitle = new JLabel("Remaining time to start");
        subTitle.setFont(new Font("Arial", Font.PLAIN, 15));
        subTitle.setForeground(Color.LIGHT_GRAY);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel header = new JPanel();
        header.setBackground(Color.BLACK);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        header.add(title);
        header.add(Box.createVerticalStrut(15));
        header.add(subTitle);

        return header;
    }

    // ---------------- Timer UI ----------------
    private JPanel createTimerBox() {
        daysLabel = createTimeLabel();
        hoursLabel = createTimeLabel();
        minutesLabel = createTimeLabel();
        secondsLabel = createTimeLabel();

        JPanel timerBox = new JPanel(new GridLayout(1, 4, 40, 0));
        timerBox.setBackground(Color.BLACK);

        timerBox.add(createTimeUnit(daysLabel, "DAYS"));
        timerBox.add(createTimeUnit(hoursLabel, "HOURS"));
        timerBox.add(createTimeUnit(minutesLabel, "MINUTES"));
        timerBox.add(createTimeUnit(secondsLabel, "SECONDS"));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.BLACK);
        wrapper.add(timerBox);

        return wrapper;
    }

    private JPanel createTimeUnit(JLabel value, String unit) {
        JLabel unitLabel = new JLabel(unit);
        unitLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        unitLabel.setForeground(Color.GRAY);
        unitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel box = new JPanel();
        box.setBackground(Color.BLACK);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        value.setAlignmentX(Component.CENTER_ALIGNMENT);
        unitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(value);
        box.add(Box.createVerticalStrut(5));
        box.add(unitLabel);

        return box;
    }

    private JLabel createTimeLabel() {
        JLabel label = new JLabel("00");
        label.setFont(new Font("Arial", Font.BOLD, 50));
        label.setForeground(Color.WHITE);
        return label;
    }

    // ---------------- Timer Logic ----------------
    private void startTimer() {
        updateLabels();

        timer = new Timer(1000, e -> {
            if (remainingSeconds > 0) {
                remainingSeconds--;
                updateLabels();
            } else {
                timer.stop();
            }
        });
        timer.start();
    }

    private void updateLabels() {
        long days = remainingSeconds / 86400;
        long hours = (remainingSeconds % 86400) / 3600;
        long minutes = (remainingSeconds % 3600) / 60;
        long seconds = remainingSeconds % 60;

        daysLabel.setText(String.format("%02d", days));
        hoursLabel.setText(String.format("%02d", hours));
        minutesLabel.setText(String.format("%02d", minutes));
        secondsLabel.setText(String.format("%02d", seconds));
    }
}
