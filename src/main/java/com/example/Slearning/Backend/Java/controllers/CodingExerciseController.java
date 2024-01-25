package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.domain.entities.CodingExercise;
import com.example.Slearning.Backend.Java.domain.entities.TrackingCodingEx;
import com.example.Slearning.Backend.Java.services.CodingExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coding-exercise")
public class CodingExerciseController {
    @Autowired
    private CodingExerciseService codingExerciseService;

    @PutMapping("/tracking-coding-exercise/{trackId}/userCoding")
    public ResponseEntity<String> updateUserCoding(
            @PathVariable Integer trackId,
            @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(codingExerciseService.updateUserCoding(trackId, code));
    }

    @GetMapping("/{exId}/tracking-coding-exercise")
    public ResponseEntity<TrackingCodingEx> getTrackingExOfUser(
            @RequestParam("userId") UUID userId,
            @PathVariable Integer exId
    ) {
        return ResponseEntity.ok(codingExerciseService.getTrackingExOfUser(userId, exId));
    }

    @PutMapping("/tracking-coding-exercise/{trackId}/complete")
    public ResponseEntity<TrackingCodingEx> completeTracking(@PathVariable Integer trackId) {
        return ResponseEntity.ok(codingExerciseService.completeTracking(trackId));
    }

    @GetMapping("/{exId}")
    public ResponseEntity<CodingExercise> getById(@PathVariable Integer exId) {
        return ResponseEntity.ok(codingExerciseService.getById(exId));
    }

    @PostMapping
    public ResponseEntity<CodingExercise> createDraft(
            @RequestParam("chapterId") UUID chapterId,
            @RequestParam("title") String title
    ) {
        CodingExercise codingExercise = this.codingExerciseService.createDraft(chapterId, title);
        return new ResponseEntity<>(codingExercise, HttpStatus.CREATED);
    }

    @PutMapping("/{exId}/language")
    public ResponseEntity<Integer> addLanguage(
            @PathVariable Integer exId,
            @RequestParam("languageId") Integer languageId
    ) {
        Integer codingExercise = codingExerciseService.addLanguage(exId, languageId);
        return new ResponseEntity<>(codingExercise, HttpStatus.CREATED);
    }

    @PutMapping("/{exId}/author-solution")
    public ResponseEntity<CodingExercise> addAuthorSolution(
            @PathVariable Integer exId,
            @RequestParam("solution") String solution,
            @RequestParam("evaluation") String evaluation,
            @RequestParam("result") String result
    ) {
        CodingExercise codingExercise = codingExerciseService.addAuthorSolution(exId, solution, evaluation, result);
        return new ResponseEntity<>(codingExercise, HttpStatus.CREATED);
    }

    @PutMapping("/{exId}/relatedLecture")
    public ResponseEntity<UUID> addRelatedLecture(
            @PathVariable Integer exId,
            @RequestParam("lectureId") UUID lectureId
    ) {
        UUID createdHint = codingExerciseService.addRelatedLecture(exId, lectureId);
        return new ResponseEntity<>(createdHint, HttpStatus.CREATED);
    }

    @PutMapping("/{exId}/codeStarter")
    public ResponseEntity<String> addCodeStarter(
            @PathVariable Integer exId,
            @RequestParam("codeStarter") String codeStarter
    ) {
        String created = codingExerciseService.addCodeStarter(exId, codeStarter);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{exId}/hint")
    public ResponseEntity<String> addHint(
            @PathVariable Integer exId,
            @RequestParam("hint") String hint
    ) {
        String createdHint = codingExerciseService.addHint(exId, hint);
        return new ResponseEntity<>(createdHint, HttpStatus.CREATED);
    }

    @PutMapping("/{exId}/solutionExplanation")
    public ResponseEntity<String> addSolutionExplanation(
            @PathVariable Integer exId,
            @RequestParam("solutionExplanation") String solutionExplanation
    ) {
        String createdHint = codingExerciseService.addSolutionExplanation(exId, solutionExplanation);
        return new ResponseEntity<>(createdHint, HttpStatus.CREATED);
    }

    @PutMapping("/{exId}/instruction")
    public ResponseEntity<String> addInstruction(
            @PathVariable Integer exId,
            @RequestParam("instruction") String instruction
    ) {
        String createdHint = codingExerciseService.addInstruction(exId, instruction);
        return new ResponseEntity<>(createdHint, HttpStatus.CREATED);
    }

    @PutMapping("/{exId}/evaluation")
    public ResponseEntity<String> addEvaluation(
            @PathVariable Integer exId,
            @RequestParam("evaluation") String evaluation
    ) {
        String createdHint = codingExerciseService.addEvaluation(exId, evaluation);
        return new ResponseEntity<>(createdHint, HttpStatus.CREATED);
    }

    @PutMapping("/{exId}/publish")
    public ResponseEntity<CodingExercise> publishCodingExercise(
            @PathVariable Integer exId
    ) {
        CodingExercise codingExercise = codingExerciseService.publishCodingExercise(exId);
        return ResponseEntity.ok(codingExercise);
    }

    @GetMapping("/lecture")
    public ResponseEntity<List<CodingExercise>> getByLecture(
            @RequestParam("lectureId") UUID lectureId
    ) {
        return ResponseEntity.ok(codingExerciseService.getByLecture(lectureId));
    }

    @GetMapping
    public ResponseEntity<List<CodingExercise>> getByCourse(
            @RequestParam("chapterId") UUID chapterId
    ) {
        return ResponseEntity.ok(codingExerciseService.getByChapter(chapterId));
    }
}
