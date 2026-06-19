package com.company.ems.security.jwt;

import com.company.ems.common.DataScope;
import com.company.ems.role.Role;
import com.company.ems.user.User;
import com.company.ems.userrole.UserRole;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Objects;

@Service
public class DataScopeService {

    private final SecurityUtil securityUtil;

    public DataScopeService(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    public DataScope getCurrentUserScope() {
        User user = securityUtil.getCurrentUser();
        if (user == null || user.getUserRoles() == null) {
            return DataScope.SELF;
        }
        return user.getUserRoles()
                .stream()
                .map(UserRole::getRole)
                .filter(Objects::nonNull)
                .map(Role::getDataScope)
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(this::priority))
                .orElse(DataScope.SELF);
    }

    private int priority(DataScope scope) {
        return switch (scope) {
            case SELF -> 1;
            case DEPARTMENT -> 2;
            case ALL -> 3;
        };
    }
}