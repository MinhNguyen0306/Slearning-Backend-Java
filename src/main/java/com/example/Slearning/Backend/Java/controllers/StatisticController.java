package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.domain.responses.Top5CourseResponse;
import com.example.Slearning.Backend.Java.domain.responses.Top5RateResponse;
import com.example.Slearning.Backend.Java.domain.responses.Top5TopicResponse;
import com.example.Slearning.Backend.Java.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/statistic")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/{mentorId}/top-5-course")
    public ResponseEntity<Top5CourseResponse> statisticTop5Course(@PathVariable UUID mentorId) {
        return ResponseEntity.ok(statisticService.statisticTop5Course(mentorId));
    }

    @GetMapping("/{mentorId}/top-5-rate")
    public ResponseEntity<Top5RateResponse> statisticTop5Rate(@PathVariable UUID mentorId) {
        return ResponseEntity.ok(statisticService.statisticTop5Rate(mentorId));
    }

    @GetMapping("/{mentorId}/top-5-topic")
    public ResponseEntity<Top5TopicResponse> statisticTop5Topic(@PathVariable UUID mentorId) {
        return ResponseEntity.ok(statisticService.statisticTop5Topic(mentorId));
    }
}
