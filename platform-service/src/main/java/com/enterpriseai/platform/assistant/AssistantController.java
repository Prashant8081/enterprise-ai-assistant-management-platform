package com.enterpriseai.platform.assistant;

import java.util.List;
import java.util.UUID;

import com.enterpriseai.platform.security.CurrentUser;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistants")
class AssistantController {

    private final AssistantService assistantService;
    private final CurrentUser currentUser;

    AssistantController(AssistantService assistantService, CurrentUser currentUser) {
        this.assistantService = assistantService;
        this.currentUser = currentUser;
    }

    @GetMapping
    List<AssistantResponse> list() {
        return assistantService.list(currentUser.get().getTenant())
            .stream()
            .map(AssistantResponse::from)
            .toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN', 'TENANT_ADMIN', 'APP_BUILDER')")
    AssistantResponse create(@Valid @RequestBody CreateAssistantRequest request) {
        return AssistantResponse.from(assistantService.create(currentUser.get().getTenant(), request));
    }

    @GetMapping("/{assistantId}")
    AssistantResponse get(@PathVariable UUID assistantId) {
        return AssistantResponse.from(assistantService.getForTenant(assistantId, currentUser.get().getTenant()));
    }
}
