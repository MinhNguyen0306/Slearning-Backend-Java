package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.entities.Course;
import com.example.Slearning.Backend.Java.domain.entities.Payment;
import com.example.Slearning.Backend.Java.domain.entities.Topic;
import com.example.Slearning.Backend.Java.domain.entities.User;
import com.example.Slearning.Backend.Java.domain.responses.Top5CourseResponse;
import com.example.Slearning.Backend.Java.domain.responses.Top5RateResponse;
import com.example.Slearning.Backend.Java.domain.responses.Top5TopicResponse;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.CourseRepository;
import com.example.Slearning.Backend.Java.repositories.PaymentRepository;
import com.example.Slearning.Backend.Java.repositories.TopicRepository;
import com.example.Slearning.Backend.Java.repositories.UserRepository;
import com.example.Slearning.Backend.Java.services.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;

    private final TopicRepository topicRepository;

    private final CourseRepository courseRepository;

    @Override
    public List<Payment> statisticTurnOverMentor(UUID mentorId) {
        userRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", mentorId));
        return paymentRepository.getPaymentsOfMentor(mentorId);
    }

    @Override
    public Top5CourseResponse statisticTop5Course(UUID mentorId) {
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor", "Id", mentorId));
        List<Course> courses = mentor.getCourses();
        List<Course> top5Course = courses.stream()
                .sorted((course1, course2) -> {
                    if(course1.getPayments().size() > course2.getPayments().size()) {
                        return 1;
                    } else if(course1.getPayments().size() < course2.getPayments().size()) {
                        return -1;
                    } else {
                        return 0;
                    }
                })
                .limit(5)
                .collect(Collectors.toList());
        Top5CourseResponse response = new Top5CourseResponse();
        response.setCourses(top5Course);
        for(Course course : top5Course) {
            int sum = 0;
            for(Payment payment: course.getPayments()) {
                sum += payment.getAmount();
            }
            response.getTurnoverOfCourses().add(sum);
        }
        return response;
    }

    @Override
    public Top5TopicResponse statisticTop5Topic(UUID mentorId) {
        userRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", mentorId));
        List<String> top5Topic = topicRepository.top5Topic(mentorId);
        Top5TopicResponse response = new Top5TopicResponse();
        response.setTopics(top5Topic);
        response.setNumberCourse(topicRepository.numberCourseSold(mentorId));
        return response;
    }

    @Override
    public Top5RateResponse statisticTop5Rate(UUID mentorId) {
        userRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor", "Id", mentorId));
        List<String> top5Rate = courseRepository.top5Rate(mentorId);
        Top5RateResponse response = new Top5RateResponse();
        response.setCourses(top5Rate);
        response.setRateCourse(courseRepository.top5RateNumberRate(mentorId));
        return response;
    }
}
