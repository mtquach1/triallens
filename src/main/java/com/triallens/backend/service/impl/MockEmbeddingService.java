package com.triallens.backend.service.impl;

import com.triallens.backend.service.EmbeddingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MockEmbeddingService implements EmbeddingService {

    private static final int VECTOR_DIMENSION = 1536;

    @Override
    public String generateEmbedding(String text) {
        // Seed random with text hash code so identical text generates identical vectors
        Random random = new Random(text.hashCode());
        StringBuilder sb = new StringBuilder("[");
        
        for (int i = 0; i < VECTOR_DIMENSION; i++) {
            double value = (random.nextDouble() * 2) - 1;
            sb.append(String.format("%.6f", value));
            if (i < VECTOR_DIMENSION - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public List<String> generateEmbeddings(List<String> texts) {
        List<String> embeddings = new ArrayList<>();
        for (String text : texts) {
            embeddings.add(generateEmbedding(text));
        }
        return embeddings;
    }
}