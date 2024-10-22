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

import java.util.ArrayList;
import java.util.List;

public class GamesFragment extends Fragment {

    private GameAdapter gameAdapter;
    private List<Game> gameList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the game list with some Game objects
        gameList = new ArrayList<>();
        gameList.add(new Game("Fortnite"));
        gameList.add(new Game("League of Legends"));
        gameList.add(new Game("Minecraft"));
        // Add more games as needed

        // Set up the adapter
        gameAdapter = new GameAdapter(gameList);
        recyclerView.setAdapter(gameAdapter);

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
