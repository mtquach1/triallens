package com.triallens.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_chunks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    private String sectionTitle;
    private Integer pageNumber;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String chunkText;

    // Vector field for pgvector similarity searches
    @Column(name = "embedding", columnDefinition = "vector(1536)")
    private String embedding;
}