package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.domain.entities.Answer;
import com.example.Slearning.Backend.Java.domain.entities.Question;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.services.QuestionService;
import com.example.Slearning.Backend.Java.utils.enums.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PatchMapping("/{questionId}/add/explanation")
    public ResponseEntity<String> addQuestionExplanation(
            @PathVariable UUID questionId,
            @RequestParam("explanation") String explanation
    ) {
        return ResponseEntity.ok(questionService.addQuestionExplanation(questionId, explanation));
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<Question> getQuestionById(@PathVariable UUID questionId) {
        Question question = this.questionService.getQuestionById(questionId);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/answers/{answerId}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable UUID answerId) {
        Answer answer = this.questionService.getAnswerById(answerId);
        return ResponseEntity.ok(answer);
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(
            @RequestParam("chapterId") UUID chapterId,
            @RequestParam("question") String question,
            @RequestParam("questionType") QuestionType questionType,
            @RequestParam("answers") List<String> answers
    ) {
        Question createdQuestion = questionService.createQuestion(chapterId, question, questionType, answers);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @PatchMapping("/{questionId}")
    public ResponseEntity<Question> editQuestion(
            @PathVariable("questionId") UUID questionId,
            @RequestParam("question") String question
    ) {
        Question editedQuestion = questionService.editQuestion(questionId, question);
        return new ResponseEntity<>(editedQuestion, HttpStatus.CREATED);
    }

    @PostMapping("/{questionId}/answers")
    public ResponseEntity<Answer> createAnswer(
            @PathVariable UUID questionId,
            @RequestParam("answer") String answer
    ) {
        Answer createdAnswer = questionService.createAnswer(questionId, answer);
        return new ResponseEntity<>(createdAnswer, HttpStatus.CREATED);
    }

    @PatchMapping("/answers/{answerId}")
    public ResponseEntity<Answer> editAnswer(
            @PathVariable UUID answerId,
            @RequestParam("answer") String answer
    ) {
        Answer createdAnswer = questionService.editAnswer(answerId, answer);
        return new ResponseEntity<>(createdAnswer, HttpStatus.CREATED);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable UUID questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<ApiResponse> deleteAnswer(@PathVariable UUID answerId) {
        ApiResponse response = questionService.deleteAnswer(answerId);
        if(response.getStatus().equals("400")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/answers/{answerId}")
    public ResponseEntity<Answer> chooseCorrectAnswer(@PathVariable UUID answerId) {
        Answer updatedAnswer = questionService.chooseCorrectAnswer(answerId);
        return new ResponseEntity<>(updatedAnswer, HttpStatus.CREATED);
    }

    @GetMapping("/check-correct")
    public ResponseEntity<Boolean> checkCorrectAnswer(
            @RequestParam("questionId") UUID questionId,
            @RequestParam(value = "answerIds", required = false) List<UUID> answerIds,
            @RequestParam(value = "shortAnswer", required = false) String shortAnswer
    ) {
        boolean checkCorrect = this.questionService.checkCorrectAnswer(questionId, answerIds, shortAnswer);
        return ResponseEntity.ok(checkCorrect);
    }
}
