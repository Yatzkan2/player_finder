package com.example.player_finder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private final List<User> friendsList;
    private final List<String> gamesList; // Change from List<Game> to List<String> for game IDs

    // No-argument constructor required for Firestore deserialization
    public User() {
        this.id = UUID.randomUUID().toString();  // Generate a new ID if needed
        this.friendsList = new ArrayList<>();
        this.gamesList = new ArrayList<>();
    }

    public User(String username, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
        this.friendsList = new ArrayList<>();
        this.gamesList = new ArrayList<>();
    }

    public void addFriend(User friend) {
        friendsList.add(friend);
    }

    // Add game by ID
    public void addGame(String gameTitle) {
        if (!gamesList.contains(gameTitle)) {
            gamesList.add(gameTitle); // Add the game ID to the list
            // Save to database if needed
        }
    }

    // Remove game by ID
    public void removeGame(String gameTitle) {
        gamesList.remove(gameTitle); // Remove the game ID from the list
        // Save to database if needed
    }

    // Check if game exists in user's list
    public boolean hasGame(String gameTitle) {
        return gamesList.contains(gameTitle); // Return true if game ID exists in the list
    }

    //setters
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    //getters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<User> getFriendsList() {
        return friendsList;
    }

    public List<String> getGamesList() {
        return gamesList; // Return the list of game IDs
    }
}
