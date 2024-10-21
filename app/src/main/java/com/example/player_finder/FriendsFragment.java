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

public class FriendsFragment extends Fragment {

    private final List<User> friendsList = new ArrayList<>(); // Marked as final
    private FriendsAdapter friendsAdapter;
    private RecyclerView recyclerView;
    private EditText searchInput, addInput;
    private Button searchButton, addButton;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view);
        searchInput = view.findViewById(R.id.search_input);
        addInput = view.findViewById(R.id.add_input);
        searchButton = view.findViewById(R.id.search_button);
        addButton = view.findViewById(R.id.add_button);

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        friendsAdapter = new FriendsAdapter(friendsList, this::removeFriend);
        recyclerView.setAdapter(friendsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load user friends from Firebase
        loadFriendsFromFirebase();

        // Add a friend
        addButton.setOnClickListener(v -> addFriend());

        // Search for friends
        searchButton.setOnClickListener(v -> searchFriends());

        return view;
    }

    private void loadFriendsFromFirebase() {
        firestore.collection("users").document("currentUserId").collection("friends")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    friendsList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        User friend = document.toObject(User.class);
                        friendsList.add(friend);
                    }
                    friendsAdapter.notifyItemRangeInserted(0, friendsList.size()); // More efficient method
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load friends.", Toast.LENGTH_SHORT).show());
    }

    private void addFriend() {
        String username = addInput.getText().toString().trim();
        if (!username.isEmpty()) {
            User newFriend = new User(username, "email@example.com", "password");
            firestore.collection("users").document("currentUserId").collection("friends")
                    .add(newFriend)
                    .addOnSuccessListener(documentReference -> {
                        friendsList.add(newFriend);
                        friendsAdapter.notifyItemInserted(friendsList.size() - 1); // More efficient method
                        Toast.makeText(getContext(), username + " added to list", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to add friend.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Enter a username", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFriend(User user) {
        firestore.collection("users").document("currentUserId").collection("friends")
                .document(user.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    int position = friendsList.indexOf(user);
                    if (position != -1) {
                        friendsList.remove(position);
                        friendsAdapter.notifyItemRemoved(position); // More efficient method
                        Toast.makeText(getContext(), "Friend removed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to remove friend.", Toast.LENGTH_SHORT).show());
    }

    private void searchFriends() {
        String query = searchInput.getText().toString().trim();
        if (!query.isEmpty()) {
            List<User> filteredList = new ArrayList<>();
            for (User user : friendsList) {
                if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(user);
                }
            }
            friendsAdapter.updateList(filteredList); // Efficient update
        } else {
            friendsAdapter.updateList(friendsList);  // Reset to the full list
        }
    }
}
