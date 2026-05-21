package com.enterpriseai.platform.assistant;

import java.time.Instant;
import java.util.UUID;

record AssistantResponse(
    UUID id,
    String name,
    String slug,
    String systemPrompt,
    String modelName,
    AssistantStatus status,
    Instant createdAt
) {

    static AssistantResponse from(AiAssistant assistant) {
        return new AssistantResponse(
            assistant.getId(),
            assistant.getName(),
            assistant.getSlug(),
            assistant.getSystemPrompt(),
            assistant.getModelName(),
            assistant.getStatus(),
            assistant.getCreatedAt());
    }
}
