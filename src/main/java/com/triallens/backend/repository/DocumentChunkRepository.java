package com.triallens.backend.repository;

import com.triallens.backend.model.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {

    /**
     * Finds top K similar chunks within a Study using pgvector cosine distance (<=>).
     */
    @Query(value = """
            SELECT dc.*, (dc.embedding <=> cast(:queryVector as vector)) as distance
            FROM document_chunks dc
            JOIN documents d ON dc.document_id = d.id
            WHERE d.study_id = :studyId
            ORDER BY dc.embedding <=> cast(:queryVector as vector) ASC
            LIMIT :topK
            """, nativeQuery = true)
    List<DocumentChunk> findSimilarChunks(@Param("studyId") Long studyId, 
                                          @Param("queryVector") String queryVector, 
                                          @Param("topK") int topK);
}