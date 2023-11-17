package com.example.project4task2.Service;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.example.project4task2.Model.User;
import com.example.project4task2.util.PasswordHasher;
import com.example.project4task2.util.TokenManager;
import org.bson.Document;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserService {
    private MongoCollection<Document> userCollection;
    private final static Logger LOGGER = Logger.getLogger(PlayerService.class.getName());
    public UserService() {
        try (MongoClient mongoClient = MongoClients.create
                ("mongodb+srv://alinazeng:tVx8Q6B4qWjcXdCE@cluster0.ini6vho.mongodb.net/?retryWrites=true&w=majority")) {
            MongoDatabase database = mongoClient.getDatabase("Project4");
            userCollection = database.getCollection("users");
        }
    }

    public User registerUser(String username, String password) {
        LOGGER.log(Level.INFO, "Attempting to register user: {0}", username);
        System.out.println("Attempting to register user: " + username);

        try {
            String hashedPassword = PasswordHasher.hashPassword(password);
            LOGGER.log(Level.INFO, "Password hashing succeeded for user: {0}", username);
            System.out.println("Password hashing succeeded for user: " + username);

            Document newUser = new Document("username", username)
                    .append("passwordHash", hashedPassword)
                    .append("favoritePlayers", Collections.emptyList());

            userCollection.insertOne(newUser);
            LOGGER.log(Level.INFO, "User registered successfully: {0}", username);
            System.out.println("User registered successfully: " + username);

            // Return the new User object without the password hash for security reasons
            return new User(username, null, Collections.emptyList());

        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Password hashing failed for user: {0}", username);
            System.out.println("Password hashing failed for user: " + username);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "User registration failed for user: {0}", username);
            System.out.println("User registration failed for user: " + username);
            e.printStackTrace();
            return null;
        }
    }


    public String authenticateUser(String username, String password) {
        LOGGER.log(Level.INFO, "Attempting to authenticate user: {0}", username);
        System.out.println("Attempting to authenticate user: " + username);

        Document foundUser = userCollection.find(Filters.eq("username", username)).first();
        if (foundUser == null) {
            LOGGER.log(Level.WARNING, "User not found: {0}", username);
            System.out.println("User not found: " + username);
            return null;
        }

        try {
            String hashedPassword = PasswordHasher.hashPassword(password);
            if (hashedPassword.equals(foundUser.getString("passwordHash"))) {
                LOGGER.log(Level.INFO, "User authenticated: {0}", username);
                System.out.println("User authenticated: " + username);
                return TokenManager.generateToken(username);
            } else {
                LOGGER.log(Level.WARNING, "Invalid password for user: {0}", username);
                System.out.println("Invalid password for user: " + username);
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error hashing password for user: {0}", username);
            System.out.println("Error hashing password for user: " + username);
            e.printStackTrace();
        }

        LOGGER.log(Level.INFO, "Authentication failed for user: {0}", username);
        System.out.println("Authentication failed for user: " + username);
        return null;
    }


    public boolean addFavoritePlayer(String username, String playerId) {
        LOGGER.log(Level.INFO, "Adding favorite player {0} for user: {1}", new Object[]{playerId, username});
        System.out.println("Adding favorite player " + playerId + " for user: " + username);
        try {
            userCollection.updateOne(Filters.eq("username", username), Updates.addToSet("favoritePlayers", playerId));
            LOGGER.log(Level.INFO, "Favorite player {0} added successfully for user: {1}", new Object[]{playerId, username});
            System.out.println("Favorite player " + playerId + " added successfully for user: " + username);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding favorite player {0} for user: {1}", new Object[]{playerId, username});
            System.out.println("Error adding favorite player " + playerId + " for user: " + username);
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFavoritePlayer(String username, String playerId) {
        LOGGER.log(Level.INFO, "Removing favorite player {0} for user: {1}", new Object[]{playerId, username});
        System.out.println("Removing favorite player " + playerId + " for user: " + username);
        try {
            userCollection.updateOne(Filters.eq("username", username), Updates.pull("favoritePlayers", playerId));
            LOGGER.log(Level.INFO, "Favorite player {0} removed successfully for user: {1}", new Object[]{playerId, username});
            System.out.println("Favorite player " + playerId + " removed successfully for user: " + username);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error removing favorite player {0} for user: {1}", new Object[]{playerId, username});
            System.out.println("Error removing favorite player " + playerId + " for user: " + username);
            e.printStackTrace();
            return false;
        }
    }

}
