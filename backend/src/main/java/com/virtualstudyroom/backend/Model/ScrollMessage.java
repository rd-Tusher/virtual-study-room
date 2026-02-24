package com.virtualstudyroom.backend.Model;

import lombok.Data;

@Data
public class ScrollMessage {

    private String senderID;
    private String verticalPercent;
    // private String horizontalPercent;

    public ScrollMessage(){}
    public ScrollMessage(String senderID, String verticalPercent){
        this.senderID = senderID;
        // this.horizontalPercent = horizontalPercentage;
        this.verticalPercent = verticalPercent;
    }
}
