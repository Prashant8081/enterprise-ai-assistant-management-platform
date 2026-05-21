package com.enterpriseai.platform.document;

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
@RequestMapping("/api/assistants/{assistantId}/documents")
class DocumentController {

    private final DocumentService documentService;
    private final CurrentUser currentUser;

    DocumentController(DocumentService documentService, CurrentUser currentUser) {
        this.documentService = documentService;
        this.currentUser = currentUser;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN', 'TENANT_ADMIN', 'APP_BUILDER')")
    DocumentResponse ingest(@PathVariable UUID assistantId,
                            @Valid @RequestBody DocumentIngestRequest request) {
        return DocumentResponse.from(documentService.ingest(currentUser.get().getTenant(), assistantId, request));
    }

    @GetMapping
    List<DocumentResponse> list(@PathVariable UUID assistantId) {
        return documentService.list(currentUser.get().getTenant(), assistantId)
            .stream()
            .map(DocumentResponse::from)
            .toList();
    }
}
