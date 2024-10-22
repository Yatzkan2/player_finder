package com.example.player_finder;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChats;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        // Initialize RecyclerView
        recyclerViewChats = findViewById(R.id.recyclerViewChats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));

        // Create some dummy data for chatList
        chatList = new ArrayList<>();
        loadDummyChats(); // Load some dummy chats

        // Set up the adapter with the dummy chat list
        chatAdapter = new ChatAdapter(this, chatList);
        recyclerViewChats.setAdapter(chatAdapter);
    }

    // Method to load some dummy chat data
    private void loadDummyChats() {
        chatList.add(new Chat(Arrays.asList("User1", "User2"), "Hey, how are you?", "2024-10-22T10:00:00Z"));
        chatList.add(new Chat(Arrays.asList("User3", "User4"), "Let's meet up tomorrow.", "2024-10-22T11:00:00Z"));
        chatList.add(new Chat(Arrays.asList("User5", "User6"), "Where are you?", "2024-10-22T12:00:00Z"));
        chatList.add(new Chat(Arrays.asList("User7", "User8"), "I am on my way.", "2024-10-22T13:00:00Z"));
        chatList.add(new Chat(Arrays.asList("User9", "User10"), "Sounds great!", "2024-10-22T14:00:00Z"));
    }
}
