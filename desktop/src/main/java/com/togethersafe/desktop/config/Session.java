package com.togethersafe.desktop.config;

import com.togethersafe.desktop.model.User;

public class Session {
    private static User currentUser;

    public static User getCurrentUser() { return currentUser; }
    public static void setCurrentUser(User user) { currentUser = user; }
    public static void clear() { currentUser = null; }
}