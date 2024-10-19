package com.example.player_finder;

public class Game {
    private String id;
    private String title;

    public Game(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
