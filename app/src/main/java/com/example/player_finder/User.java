package com.example.player_finder;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private List<User> friendsList;
    private List<Game> gamesList;

    public User(String id, String username, String email, String password) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<User> getFriendsList() {
        return friendsList;
    }

    public List<Game> getGamesList() {
        return gamesList;
    }
}
