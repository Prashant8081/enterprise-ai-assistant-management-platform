package com.enterpriseai.platform.chat;

import java.util.List;
import java.util.UUID;

record ChatResponse(
    UUID auditId,
    String answer,
    String modelName,
    long latencyMs,
    List<Citation> citations
) {

    record Citation(UUID documentId, String title, int chunkIndex, String excerpt) {
    }
}
