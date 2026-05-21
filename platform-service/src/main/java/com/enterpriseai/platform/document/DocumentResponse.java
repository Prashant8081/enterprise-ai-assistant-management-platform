package com.enterpriseai.platform.document;

import java.time.Instant;
import java.util.UUID;

record DocumentResponse(
    UUID id,
    String title,
    String sourceType,
    DocumentStatus status,
    String contentHash,
    Instant createdAt
) {

    static DocumentResponse from(KnowledgeDocument document) {
        return new DocumentResponse(
            document.getId(),
            document.getTitle(),
            document.getSourceType(),
            document.getStatus(),
            document.getContentHash(),
            document.getCreatedAt());
    }
}
