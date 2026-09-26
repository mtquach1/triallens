package com.triallens.backend.controller;

import com.triallens.backend.model.DocumentChunk;
import com.triallens.backend.repository.DocumentChunkRepository;
import com.triallens.backend.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository documentChunkRepository;

    @GetMapping
    public ResponseEntity<List<DocumentChunk>> searchChunks(
            @RequestParam("studyId") Long studyId,
            @RequestParam("query") String query,
            @RequestParam(value = "topK", defaultValue = "3") int topK) {

        // 1. Convert search query into vector space
        String queryVector = embeddingService.generateEmbedding(query);

        // 2. Query pgvector using cosine distance
        List<DocumentChunk> results = documentChunkRepository.findSimilarChunks(studyId, queryVector, topK);

        return ResponseEntity.ok(results);
    }
}