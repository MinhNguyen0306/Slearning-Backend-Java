package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.entities.Payment;
import com.example.Slearning.Backend.Java.domain.responses.Top5CourseResponse;
import com.example.Slearning.Backend.Java.domain.responses.Top5RateResponse;
import com.example.Slearning.Backend.Java.domain.responses.Top5TopicResponse;

import java.util.List;
import java.util.UUID;

public interface StatisticService {
    List<Payment> statisticTurnOverMentor(UUID mentorId);
    Top5CourseResponse statisticTop5Course(UUID mentorId);
    Top5TopicResponse statisticTop5Topic(UUID mentorId);
    Top5RateResponse statisticTop5Rate(UUID mentorId);
}
