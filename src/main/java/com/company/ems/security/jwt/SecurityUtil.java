package com.company.ems.security.jwt;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.company.ems.user.User;
import com.company.ems.user.UserRepository;

@Component
public class SecurityUtil {

    private final UserRepository userRepository;

    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserPrincipal getCurrentPrincipal() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
            !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new RuntimeException("User not authenticated");
        }
        return principal;
    }

    public UUID getCurrentUserId() {
        return getCurrentPrincipal().getId();
    }

    public UUID getCurrentCompanyId() {
        return getCurrentPrincipal().getCompanyId();
    }

    public User getCurrentUser() {
        return userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}