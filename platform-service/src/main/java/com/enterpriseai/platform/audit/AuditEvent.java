package com.enterpriseai.platform.audit;

import java.time.Instant;
import java.util.UUID;

public record AuditEvent(
    UUID auditId,
    UUID tenantId,
    UUID assistantId,
    UUID userId,
    String modelName,
    int retrievedChunks,
    long latencyMs,
    Instant occurredAt
) {

    public static AuditEvent from(AuditLog log, UUID tenantId) {
        return new AuditEvent(
            log.getId(),
            tenantId,
            log.getAssistantId(),
            log.getUserId(),
            log.getModelName(),
            log.getRetrievedChunks(),
            log.getLatencyMs(),
            log.getCreatedAt());
    }
}
