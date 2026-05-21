package com.enterpriseai.platform.document;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {

    @Query(value = """
        select *
        from document_chunks
        where tenant_id = :tenantId
          and assistant_id = :assistantId
        order by embedding <=> cast(:embedding as vector)
        limit :limit
        """, nativeQuery = true)
    List<DocumentChunk> searchSimilar(@Param("tenantId") UUID tenantId,
                                      @Param("assistantId") UUID assistantId,
                                      @Param("embedding") String embedding,
                                      @Param("limit") int limit);
}
