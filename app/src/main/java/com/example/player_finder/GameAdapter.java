package com.example.player_finder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private final List<Game> gameList;
    private final List<Game> gameListFull; // To keep a copy of the full list for filtering
    private final User currentUser; // Reference to the current user
    private final String userId; // User ID of the current user
    private DatabaseManager databaseManager; // Database manager instance
    private boolean isShowingMyGames = false; // Flag to track if "My Games" is displayed


    // Constructor
    public GameAdapter(List<Game> gameList, User currentUser, String userId) {
        this.gameList = gameList;
        this.gameListFull = new ArrayList<>(gameList); // Create a copy of the full list
        this.currentUser = currentUser; // Pass the current user for managing games
        this.userId = userId; // Store the user ID
        this.databaseManager = new DatabaseManager(); // Initialize DatabaseManager

    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.gameTitle.setText(game.getTitle());

        // Check if the game is already in the user's list
        boolean isGameInUserList = currentUser.hasGame(game.getTitle());

        // Set button text based on game presence
        holder.buttonAction.setText(isGameInUserList ? "Remove" : "Add");

        holder.buttonAction.setOnClickListener(v -> {
            if (isGameInUserList) {
                currentUser.removeGame(game.getTitle()); // Remove the game
                databaseManager.updateUserFieldById(userId, "gamesList", currentUser.getGamesList());
                Toast.makeText(v.getContext(), "Removed: " + game.getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                currentUser.addGame(game.getTitle()); // Add the game
                databaseManager.updateUserFieldById(userId, "gamesList", currentUser.getGamesList());
                Toast.makeText(v.getContext(), "Added: " + game.getTitle(), Toast.LENGTH_SHORT).show();
            }
            //notifyItemChanged(position); // Refresh the item to update button text
            // Re-render based on current view
            if (isShowingMyGames) {
                showMyGames(); // Re-filter to show the updated "My Games" list
            } else {
                notifyItemChanged(position); // Just refresh the item if in "All Games"
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    // ViewHolder class for RecyclerView
    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView gameTitle;
        Button buttonAction; // Action button (Add/Remove)

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.game_title); // Adjust according to your layout
            buttonAction = itemView.findViewById(R.id.button_action); // Action button
        }
    }

    // Method to show only the user's games
    public void showMyGames() {
        isShowingMyGames = true; // Track that we are in "My Games" mode
        List<Game> filteredList = gameListFull.stream()
                .filter(game -> currentUser.hasGame(game.getTitle()))
                .collect(Collectors.toList());
        applyListChanges(filteredList);
    }

    // Method to show all games
    public void showAllGames() {
        isShowingMyGames = false; // Track that we are in "All Games" mode
        applyListChanges(gameListFull);
    }
    // Filter method
    public void filter(String text) {
        List<Game> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(gameListFull); // No filter applied, show full list
        } else {
            String filterPattern = text.toLowerCase().trim();
            for (Game game : gameListFull) {
                if (game.getTitle().toLowerCase().contains(filterPattern)) {
                    filteredList.add(game);
                }
            }
        }

        // Calculate changes in the list and apply efficient notifications
        applyListChanges(filteredList);
    }

    private void applyListChanges(List<Game> newList) {
        for (int i = gameList.size() - 1; i >= 0; i--) {
            if (!newList.contains(gameList.get(i))) {
                gameList.remove(i);
                notifyItemRemoved(i);
            }
        }

        for (int i = 0; i < newList.size(); i++) {
            if (!gameList.contains(newList.get(i))) {
                gameList.add(i, newList.get(i));
                notifyItemInserted(i);
            }
        }
    }
}
