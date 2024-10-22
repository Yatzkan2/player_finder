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

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private final List<User> userList;
    private final List<User> userListFull; // To keep a copy of the full list for filtering
    private DatabaseManager databaseManager; // Database manager instance
    private User currentUser; // Reference to the current user
    private final String userId; // User ID of the current user
    private boolean isShowingMyFriends = false; // Flag to track if "My Friends" is displayed

    // Constructor
    public FriendAdapter(List<User> userList, User currentUser, String userId) {
        this.userList = userList;
        this.userListFull = new ArrayList<>(userList); // Create a copy of the full list
        this.databaseManager = new DatabaseManager(); // Initialize DatabaseManager
        this.currentUser = currentUser; // Store the current user
        this.userId = userId; // Store the user ID
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User user = userList.get(position);
        holder.friendName.setText(user.getUsername()); // Assuming User has getUsername method

        // Check if the user is already a friend
        boolean isFriend = currentUser.isFriend(user.getUsername()); // Assuming User has isFriend method

        // Set button text based on friendship status
        holder.buttonAction.setText(isFriend ? "Remove" : "Add");

        holder.buttonAction.setOnClickListener(v -> {
            if (isFriend) {
                currentUser.removeFriend(user.getUsername()); // Remove friend
                databaseManager.updateUserFieldById(userId, "friendsList", currentUser.getFriendsList());
                Toast.makeText(v.getContext(), "Removed: " + user.getUsername(), Toast.LENGTH_SHORT).show();
            } else {
                currentUser.addFriend(user.getUsername()); // Add friend
                databaseManager.updateUserFieldById(userId, "friendsList", currentUser.getFriendsList());
                Toast.makeText(v.getContext(), "Added: " + user.getUsername(), Toast.LENGTH_SHORT).show();
            }
            // Re-render based on current view
            if (isShowingMyFriends) {
                showMyFriends(); // Re-filter to show the updated "My Friends" list
            } else {
                notifyItemChanged(position); // Just refresh the item if in "All Friends"
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder class for RecyclerView
    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView friendName; // TextView to show friend's name
        Button buttonAction; // Action button (Add/Remove)

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friend_username); // Adjust according to your layout
            buttonAction = itemView.findViewById(R.id.button_action); // Action button
        }
    }

    // Method to show only the user's friends
    public void showMyFriends() {
        isShowingMyFriends = true; // Track that we are in "My Friends" mode
        List<User> filteredList = userListFull.stream()
                .filter(user -> currentUser.isFriend(user.getUsername()))
                .collect(Collectors.toList());
        applyListChanges(filteredList);
    }

    // Method to show all users
    public void showAllFriends() {
        isShowingMyFriends = false; // Track that we are in "All Users" mode
        applyListChanges(userListFull);
    }

    // Filter method
    public void filter(String text) {
        List<User> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(userListFull); // No filter applied, show full list
        } else {
            String filterPattern = text.toLowerCase().trim();
            for (User user : userListFull) {
                if (user.getUsername().toLowerCase().contains(filterPattern)) {
                    filteredList.add(user);
                }
            }
        }

        // Calculate changes in the list and apply efficient notifications
        applyListChanges(filteredList);
    }

    private void applyListChanges(List<User> newList) {
        for (int i = userList.size() - 1; i >= 0; i--) {
            if (!newList.contains(userList.get(i))) {
                userList.remove(i);
                notifyItemRemoved(i);
            }
        }

        for (int i = 0; i < newList.size(); i++) {
            if (!userList.contains(newList.get(i))) {
                userList.add(i, newList.get(i));
                notifyItemInserted(i);
            }
        }
    }
}
