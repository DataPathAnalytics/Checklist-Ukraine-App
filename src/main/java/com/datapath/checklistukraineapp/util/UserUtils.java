package com.datapath.checklistukraineapp.util;

import com.datapath.checklistukraineapp.security.UserAuthInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    public static Long getCurrentUserId() {
        return Long.parseLong(((UserAuthInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }
}
