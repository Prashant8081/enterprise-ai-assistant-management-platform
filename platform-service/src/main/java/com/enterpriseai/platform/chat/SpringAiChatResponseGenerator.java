package com.enterpriseai.platform.chat;

import java.util.List;
import java.util.stream.Collectors;

import com.enterpriseai.platform.assistant.AiAssistant;
import com.enterpriseai.platform.document.DocumentChunk;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(ChatClient.Builder.class)
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "spring-ai")
class SpringAiChatResponseGenerator implements AiResponseGenerator {

    private final ChatClient chatClient;

    SpringAiChatResponseGenerator(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public String generate(AiAssistant assistant, String userMessage, List<DocumentChunk> context) {
        String grounding = context.stream()
            .map(DocumentChunk::getContent)
            .collect(Collectors.joining("\n\n---\n\n"));

        return chatClient.prompt()
            .system(assistant.getSystemPrompt())
            .user("""
                Answer using only the enterprise knowledge below. If the answer is not present, say that the knowledge base does not contain enough information.

                Enterprise knowledge:
                %s

                User question:
                %s
                """.formatted(grounding, userMessage))
            .call()
            .content();
    }
}
