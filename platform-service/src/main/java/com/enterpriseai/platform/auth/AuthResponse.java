package com.enterpriseai.platform.auth;

import java.util.Set;
import java.util.UUID;

import com.enterpriseai.platform.security.Role;
import com.enterpriseai.platform.user.UserAccount;

record AuthResponse(String token, UserView user) {

    static AuthResponse from(UserAccount user, String token) {
        return new AuthResponse(token, UserView.from(user));
    }

    record UserView(UUID id, String email, String fullName, UUID tenantId, String tenantSlug, Set<Role> roles) {

        static UserView from(UserAccount user) {
            return new UserView(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getTenant().getId(),
                user.getTenant().getSlug(),
                user.getRoles());
        }
    }
}
