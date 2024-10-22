package com.example.player_finder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final Context context;
    private final List<Chat> chatList;

    // Constructor for ChatAdapter
    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    // Create a new ViewHolder that holds the item layout (item_chat.xml)
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    // Bind data to the ViewHolder to display the information in each row
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        // For simplicity, joining the participants list as a string
        holder.textViewParticipants.setText(String.join(", ", chat.getParticipants()));

        // Display the last message of the chat
        holder.textViewLastMessage.setText(chat.getLastMessage());

        // Handle click event to open the chat activity when the item is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatsActivity.class);
            intent.putExtra("chatId", chat.getChatId());
            context.startActivity(intent);
        });
    }

    // Get the number of items in the list
    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // Define the ViewHolder for the adapter
    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView textViewParticipants, textViewLastMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewParticipants = itemView.findViewById(R.id.textViewParticipants);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
        }
    }
}
