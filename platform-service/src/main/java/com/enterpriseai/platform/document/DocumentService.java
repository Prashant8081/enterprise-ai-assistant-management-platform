package com.enterpriseai.platform.document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

import com.enterpriseai.platform.assistant.AiAssistant;
import com.enterpriseai.platform.assistant.AssistantService;
import com.enterpriseai.platform.tenant.Tenant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentService {

    private final AssistantService assistantService;
    private final KnowledgeDocumentRepository documents;
    private final DocumentChunkRepository chunks;
    private final DocumentChunker chunker;
    private final EmbeddingService embeddingService;

    public DocumentService(AssistantService assistantService,
                           KnowledgeDocumentRepository documents,
                           DocumentChunkRepository chunks,
                           DocumentChunker chunker,
                           EmbeddingService embeddingService) {
        this.assistantService = assistantService;
        this.documents = documents;
        this.chunks = chunks;
        this.chunker = chunker;
        this.embeddingService = embeddingService;
    }

    @Transactional
    public KnowledgeDocument ingest(Tenant tenant, java.util.UUID assistantId, DocumentIngestRequest request) {
        AiAssistant assistant = assistantService.getForTenant(assistantId, tenant);
        KnowledgeDocument document = documents.save(new KnowledgeDocument(
            tenant,
            assistant,
            request.title(),
            request.sourceType(),
            sha256(request.content())));

        List<String> splitContent = chunker.chunk(request.content());
        for (int i = 0; i < splitContent.size(); i++) {
            String content = splitContent.get(i);
            chunks.save(new DocumentChunk(
                tenant,
                assistant,
                document,
                i,
                content,
                chunker.estimateTokens(content),
                embeddingService.embed(content)));
        }
        return document;
    }

    @Transactional(readOnly = true)
    public List<KnowledgeDocument> list(Tenant tenant, java.util.UUID assistantId) {
        AiAssistant assistant = assistantService.getForTenant(assistantId, tenant);
        return documents.findByTenantAndAssistantOrderByCreatedAtDesc(tenant, assistant);
    }

    @Transactional(readOnly = true)
    public List<DocumentChunk> retrieve(Tenant tenant, AiAssistant assistant, String query, int limit) {
        return chunks.searchSimilar(tenant.getId(), assistant.getId(), embeddingService.embed(query), limit);
    }

    private String sha256(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 not available", ex);
        }
    }
}
