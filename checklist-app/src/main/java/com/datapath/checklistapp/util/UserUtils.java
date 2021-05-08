package com.datapath.checklistapp.util;

import com.datapath.checklistapp.security.UserAuthInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    public static Integer getCurrentUserId() {
        return Integer.parseInt(((UserAuthInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    public static boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(String::valueOf)
                .anyMatch(role::equals);
    }
}
