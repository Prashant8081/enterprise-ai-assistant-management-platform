package com.enterpriseai.platform.assistant;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.enterpriseai.platform.common.NotFoundException;
import com.enterpriseai.platform.tenant.Tenant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssistantService {

    private final AiAssistantRepository assistants;

    public AssistantService(AiAssistantRepository assistants) {
        this.assistants = assistants;
    }

    @Transactional
    public AiAssistant create(Tenant tenant, CreateAssistantRequest request) {
        AiAssistant assistant = new AiAssistant(
            tenant,
            request.name(),
            slugify(request.name()),
            request.systemPrompt(),
            request.modelName());
        return assistants.save(assistant);
    }

    @Transactional(readOnly = true)
    public List<AiAssistant> list(Tenant tenant) {
        return assistants.findByTenantOrderByCreatedAtDesc(tenant);
    }

    @Transactional(readOnly = true)
    public AiAssistant getForTenant(UUID id, Tenant tenant) {
        return assistants.findByIdAndTenant(id, tenant)
            .orElseThrow(() -> new NotFoundException("Assistant not found"));
    }

    private String slugify(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("(^-|-$)", "");
        return normalized.isBlank() ? "assistant" : normalized;
    }
}
