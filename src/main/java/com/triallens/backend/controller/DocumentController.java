package com.triallens.backend.controller;

import com.triallens.backend.model.Document;
import com.triallens.backend.model.DocumentChunk;
import com.triallens.backend.model.Study;
import com.triallens.backend.repository.DocumentChunkRepository;
import com.triallens.backend.repository.DocumentRepository;
import com.triallens.backend.repository.StudyRepository;
import com.triallens.backend.service.EmbeddingService;
import com.triallens.backend.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final PdfService pdfService;
    private final EmbeddingService embeddingService;
    private final StudyRepository studyRepository;
    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("studyId") Long studyId,
            @RequestParam("file") MultipartFile file) {
        
        try {
            Study study = studyRepository.findById(studyId)
                    .orElseThrow(() -> new IllegalArgumentException("Study not found with id: " + studyId));

            Document document = Document.builder()
                    .study(study)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .build();

            document = documentRepository.save(document);

            // 1. Extract text chunks
            List<String> chunks = pdfService.extractAndChunkText(file, 1000);

            // 2. Generate vector embeddings and save chunks
            for (int i = 0; i < chunks.size(); i++) {
                String chunkText = chunks.get(i);
                String vectorEmbedding = embeddingService.generateEmbedding(chunkText);

                DocumentChunk chunk = DocumentChunk.builder()
                        .document(document)
                        .chunkText(chunkText)
                        .pageNumber(i + 1)
                        .embedding(vectorEmbedding)
                        .build();

                documentChunkRepository.save(chunk);
            }

            return ResponseEntity.ok("Successfully uploaded and processed " + chunks.size() + " chunks with embeddings.");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        }
    }
}