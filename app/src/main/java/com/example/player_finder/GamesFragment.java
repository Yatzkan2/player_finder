package com.example.player_finder;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GamesFragment extends Fragment {

    private GameAdapter gameAdapter;
    private List<Game> gameList; // Declare gameList for better scope
    private DatabaseManager databaseManager; // Declare DatabaseManager instance

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize DatabaseManager
        databaseManager = new DatabaseManager(); // Assuming you have a default constructor

        // Fetch games from database asynchronously
        CompletableFuture<List<Game>> fetchGamesFuture = databaseManager.fetchAllGames();

        // When the data is fetched, update the RecyclerView adapter
        fetchGamesFuture.thenAccept(fetchedGames -> {
            // Update game list and adapter
            gameList = fetchedGames;

            // Set up the adapter with the fetched games
            gameAdapter = new GameAdapter(gameList);
            recyclerView.setAdapter(gameAdapter);
        }).exceptionally(ex -> {
            // Handle any exceptions that occur during fetching
            ex.printStackTrace();
            return null;
        });

        // Initialize the SearchView and set up search filtering
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                gameAdapter.filter(query); // Apply filter on submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                gameAdapter.filter(newText); // Apply filter as text changes
                return false;
            }
        });

        return view;
    }
}
