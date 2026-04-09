package com.virtualstudyroom.backend.Model;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LateUserInfo {
    @Builder.Default
    private List<StrokeDTO> strokeDTO = new CopyOnWriteArrayList<>();
    private int canvasHeight;
    private double verticalPercentage;
    private String userID;
}   