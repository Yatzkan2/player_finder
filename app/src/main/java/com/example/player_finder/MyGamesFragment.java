package com.example.player_finder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MyGamesFragment extends Fragment {

    private final List<Game> gamesList = new ArrayList<>();
    private GamesAdapter gamesAdapter;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_games, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        EditText searchInput = view.findViewById(R.id.search_input);
        EditText addInput = view.findViewById(R.id.add_input);
        Button searchButton = view.findViewById(R.id.search_button);
        Button addButton = view.findViewById(R.id.add_button);

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        gamesAdapter = new GamesAdapter(gamesList, this::removeGame);
        recyclerView.setAdapter(gamesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load user's games from Firebase
        loadGamesFromFirebase();

        // Add a game
        addButton.setOnClickListener(v -> addGame(addInput.getText().toString().trim()));

        // Search for games
        searchButton.setOnClickListener(v -> searchGames(searchInput.getText().toString().trim()));

        return view;
    }

    private void loadGamesFromFirebase() {
        firestore.collection("users").document("currentUserId").collection("games")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    gamesList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Game game = document.toObject(Game.class);
                        gamesList.add(game);
                    }
                    gamesAdapter.notifyItemRangeInserted(0, gamesList.size());  // Efficient update
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load games.", Toast.LENGTH_SHORT).show());
    }

    private void addGame(String gameName) {
        if (!gameName.isEmpty()) {
            Game newGame = new Game("someId", gameName);  // Provide an ID and title
            firestore.collection("users").document("currentUserId").collection("games")
                    .add(newGame)
                    .addOnSuccessListener(documentReference -> {
                        gamesList.add(newGame);
                        gamesAdapter.notifyItemInserted(gamesList.size() - 1);  // Efficient update
                        Toast.makeText(getContext(), gameName + " added to list", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to add game.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Enter a game name", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeGame(Game game) {
        firestore.collection("users").document("currentUserId").collection("games")
                .document(game.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    int position = gamesList.indexOf(game);
                    if (position != -1) {
                        gamesList.remove(position);
                        gamesAdapter.notifyItemRemoved(position);  // Efficient update
                        Toast.makeText(getContext(), "Game removed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to remove game.", Toast.LENGTH_SHORT).show());
    }

    private void searchGames(String query) {
        if (!query.isEmpty()) {
            List<Game> filteredList = new ArrayList<>();
            for (Game game : gamesList) {
                if (game.getTitle().toLowerCase().contains(query.toLowerCase())) {  // Use getTitle()
                    filteredList.add(game);
                }
            }
            gamesAdapter.updateList(filteredList);  // Update the filtered list
        } else {
            gamesAdapter.updateList(gamesList);  // Reset to the full list
        }
    }
}
