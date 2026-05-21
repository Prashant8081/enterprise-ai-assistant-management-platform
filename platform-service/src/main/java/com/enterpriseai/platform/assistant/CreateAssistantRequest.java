package com.enterpriseai.platform.assistant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record CreateAssistantRequest(
    @NotBlank @Size(max = 120) String name,
    @NotBlank @Size(max = 4000) String systemPrompt,
    @NotBlank @Size(max = 120) String modelName
) {
}
