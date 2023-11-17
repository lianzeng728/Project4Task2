package com.example.project4task2.Model;

import java.util.List;

public class User {
    private String username;
    private String passwordHash; // Store hashed passwords
    private List<String> favoritePlayerIds;

    // Constructor
    public User(String username, String passwordHash, List<String> favoritePlayerIds) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.favoritePlayerIds = favoritePlayerIds;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public List<String> getFavoritePlayerIds() { return favoritePlayerIds; }
    public void setFavoritePlayerIds(List<String> favoritePlayerIds) { this.favoritePlayerIds = favoritePlayerIds; }
}