package com.example.project4task2.util;

import java.util.UUID;

public class TokenManager {
    public static String generateToken(String username) {
        // In a real application, use JWT or similar library to generate a secure token
        return UUID.randomUUID().toString();
    }
}
//alinazeng
//tVx8Q6B4qWjcXdCE