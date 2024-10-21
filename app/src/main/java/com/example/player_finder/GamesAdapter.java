package com.example.player_finder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GamesViewHolder> {

    private List<Game> gamesList;
    private final OnGameRemoveListener removeListener;

    public GamesAdapter(List<Game> gamesList, OnGameRemoveListener removeListener) {
        this.gamesList = gamesList;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public GamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GamesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GamesViewHolder holder, int position) {
        Game game = gamesList.get(position);
        holder.gameNameTextView.setText(game.getTitle());

        // Remove game listener
        holder.itemView.setOnLongClickListener(v -> {
            removeListener.onGameRemove(game);
            notifyItemRemoved(holder.getAdapterPosition());  // Notify that an item has been removed
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    public void updateList(List<Game> updatedList) {
        this.gamesList = updatedList;
        notifyItemRangeChanged(0, updatedList.size());  // Refresh only the changed range
    }

    public static class GamesViewHolder extends RecyclerView.ViewHolder {
        public TextView gameNameTextView;

        public GamesViewHolder(@NonNull View itemView) {
            super(itemView);
            gameNameTextView = itemView.findViewById(R.id.game_name_text_view);  // Add the ID in item_game.xml layout
        }
    }

    public interface OnGameRemoveListener {
        void onGameRemove(Game game);
    }
}
