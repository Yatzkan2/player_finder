package com.example.player_finder;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.WriteBatch;
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


    // Clear collection with a Task that signals when clearing is complete
    private Task<Void> clearCollection(String collectionName) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        db.collection(collectionName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WriteBatch batch = db.batch();
                for (DocumentSnapshot document : task.getResult()) {
                    batch.delete(document.getReference());
                }
                batch.commit().addOnCompleteListener(batchTask -> {
                    if (batchTask.isSuccessful()) {
                        Log.d("DatabaseManager", "Collection " + collectionName + " cleared.");
                        taskCompletionSource.setResult(null);
                    } else {
                        Log.w("DatabaseManager", "Error clearing collection: " + collectionName, batchTask.getException());
                        taskCompletionSource.setException(batchTask.getException());
                    }
                });
            } else {
                Log.w("DatabaseManager", "Error getting documents: ", task.getException());
                taskCompletionSource.setException(task.getException());
            }
        });

        return taskCompletionSource.getTask();
    }

    // Seed the database with South Park characters and related games
    public void seedDatabase() {
        // First, clear the "users" and "games" collections
        Task<Void> clearUsersTask = clearCollection("users");
        Task<Void> clearGamesTask = clearCollection("games");

        // Wait for both collections to be cleared before proceeding to seed
        Tasks.whenAll(clearUsersTask, clearGamesTask).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Create users (South Park characters) using ArrayList
                List<User> southParkCharacters = new ArrayList<>();
                southParkCharacters.add(new User("Eric Cartman", "cartman@southpark.com", "1234"));
                southParkCharacters.add(new User("Stan Marsh", "stan@southpark.com", "1234"));
                southParkCharacters.add(new User("Kyle Broflovski", "kyle@southpark.com", "1234"));
                southParkCharacters.add(new User("Kenny McCormick", "kenny@southpark.com", "1234"));
                southParkCharacters.add(new User("Butters Stotch", "butters@southpark.com", "1234"));
                southParkCharacters.add(new User("Randy Marsh", "randy@southpark.com", "1234"));

                // Create games (South Park-themed games) using ArrayList
                List<Game> southParkGames = new ArrayList<>();
                southParkGames.add(new Game("Terrance and Phillip: The Game"));
                southParkGames.add(new Game("Chasing the Dream"));
                southParkGames.add(new Game("The Coon's Revenge"));
                southParkGames.add(new Game("Operation: Black Friday"));
                southParkGames.add(new Game("South Park Rally"));

                // Seed users into the "users" collection
                for (User user : southParkCharacters) {
                    db.collection("users").add(user)
                            .addOnSuccessListener(documentReference -> Log.d("DatabaseManager", "User added with ID: " + documentReference.getId()))
                            .addOnFailureListener(e -> Log.w("DatabaseManager", "Error adding user", e));
                }

                // Seed games into the "games" collection
                for (Game game : southParkGames) {
                    db.collection("games").add(game)
                            .addOnSuccessListener(documentReference -> Log.d("DatabaseManager", "Game added with ID: " + documentReference.getId()))
                            .addOnFailureListener(e -> Log.w("DatabaseManager", "Error adding game", e));
                }
            } else {
                Log.w("DatabaseManager", "Error clearing collections: ", task.getException());
            }
        });
    }
}
