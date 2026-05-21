package com.enterpriseai.platform.bootstrap;

import java.util.Set;

import com.enterpriseai.platform.security.Role;
import com.enterpriseai.platform.tenant.Tenant;
import com.enterpriseai.platform.tenant.TenantRepository;
import com.enterpriseai.platform.user.UserAccount;
import com.enterpriseai.platform.user.UserAccountRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class DataInitializer implements CommandLineRunner {

    private final TenantRepository tenants;
    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    DataInitializer(TenantRepository tenants,
                    UserAccountRepository users,
                    PasswordEncoder passwordEncoder,
                    @Value("${app.bootstrap.admin-email}") String adminEmail,
                    @Value("${app.bootstrap.admin-password}") String adminPassword) {
        this.tenants = tenants;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Tenant tenant = tenants.findBySlug("acme")
            .orElseGet(() -> tenants.save(new Tenant("Acme Enterprise", "acme")));

        if (!users.existsByEmailIgnoreCase(adminEmail)) {
            users.save(new UserAccount(
                adminEmail,
                passwordEncoder.encode(adminPassword),
                "Platform Admin",
                tenant,
                Set.of(Role.PLATFORM_ADMIN, Role.TENANT_ADMIN, Role.APP_BUILDER, Role.AUDITOR, Role.USER)));
        }
    }
}
