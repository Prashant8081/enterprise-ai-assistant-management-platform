package com.enterpriseai.platform.assistant;

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
@Table(name = "ai_assistants")
public class AiAssistant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false, length = 4000)
    private String systemPrompt;

    @Column(nullable = false)
    private String modelName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssistantStatus status = AssistantStatus.ACTIVE;

    protected AiAssistant() {
    }

    public AiAssistant(Tenant tenant, String name, String slug, String systemPrompt, String modelName) {
        this.tenant = tenant;
        this.name = name;
        this.slug = slug;
        this.systemPrompt = systemPrompt;
        this.modelName = modelName;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getModelName() {
        return modelName;
    }

    public AssistantStatus getStatus() {
        return status;
    }
}
