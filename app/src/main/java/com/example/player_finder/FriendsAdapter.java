package com.example.player_finder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private final List<User> friendsList;
    private final OnFriendRemoveListener removeListener;

    public FriendsAdapter(List<User> friendsList, OnFriendRemoveListener removeListener) {
        this.friendsList = friendsList;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        User friend = friendsList.get(position);
        holder.usernameTextView.setText(friend.getUsername());

        // Remove friend listener
        holder.itemView.setOnLongClickListener(v -> {
            removeListener.onFriendRemove(friend);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public void addFriend(User newFriend) {
        friendsList.add(newFriend);
        notifyItemInserted(friendsList.size() - 1); // Efficiently notify that a new item was added
    }

    public void removeFriend(User friend) {
        int position = friendsList.indexOf(friend);
        if (position != -1) {
            friendsList.remove(position);
            notifyItemRemoved(position); // Efficiently notify that an item was removed
        }
    }

    public void updateList(List<User> updatedList) {
        this.friendsList.clear();
        this.friendsList.addAll(updatedList);
        notifyItemRangeChanged(0, friendsList.size()); // Efficiently notify a range of items was updated
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username_text_view);
        }
    }

    public interface OnFriendRemoveListener {
        void onFriendRemove(User user);
    }
}
