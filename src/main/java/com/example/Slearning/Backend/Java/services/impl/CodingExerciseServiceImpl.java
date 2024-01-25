package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.entities.*;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.*;
import com.example.Slearning.Backend.Java.services.CodingExerciseService;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CodingExerciseServiceImpl implements CodingExerciseService {

    private final CodingExerciseRepository codingExerciseRepository;

    private final UserRepository userRepository;

    private final ChapterRepository chapterRepository;

    private final TrackingCodingExRepository trackingCodingExRepository;

    private final LectureRepository lectureRepository;

    public String updateUserCoding(Integer trackId, String code) {
        TrackingCodingEx trackingCodingEx = trackingCodingExRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("TrackingCodingEx", "Id", trackId));
        trackingCodingEx.setUserCoding(code);
        TrackingCodingEx updatedTracking = trackingCodingExRepository.save(trackingCodingEx);
        return updatedTracking.getUserCoding();
    }

    @Override
    public TrackingCodingEx getTrackingExOfUser(UUID userId, Integer exId) {
        TrackingCodingEx trackingCodingEx = trackingCodingExRepository.getByUserAndEx(userId, exId)
                .orElseThrow(() -> new ResourceNotFoundException("UserId and ExId", "Id", exId));
        return trackingCodingEx;
    }

    @Override
    public TrackingCodingEx completeTracking(Integer trackId) {
        TrackingCodingEx trackingCodingEx = trackingCodingExRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("TrackingCodingEx", "Id", trackId));
        if(trackingCodingEx.isCompleted()) {
            return trackingCodingEx;
        } else {
            trackingCodingEx.setCompleted(true);
            TrackingCodingEx updatedTracking = trackingCodingExRepository.save(trackingCodingEx);
            return updatedTracking;
        }
    }

    @Override
    public CodingExercise createDraft(UUID chapterId, String title) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterId));
        CodingExercise codingExercise = new CodingExercise();
        codingExercise.setTitle(title);
        codingExercise.setChapter(chapter);
        codingExercise.setPublishStatus(PublishStatus.UN_PUBLISHING);
        return codingExerciseRepository.save(codingExercise);
    }

    @Override
    public Integer addLanguage(Integer exId, Integer languageId) {
        CodingExercise codingExercise = codingExerciseRepository.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException("Coding Exercise", "Id", exId));
        codingExercise.setLanguageId(languageId);
        codingExerciseRepository.save(codingExercise);
        return languageId;
    }

    @Override
    public UUID addRelatedLecture(Integer exId, UUID lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        CodingExercise codingExercise = codingExerciseRepository.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException("Coding Exercise", "Id", exId));
        codingExercise.setLecture(lecture);
        codingExerciseRepository.save(codingExercise);
        return lectureId;
    }

    @Override
    public String addCodeStarter(Integer exId, String codeStarter) {
        CodingExercise codingExercise = codingExerciseRepository.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException("Coding Exercise", "Id", exId));
        codingExercise.setCodeStarter(codeStarter);
        codingExerciseRepository.save(codingExercise);
        return codeStarter;
    }

    @Override
    public CodingExercise addAuthorSolution(Integer exId, String solution, String evaluation, String result) {
        CodingExercise codingExercise = codingExerciseRepository.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException("Coding Exercise", "Id", exId));
        codingExercise.setSolution(solution);
        codingExercise.setEvaluation(evaluation);
        codingExercise.setResult(result);
        return codingExerciseRepository.save(codingExercise);
    }

    @Override
    public String addHint(Integer exId, String hint) {
        CodingExercise codingExercise = codingExerciseRepository.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException("Coding Exercise", "Id", exId));
        codingExercise.setHint(hint);
        codingExerciseRepository.save(codingExercise);
        return hint;
    }

    @Override
    public String addSolutionExplanation(Integer exId, String solutionExplanation) {
        CodingExercise codingExercise = codingExerciseRepository.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException("Coding Exercise", "Id", exId));
        codingExercise.setSolutionExplanation(solutionExplanation);
        codingExerciseRepository.save(codingExercise);
        return solutionExplanation;
    }

    @Override
    public String addInstruction(Integer exId, String instruction) {
        CodingExercise codingExercise = codingExerciseRepository.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException("Coding Exercise", "Id", exId));
        codingExercise.setInstruction(instruction);
        codingExerciseRepository.save(codingExercise);
        return instruction;
    }

    @Override
    public String addEvaluation(Integer exId, String evaluation) {
        CodingExercise codingExercise = codingExerciseRepository.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException("Coding Exercise", "Id", exId));
        codingExercise.setEvaluation(evaluation);
        codingExerciseRepository.save(codingExercise);
        return evaluation;
    }

    @Override
    public CodingExercise publishCodingExercise(Integer exId) {
        CodingExercise codingExercise = codingExerciseRepository.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException("Coding Exercise", "Id", exId));
        if(codingExercise.getSolution() == null || codingExercise.getInstruction() == null
        || codingExercise.getLecture() == null || codingExercise.getLanguageId() == null
        || codingExercise.getResult() == null) {
            throw new IllegalStateException("Chua du dieu kien de xuat ban");
        }
        codingExercise.setPublishStatus(PublishStatus.PUBLISHING);
        return codingExerciseRepository.save(codingExercise);
    }

    @Override
    public List<CodingExercise> getByLecture(UUID lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        return lecture.getCodingExercises();
    }

    @Override
    public List<CodingExercise> getByChapter(UUID chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterId));
        return chapter.getCodingExercises();
    }

    @Override
    public CodingExercise getById(Integer exId) {
        CodingExercise codingExercise = codingExerciseRepository.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException("CodingExercise", "Id", exId));
        return codingExercise;
    }
}
