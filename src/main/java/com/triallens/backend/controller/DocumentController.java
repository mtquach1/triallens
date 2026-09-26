package com.triallens.backend.controller;

import com.triallens.backend.model.Document;
import com.triallens.backend.model.DocumentChunk;
import com.triallens.backend.model.Study;
import com.triallens.backend.repository.DocumentChunkRepository;
import com.triallens.backend.repository.DocumentRepository;
import com.triallens.backend.repository.StudyRepository;
import com.triallens.backend.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final PdfService pdfService;
    private final StudyRepository studyRepository;
    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("studyId") Long studyId) {

        try {
            Study study = studyRepository.findById(studyId)
                    .orElseThrow(() -> new RuntimeException("Study not found"));

            Document document = Document.builder()
                    .filename(file.getOriginalFilename())
                    .version(1)
                    .study(study)
                    .build();
            documentRepository.save(document);

            // Extract text into 1000-character chunks
            List<String> textChunks = pdfService.extractAndChunkText(file, 1000);

            for (int i = 0; i < textChunks.size(); i++) {
                DocumentChunk chunk = DocumentChunk.builder()
                        .document(document)
                        .chunkText(textChunks.get(i))
                        .pageNumber(i + 1)
                        .build();
                documentChunkRepository.save(chunk);
            }

            return ResponseEntity.ok("Successfully uploaded and processed " + textChunks.size() + " chunks.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }
}