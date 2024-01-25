package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.entities.Answer;
import com.example.Slearning.Backend.Java.domain.entities.Progress;
import com.example.Slearning.Backend.Java.domain.entities.Question;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.utils.enums.QuestionType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface QuestionService {
    String addQuestionExplanation(UUID questionId, String explanation);
    Question getQuestionById(UUID questionId);
    Answer getAnswerById(UUID answerId);
    Question createQuestion(
            UUID chapterId,
            String question,
            QuestionType questionType,
            List<String> answers
    );
    Question editQuestion(UUID questionId, String question);
    Answer createAnswer(UUID questionId, String answer);
    Answer editAnswer(UUID answerId, String answer);

    void deleteQuestion(UUID questionId);

    ApiResponse deleteAnswer(UUID answerId);

    Answer chooseCorrectAnswer(UUID answerId);

    boolean checkCorrectAnswer(UUID questionId, List<UUID> answerIds, String shortAnswer);

}
