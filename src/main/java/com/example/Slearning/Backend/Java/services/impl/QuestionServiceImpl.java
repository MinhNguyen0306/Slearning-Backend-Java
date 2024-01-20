package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.entities.*;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.*;
import com.example.Slearning.Backend.Java.services.ProgressService;
import com.example.Slearning.Backend.Java.services.QuestionService;
import com.example.Slearning.Backend.Java.utils.enums.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final ChapterRepository chapterRepository;

    private final QuestionRepository questionRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;


    private final ProgressService progressService;

    private final AnswerRepository answerRepository;

    @Override
    public Question getQuestionById(UUID questionId) {
        Question question = this.questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "Id", questionId));
        return question;
    }

    @Override
    public Answer getAnswerById(UUID answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer", "Id", answerId));
        return answer;
    }

    @Override
    public Question createQuestion(
            UUID chapterId,
            String questionTitle,
            QuestionType questionType,
            List<String> answersTitle
    ) {
        Chapter chapter = this.chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterId.toString()));

        if(questionTitle == "" || answersTitle.stream().anyMatch(answer -> answer == "")) {
            throw new IllegalStateException("Khong duoc de trong");
        }

        if(answersTitle.size() < 2 && (questionType.equals(QuestionType.MULTIPLE_CHOICE) || questionType.equals(QuestionType.SINGLE_CHOICE))) {
            throw new IllegalStateException("Phai co it nhat 2 cau tra loiyy");
        }

        Question question = new Question();
        question.setQuestion(questionTitle);
        question.setChapter(chapter);
        question.setQuestionType(questionType);

        List<Answer> answers = new ArrayList<>();
        if(questionType.equals(QuestionType.SHORT_ANSWER)) {
            for(String title: answersTitle) {
                Answer answer = new Answer();
                answer.setAnswer(title);
                answer.setQuestion(question);
                answer.setCorrect(true);
                answers.add(answer);
            }
        } else {
            for(String title: answersTitle) {
                Answer answer = new Answer();
                answer.setAnswer(title);
                answer.setQuestion(question);
                answer.setCorrect(false);
                answers.add(answer);
            }
            answers.get(0).setCorrect(true);
            if(questionType.equals(QuestionType.MULTIPLE_CHOICE)) {
                answers.get(1).setCorrect(true);
            }
        }

        question.setAnswers(answers);
        Question createdQuestion = this.questionRepository.save(question);
        return createdQuestion;
    }

    @Override
    public Question editQuestion(UUID questionId, String question) {
        Question questionEditing = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "Id", questionId));
        questionEditing.setQuestion(question);
        return questionRepository.save(questionEditing);
    }

    @Override
    public Answer createAnswer(UUID questionId, String answerTitle) {
        Question question = this.questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "Id", questionId.toString()));

        Answer answer = new Answer();
        answer.setAnswer(answerTitle);
        answer.setQuestion(question);
        if(question.getQuestionType().equals(QuestionType.SHORT_ANSWER)) {
            answer.setCorrect(true);
        } else {
            answer.setCorrect(false);
        }
        return this.answerRepository.save(answer);
    }

    @Override
    public Answer editAnswer(UUID answerId, String answer) {
        Answer answerEditing = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer", "Id", answerId));
        answerEditing.setAnswer(answer);
        return answerRepository.save(answerEditing);
    }

    @Override
    public void deleteQuestion(UUID questionId) {
        Question question = this.questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "Id", questionId.toString()));

        questionRepository.delete(question);
    }

    @Override
    public ApiResponse deleteAnswer(UUID answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer", "Id", answerId.toString()));
        Question question = answer.getQuestion();

        if(question.getAnswers().size() == 2) {
            return ApiResponse.builder()
                    .message("Phai co toi thieu 2 cau tra loi")
                    .status("400")
                    .build();
        } else {
            if(answer.isCorrect()) {
                question.getAnswers().get(0).setCorrect(true);
                this.questionRepository.save(question);
            }

            this.answerRepository.delete(answer);

            return ApiResponse.builder()
                    .message("Da xoa cau tra loi")
                    .status("200")
                    .build();
        }
    }

    @Override
    public Answer chooseCorrectAnswer(UUID answerId) {
        Answer answer = this.answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer", "Id", answerId.toString()));
        Question question = answer.getQuestion();
        switch (question.getQuestionType()) {
            case SINGLE_CHOICE -> {
                if(answer.isCorrect()) {
                    return answer;
                } else {
                    Optional<Answer> answerOptional = question.getAnswers().stream()
                            .filter(answer1 -> answer1.isCorrect())
                            .findFirst();
                    if(answerOptional.isPresent()) {
                        Answer answer1 = answerOptional.get();
                        answer1.setCorrect(false);
                        this.answerRepository.save(answer1);
                    }
                    answer.setCorrect(true);
                    return this.answerRepository.save(answer);
                }
            }
            case MULTIPLE_CHOICE -> {
                if(answer.isCorrect()) {
                    int checkHaveTwoCorrect = question.getAnswers().stream()
                                    .filter(answer1 -> answer1.isCorrect())
                            .collect(Collectors.toList()).size();
                    if(checkHaveTwoCorrect >= 2) {
                        answer.setCorrect(false);
                        return this.answerRepository.save(answer);
                    } else {
                        return answer;
                    }
                } else {
                    answer.setCorrect(true);
                    return this.answerRepository.save(answer);
                }
            }
        }
        return answer;
    }

    @Override
    public boolean checkCorrectAnswer(UUID questionId, List<UUID> answerIds, String shortAnswer) {
        Question question = this.questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "Id", questionId));
        if(question.getQuestionType().equals(QuestionType.SINGLE_CHOICE)) {
            boolean checkCorrect = question.getAnswers().stream()
                    .anyMatch(answer -> answer.getId().equals(answerIds.get(0)) && answer.isCorrect());
            return checkCorrect;
        } else if(question.getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
            for(UUID answerId : answerIds) {
                boolean checkCorrect = question.getAnswers().stream()
                        .anyMatch(answer -> answer.getId().equals(answerId) && answer.isCorrect());
                if(checkCorrect == false) {
                    return false;
                }
            }
            int numberAnswerCorrect = question.getAnswers().stream()
                    .filter(answer -> answer.isCorrect())
                    .collect(Collectors.toList())
                    .size();
            if(numberAnswerCorrect != answerIds.size()) {
                return false;
            } else {
                return true;
            }
        } else {
            if(shortAnswer != null) {
                boolean checkCorrect = question.getAnswers().stream()
                        .anyMatch(answer -> answer.getAnswer().equalsIgnoreCase(shortAnswer.trim()));
                return checkCorrect;
            } else {
                throw new IllegalStateException("Bạn chưa nhập câu trả lời!");
            }
        }
    }
}
