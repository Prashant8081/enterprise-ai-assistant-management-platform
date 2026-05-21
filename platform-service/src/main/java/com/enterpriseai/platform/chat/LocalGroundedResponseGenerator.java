package com.enterpriseai.platform.chat;

import java.util.List;
import java.util.stream.Collectors;

import com.enterpriseai.platform.assistant.AiAssistant;
import com.enterpriseai.platform.document.DocumentChunk;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "local", matchIfMissing = true)
class LocalGroundedResponseGenerator implements AiResponseGenerator {

    @Override
    public String generate(AiAssistant assistant, String userMessage, List<DocumentChunk> context) {
        if (context.isEmpty()) {
            return "I could not find indexed knowledge for this assistant yet. Add documents first, then ask again.";
        }

        String evidence = context.stream()
            .map(chunk -> "- " + compact(chunk.getContent(), 450))
            .collect(Collectors.joining("\n"));

        return """
            %s

            Based on the indexed enterprise knowledge, here is the safest answer:
            %s

            User question: %s

            I used the retrieved document chunks above as grounding context. For production, switch app.ai.provider to a real Spring AI-backed model provider.
            """.formatted(assistant.getSystemPrompt(), evidence, userMessage).trim();
    }

    private String compact(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }
}
