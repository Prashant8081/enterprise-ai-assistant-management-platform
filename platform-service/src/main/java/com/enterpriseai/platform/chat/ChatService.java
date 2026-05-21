package com.enterpriseai.platform.chat;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.enterpriseai.platform.assistant.AiAssistant;
import com.enterpriseai.platform.assistant.AssistantService;
import com.enterpriseai.platform.audit.AuditEvent;
import com.enterpriseai.platform.audit.AuditEventPublisher;
import com.enterpriseai.platform.audit.AuditLog;
import com.enterpriseai.platform.audit.AuditLogRepository;
import com.enterpriseai.platform.document.DocumentChunk;
import com.enterpriseai.platform.document.DocumentService;
import com.enterpriseai.platform.tenant.Tenant;
import com.enterpriseai.platform.user.UserAccount;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    private final AssistantService assistantService;
    private final DocumentService documentService;
    private final AiResponseGenerator responseGenerator;
    private final AuditLogRepository auditLogs;
    private final AuditEventPublisher auditEventPublisher;

    public ChatService(AssistantService assistantService,
                       DocumentService documentService,
                       AiResponseGenerator responseGenerator,
                       AuditLogRepository auditLogs,
                       AuditEventPublisher auditEventPublisher) {
        this.assistantService = assistantService;
        this.documentService = documentService;
        this.responseGenerator = responseGenerator;
        this.auditLogs = auditLogs;
        this.auditEventPublisher = auditEventPublisher;
    }

    @Transactional
    public ChatResponse chat(UserAccount user, UUID assistantId, ChatRequest request) {
        Tenant tenant = user.getTenant();
        AiAssistant assistant = assistantService.getForTenant(assistantId, tenant);
        Instant started = Instant.now();
        List<DocumentChunk> context = documentService.retrieve(tenant, assistant, request.message(), 4);
        String answer = responseGenerator.generate(assistant, request.message(), context);
        long latencyMs = Duration.between(started, Instant.now()).toMillis();

        AuditLog audit = auditLogs.save(new AuditLog(
            tenant,
            assistant.getId(),
            user.getId(),
            request.message(),
            answer,
            assistant.getModelName(),
            context.size(),
            latencyMs));

        auditEventPublisher.publish(AuditEvent.from(audit, tenant.getId()));

        return new ChatResponse(
            audit.getId(),
            answer,
            assistant.getModelName(),
            latencyMs,
            context.stream()
                .map(chunk -> new ChatResponse.Citation(
                    chunk.getDocument().getId(),
                    chunk.getDocument().getTitle(),
                    chunk.getChunkIndex(),
                    excerpt(chunk.getContent())))
                .toList());
    }

    private String excerpt(String content) {
        return content.length() <= 260 ? content : content.substring(0, 257) + "...";
    }
}
