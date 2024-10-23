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
import android.widget.Button; // Import Button
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FriendsFragment extends Fragment {

    private FriendAdapter friendAdapter;
    private List<User> userList; // Declare userList for better scope
    private DatabaseManager databaseManager; // Declare DatabaseManager instance
    private UserSessionManager userSessionManager; // Declare UserSessionManager instance
    private User currentUser; // To hold the current user
    private Button myFriendsButton; // Declare My Friends button
    private boolean showingAllFriends = true; // Flag to track the state of button

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

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

                // Fetch users from database asynchronously
                CompletableFuture<List<User>> fetchUsersFuture = databaseManager.fetchAllUsers();

                // When the data is fetched, update the RecyclerView adapter
                fetchUsersFuture.thenAccept(fetchedUsers -> {
                    // Update user list and adapter
                    userList = fetchedUsers;

                    // Set up the adapter with the fetched users
                    friendAdapter = new FriendAdapter(userList, currentUser, userId, requireContext());  // Pass current user if needed
                    RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(friendAdapter);
                }).exceptionally(ex -> {
                    // Handle any exceptions that occur during fetching users
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
                if (friendAdapter != null) {
                    friendAdapter.filter(query); // Apply filter on submit
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (friendAdapter != null) {
                    friendAdapter.filter(newText); // Apply filter as text changes
                }
                return false;
            }
        });

        // Handle "My Friends" button click to filter the user's friends
        myFriendsButton = view.findViewById(R.id.myFriendsButton); // Ensure the button is present in the layout
        myFriendsButton.setOnClickListener(v -> {
            if (showingAllFriends) {
                friendAdapter.showMyFriends(); // Show only the user's friends
                myFriendsButton.setText("all_friends");
                showingAllFriends = false;
            } else {
                friendAdapter.showAllFriends(); // Show all friends again
                myFriendsButton.setText("my_friends");
                showingAllFriends = true;
            }
        });

        return view; // Return the inflated view
    }
}
