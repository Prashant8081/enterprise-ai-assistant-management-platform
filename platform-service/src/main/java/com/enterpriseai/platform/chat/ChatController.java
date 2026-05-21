package com.enterpriseai.platform.chat;

import java.util.UUID;

import com.enterpriseai.platform.security.CurrentUser;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistants/{assistantId}/chat")
class ChatController {

    private final ChatService chatService;
    private final CurrentUser currentUser;

    ChatController(ChatService chatService, CurrentUser currentUser) {
        this.chatService = chatService;
        this.currentUser = currentUser;
    }

    @PostMapping
    ChatResponse chat(@PathVariable UUID assistantId, @Valid @RequestBody ChatRequest request) {
        return chatService.chat(currentUser.get(), assistantId, request);
    }
}
