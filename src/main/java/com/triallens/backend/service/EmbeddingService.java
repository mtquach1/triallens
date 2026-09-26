package com.triallens.backend.service;

import java.util.List;

public interface EmbeddingService {
    /**
     * Generates a 1536-dimensional vector for the given text.
     * Returns the array formatted as a PostgreSQL vector string (e.g., "[0.012, -0.045, ...]").
     */
    String generateEmbedding(String text);

    /**
     * Batch embedding generation for efficiency.
     */
    List<String> generateEmbeddings(List<String> texts);
}