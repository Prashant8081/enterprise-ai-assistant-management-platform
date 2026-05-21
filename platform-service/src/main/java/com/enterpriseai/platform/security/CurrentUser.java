package com.enterpriseai.platform.security;

import com.enterpriseai.platform.user.UserAccount;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    public UserAccount get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserAccount user)) {
            throw new IllegalStateException("No authenticated user in security context");
        }
        return user;
    }
}
