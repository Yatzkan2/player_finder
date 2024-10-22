package com.example.player_finder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private final List<Game> gameList;
    private final List<Game> gameListFull; // To keep a copy of the full list for filtering

    // Constructor
    public GameAdapter(List<Game> gameList) {
        this.gameList = gameList;
        this.gameListFull = new ArrayList<>(gameList); // Create a copy of the full list
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

        // Add click listener for itemView
        holder.itemView.setOnClickListener(v -> {
            // Display Toast with the game's title when clicked
            Toast.makeText(v.getContext(), "Clicked: " + game.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    // ViewHolder class for RecyclerView
    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView gameTitle;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.game_title); // Adjust according to your layout
        }
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
