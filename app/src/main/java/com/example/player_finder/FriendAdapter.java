package com.example.player_finder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private final List<User> userList;
    private final List<User> userListFull; // To keep a copy of the full list for filtering

    // Constructor
    public FriendAdapter(List<User> userList) {
        this.userList = userList;
        this.userListFull = new ArrayList<>(userList); // Create a copy of the full list
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
        holder.usernameText.setText(user.getUsername());
        holder.emailText.setText(user.getEmail());

        // Add click listener for itemView
        holder.itemView.setOnClickListener(v -> {
            // Display Toast with the user's name when clicked
            Toast.makeText(v.getContext(), "Clicked: " + user.getUsername(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder class for RecyclerView
    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView emailText;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.friend_username);
            emailText = itemView.findViewById(R.id.friend_email);
        }
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
