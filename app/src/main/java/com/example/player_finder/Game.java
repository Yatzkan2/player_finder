package com.example.player_finder;

import java.util.UUID;

public class Game {
    private String id;
    private String title;

    // No-argument constructor required for Firestore deserialization
    public Game() {
        //this.id = UUID.randomUUID().toString();  // Generate a new ID if needed
    }

    public Game(String title) {
        this.id = UUID.randomUUID().toString();  // Generate a new ID if needed

        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
