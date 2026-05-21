package com.enterpriseai.platform.document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record DocumentIngestRequest(
    @NotBlank @Size(max = 180) String title,
    @NotBlank @Size(max = 40) String sourceType,
    @NotBlank @Size(max = 120_000) String content
) {
}
