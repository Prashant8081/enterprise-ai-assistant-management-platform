package com.enterpriseai.platform.document;

import com.enterpriseai.platform.assistant.AiAssistant;
import com.enterpriseai.platform.common.BaseEntity;
import com.enterpriseai.platform.tenant.Tenant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "document_chunks")
public class DocumentChunk extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assistant_id", nullable = false)
    private AiAssistant assistant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false)
    private KnowledgeDocument document;

    @Column(nullable = false)
    private int chunkIndex;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private int tokenEstimate;

    @Column(name = "embedding", nullable = false, columnDefinition = "vector(64)")
    @ColumnTransformer(write = "?::vector")
    private String embedding;

    protected DocumentChunk() {
    }

    public DocumentChunk(Tenant tenant,
                         AiAssistant assistant,
                         KnowledgeDocument document,
                         int chunkIndex,
                         String content,
                         int tokenEstimate,
                         String embedding) {
        this.tenant = tenant;
        this.assistant = assistant;
        this.document = document;
        this.chunkIndex = chunkIndex;
        this.content = content;
        this.tokenEstimate = tokenEstimate;
        this.embedding = embedding;
    }

    public KnowledgeDocument getDocument() {
        return document;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public String getContent() {
        return content;
    }

    public int getTokenEstimate() {
        return tokenEstimate;
    }
}
