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
    private UserSessionManager userSessionManager; // Declare UserSessionManager instance
    private User currentUser; // To hold the current user

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        // Initialize UserSessionManager
        userSessionManager = new UserSessionManager(getContext());

        // Fetch the current user based on the stored user ID
        String userId = userSessionManager.getUserId();
        if (userId != null) {
            databaseManager = new DatabaseManager(); // Initialize DatabaseManager

            // Fetch the current user from the database
            CompletableFuture<User> fetchUserFuture = databaseManager.fetchUserById(userId);
            fetchUserFuture.thenAccept(user -> {
                currentUser = user; // Store the current user

                // Now fetch the games after getting the current user
                CompletableFuture<List<Game>> fetchGamesFuture = databaseManager.fetchAllGames();

                // When the data is fetched, update the RecyclerView adapter
                fetchGamesFuture.thenAccept(fetchedGames -> {
                    // Update game list and adapter
                    gameList = fetchedGames;

                    // Set up the adapter with the fetched games and the current user
                    gameAdapter = new GameAdapter(gameList, currentUser, userId);
                    RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(gameAdapter);
                }).exceptionally(ex -> {
                    // Handle any exceptions that occur during fetching games
                    ex.printStackTrace();
                    return null;
                });
            }).exceptionally(ex -> {
                // Handle any exceptions that occur during fetching user
                ex.printStackTrace();
                return null;
            });
        }

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
