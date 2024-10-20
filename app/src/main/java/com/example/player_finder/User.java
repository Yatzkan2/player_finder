package com.example.player_finder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private List<User> friendsList;
    private List<Game> gamesList;

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

    public void addGame(Game game) {
        gamesList.add(game);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public List<Game> getGamesList() {
        return gamesList;
    }
}
