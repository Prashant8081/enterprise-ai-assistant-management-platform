package com.enterpriseai.platform.audit;

import java.util.UUID;

import com.enterpriseai.platform.common.BaseEntity;
import com.enterpriseai.platform.tenant.Tenant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_logs")
public class AuditLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(nullable = false)
    private UUID assistantId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, columnDefinition = "text")
    private String prompt;

    @Column(nullable = false, columnDefinition = "text")
    private String answer;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private int retrievedChunks;

    @Column(nullable = false)
    private long latencyMs;

    protected AuditLog() {
    }

    public AuditLog(Tenant tenant,
                    UUID assistantId,
                    UUID userId,
                    String prompt,
                    String answer,
                    String modelName,
                    int retrievedChunks,
                    long latencyMs) {
        this.tenant = tenant;
        this.assistantId = assistantId;
        this.userId = userId;
        this.prompt = prompt;
        this.answer = answer;
        this.modelName = modelName;
        this.retrievedChunks = retrievedChunks;
        this.latencyMs = latencyMs;
    }

    public UUID getAssistantId() {
        return assistantId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getModelName() {
        return modelName;
    }

    public int getRetrievedChunks() {
        return retrievedChunks;
    }

    public long getLatencyMs() {
        return latencyMs;
    }
}
