package com.example.player_finder;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {

    private final FirebaseFirestore db;

    public DatabaseManager() {
        this.db = FirebaseFirestore.getInstance(); // Get Firestore instance
    }

    // Fetch user by ID
    public CompletableFuture<User> fetchUserById(String id) {
        CompletableFuture<User> future = new CompletableFuture<>();
        CollectionReference usersCollection = db.collection("users");

        usersCollection.document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                User user = task.getResult().toObject(User.class);
                future.complete(user);
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

    // Fetch user by email
    public CompletableFuture<User> fetchUserByEmail(String email) {
        CompletableFuture<User> future = new CompletableFuture<>();
        CollectionReference usersCollection = db.collection("users");

        usersCollection.whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                User user = task.getResult().getDocuments().get(0).toObject(User.class);
                future.complete(user);
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

    // Fetch game by name (title)
    public CompletableFuture<Game> fetchGameByName(String title) {
        CompletableFuture<Game> future = new CompletableFuture<>();
        CollectionReference gamesCollection = db.collection("games");

        gamesCollection.whereEqualTo("title", title).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                Game game = task.getResult().getDocuments().get(0).toObject(Game.class);
                future.complete(game);
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

    // Fetch all users
    public CompletableFuture<List<User>> fetchAllUsers() {
        CompletableFuture<List<User>> future = new CompletableFuture<>();
        CollectionReference usersCollection = db.collection("users");

        usersCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> userList = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                future.complete(userList);
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

    // Fetch all games
    public CompletableFuture<List<Game>> fetchAllGames() {
        CompletableFuture<List<Game>> future = new CompletableFuture<>();
        CollectionReference gamesCollection = db.collection("games");

        gamesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Game> gameList = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    Game game = document.toObject(Game.class);
                    if (game != null) {
                        gameList.add(game);
                    }
                }
                future.complete(gameList);
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }
}
