package com.triallens.backend.controller;

import com.triallens.backend.model.Study;
import com.triallens.backend.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyRepository studyRepository;

    @PostMapping
    public ResponseEntity<Study> createStudy(@RequestBody Study study) {
        return ResponseEntity.ok(studyRepository.save(study));
    }

    @GetMapping
    public ResponseEntity<List<Study>> getAllStudies() {
        return ResponseEntity.ok(studyRepository.findAll());
    }
}