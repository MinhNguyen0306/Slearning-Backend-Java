package com.example.Slearning.Backend.Java.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticResponse {
    private int mentorAmount;
    private int leanerAmount;
    private int courseAmount;
    private int enrollAmount;
}
