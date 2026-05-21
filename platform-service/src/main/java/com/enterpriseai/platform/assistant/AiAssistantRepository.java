package com.enterpriseai.platform.assistant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.enterpriseai.platform.tenant.Tenant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AiAssistantRepository extends JpaRepository<AiAssistant, UUID> {

    List<AiAssistant> findByTenantOrderByCreatedAtDesc(Tenant tenant);

    Optional<AiAssistant> findByIdAndTenant(UUID id, Tenant tenant);
}
