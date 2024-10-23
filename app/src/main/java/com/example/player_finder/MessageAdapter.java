package com.example.player_finder;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final List<Message> messages;
    private final String currentUserId;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public MessageAdapter(String currentUserId) {
        this.messages = new ArrayList<>();
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        boolean isSentByCurrentUser = message.getSenderId().equals(currentUserId);

        // Set message text
        holder.textMessage.setText(message.getText());

        // Set timestamp if available
        if (message.getTimestamp() != null) {
            holder.textTimestamp.setText(dateFormat.format(message.getTimestamp()));
        }

        // Apply styles based on message sender
        if (isSentByCurrentUser) {
            // Sent message style
            holder.textMessage.setBackgroundResource(R.drawable.bg_message_sent);
            holder.messageLayout.setGravity(Gravity.END);
            holder.textTimestamp.setGravity(Gravity.END);
        } else {
            // Received message style
            holder.textMessage.setBackgroundResource(R.drawable.bg_message_received);
            holder.messageLayout.setGravity(Gravity.START);
            holder.textTimestamp.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateMessages(List<Message> newMessages) {
        messages.clear();
        messages.addAll(newMessages);
        notifyDataSetChanged();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        TextView textTimestamp;
        LinearLayout messageLayout;

        MessageViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textTimestamp = itemView.findViewById(R.id.textTimestamp);
            messageLayout = itemView.findViewById(R.id.messageLayout);
        }
    }
}