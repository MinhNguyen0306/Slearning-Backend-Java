package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.entities.CodingExercise;
import com.example.Slearning.Backend.Java.domain.entities.Lecture;
import com.example.Slearning.Backend.Java.domain.entities.TrackingCodingEx;

import java.util.List;
import java.util.UUID;

public interface CodingExerciseService {

    String updateUserCoding(Integer trackId, String code);

    TrackingCodingEx getTrackingExOfUser(UUID userId, Integer exId);

    TrackingCodingEx completeTracking(Integer trackId);


    CodingExercise createDraft(UUID chapterId, String title);

    Integer addLanguage(Integer exId, Integer languageId);

    UUID addRelatedLecture(Integer exId, UUID lectureId);

    String addCodeStarter(Integer exId, String codeStarter);

    CodingExercise addAuthorSolution(
            Integer exId,
            String solution,
            String evaluation,
            String result
    );

    String addHint(Integer exId, String hint);

    String addSolutionExplanation(Integer exId, String solutionExplanation);

    String addInstruction(Integer exId, String instruction);

    String addEvaluation(Integer exId, String evaluation);

    CodingExercise publishCodingExercise(Integer exId);

    List<CodingExercise> getByLecture(UUID lectureId);

    List<CodingExercise> getByChapter(UUID chapterId);

    CodingExercise getById(Integer exId);

}
