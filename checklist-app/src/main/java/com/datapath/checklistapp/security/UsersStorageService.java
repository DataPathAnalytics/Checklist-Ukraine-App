package com.datapath.checklistapp.security;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class UsersStorageService {

    private static Set<Long> USERS_STORAGE = new CopyOnWriteArraySet<>();

    private UsersStorageService() {

    }

    static void addUser(Long userId) {
        log.info("Added user {}", userId);
        USERS_STORAGE.add(userId);
    }

    public static void removeUser(Long userId) {
        log.info("Removed user {}", userId);
        USERS_STORAGE.remove(userId);
    }

    static boolean isUserPresent(Long userId) {
        return USERS_STORAGE.contains(userId);
    }
}
