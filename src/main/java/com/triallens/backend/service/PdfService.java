package com.triallens.backend.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

    public List<String> extractAndChunkText(MultipartFile file, int chunkSize) throws IOException {
        List<String> chunks = new ArrayList<>();
        
        // Pass the raw byte array to PDFBox 3.x Loader
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String fullText = stripper.getText(document);

            // Basic chunking by character length
            int length = fullText.length();
            for (int i = 0; i < length; i += chunkSize) {
                int end = Math.min(length, i + chunkSize);
                chunks.add(fullText.substring(i, end).trim());
            }
        }
        return chunks;
    }
}