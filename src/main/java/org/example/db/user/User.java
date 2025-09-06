package org.example.db.user;

import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;

public record User(@Nullable Integer userId, String name, String email, @Nullable Timestamp createdAt) {
    public User(String name, String email) {
        this(null, name, email, null);
    }
}
