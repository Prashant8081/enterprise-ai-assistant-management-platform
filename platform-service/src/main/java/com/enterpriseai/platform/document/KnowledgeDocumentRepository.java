package com.enterpriseai.platform.document;

import java.util.List;
import java.util.UUID;

import com.enterpriseai.platform.assistant.AiAssistant;
import com.enterpriseai.platform.tenant.Tenant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocument, UUID> {

    List<KnowledgeDocument> findByTenantAndAssistantOrderByCreatedAtDesc(Tenant tenant, AiAssistant assistant);
}
