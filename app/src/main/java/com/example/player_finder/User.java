package com.example.player_finder;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id; // This will hold the Firestore document ID
    private String username;
    private String email;
    private String password;
    private final List<String> friendsList;
    private final List<String> gamesList;

    // No-argument constructor required for Firestore deserialization
    public User() {
        this.friendsList = new ArrayList<>();
        this.gamesList = new ArrayList<>();
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.friendsList = new ArrayList<>();
        this.gamesList = new ArrayList<>();
    }

    // Setters
    public void setId(String id) {
        this.id = id; // Firestore document ID
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getters
    public String getId() {
        return id; // Returns Firestore document ID
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

    public List<String> getFriendsList() {
        return friendsList;
    }

    public List<String> getGamesList() {
        return gamesList;
    }

    // Methods for friends management
    public void addFriend(String username) {
        friendsList.add(username);
    }

    public void removeFriend(String username) {
        friendsList.remove(username);
    }

    public boolean isFriend(String username) {
        return friendsList.contains(username);
    }

    // Methods for games management
    public void addGame(String gameTitle) {
        if (!gamesList.contains(gameTitle)) {
            gamesList.add(gameTitle);
        }
    }

    public void removeGame(String gameTitle) {
        gamesList.remove(gameTitle);
    }

    public boolean hasGame(String gameTitle) {
        return gamesList.contains(gameTitle);
    }
}
