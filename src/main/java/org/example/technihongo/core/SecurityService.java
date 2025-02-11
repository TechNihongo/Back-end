package org.example.technihongo.core;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {
    public boolean hasAnyRole(Authentication authentication, String... rolename) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            for (String roleName : rolename) {
                if (authority.getAuthority().equals("ROLE_" + roleName)) {
                    return true;
                }
            }
        }
        return false;
    }
}