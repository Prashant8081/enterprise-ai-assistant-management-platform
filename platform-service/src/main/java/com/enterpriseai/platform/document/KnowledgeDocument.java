package com.enterpriseai.platform.document;

import com.enterpriseai.platform.assistant.AiAssistant;
import com.enterpriseai.platform.common.BaseEntity;
import com.enterpriseai.platform.tenant.Tenant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "knowledge_documents")
public class KnowledgeDocument extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assistant_id", nullable = false)
    private AiAssistant assistant;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String sourceType;

    @Column(nullable = false)
    private String contentHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status = DocumentStatus.INDEXED;

    protected KnowledgeDocument() {
    }

    public KnowledgeDocument(Tenant tenant,
                             AiAssistant assistant,
                             String title,
                             String sourceType,
                             String contentHash) {
        this.tenant = tenant;
        this.assistant = assistant;
        this.title = title;
        this.sourceType = sourceType;
        this.contentHash = contentHash;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public AiAssistant getAssistant() {
        return assistant;
    }

    public String getTitle() {
        return title;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getContentHash() {
        return contentHash;
    }

    public DocumentStatus getStatus() {
        return status;
    }
}
