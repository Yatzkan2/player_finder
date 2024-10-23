package com.example.player_finder;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private final List<Message> messages;
    private final String currentUserId;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public ChatAdapter(String currentUserId) {
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
        boolean isSentByMe = message.getSenderId().equals(currentUserId);

        // Set message text
        holder.textMessage.setText(message.getText());

        // Set timestamp if available
        if (message.getTimestamp() != null) {
            holder.textTimestamp.setText(dateFormat.format(message.getTimestamp()));
        }

        // Adjust layout based on message sender
        ConstraintLayout.LayoutParams messageParams =
                (ConstraintLayout.LayoutParams) holder.textMessage.getLayoutParams();
        ConstraintLayout.LayoutParams timestampParams =
                (ConstraintLayout.LayoutParams) holder.textTimestamp.getLayoutParams();

        if (isSentByMe) {
            // Sent message styling
            holder.textMessage.setBackgroundResource(R.drawable.bg_message_sent);
            messageParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            messageParams.startToStart = ConstraintLayout.LayoutParams.UNSET;
            timestampParams.endToEnd = holder.textMessage.getId();
            timestampParams.startToStart = ConstraintLayout.LayoutParams.UNSET;
        } else {
            // Received message styling
            holder.textMessage.setBackgroundResource(R.drawable.bg_message_received);
            messageParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            messageParams.endToEnd = ConstraintLayout.LayoutParams.UNSET;
            timestampParams.startToStart = holder.textMessage.getId();
            timestampParams.endToEnd = ConstraintLayout.LayoutParams.UNSET;
        }

        holder.textMessage.setLayoutParams(messageParams);
        holder.textTimestamp.setLayoutParams(timestampParams);
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

    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        TextView textTimestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textTimestamp = itemView.findViewById(R.id.textTimestamp);
        }
    }
}