package com.example.player_finder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private MessageRepository messageRepository;
    private EditText messageInput;
    private Button sendButton;
    private ProgressBar progressBar;
    private String senderId;
    private String receiverId;
    private String receiverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize Views
        initializeViews();

        // Get Intent Data
        getIntentData();

        // Setup UI Components
        setupToolbar();
        setupRecyclerView();
        setupSendButton();

        // Load Initial Messages
        loadMessages();

        // Load Receiver's Name
        loadReceiverName();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewMessages);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void getIntentData() {
        senderId = getIntent().getStringExtra("senderId");
        receiverId = getIntent().getStringExtra("receiverId");

        if (senderId == null || receiverId == null) {
            Log.e(TAG, "Missing required IDs - closing activity");
            Toast.makeText(this, "Error: Missing user information", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Chat");
        }
    }

    private void setupRecyclerView() {
        messageAdapter = new MessageAdapter(senderId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Messages start from bottom
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(messageAdapter);
        messageRepository = new MessageRepository();
    }

    private void setupSendButton() {
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadReceiverName() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(receiverId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        receiverName = documentSnapshot.getString("username");
                        if (getSupportActionBar() != null && receiverName != null) {
                            getSupportActionBar().setTitle(receiverName);
                        }
                    }
                });
    }

    private void loadMessages() {
        progressBar.setVisibility(View.VISIBLE);
        messageRepository.getMessages(senderId, receiverId, new MessageRepository.OnMessagesLoadedListener() {
            @Override
            public void onMessagesLoaded(List<Message> messages) {
                messageAdapter.updateMessages(messages);
                scrollToBottom();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error loading messages", e);
                Toast.makeText(ChatActivity.this,
                        "Error loading messages", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (messageText.isEmpty()) {
            return;
        }

        sendButton.setEnabled(false);
        messageRepository.sendMessage(senderId, receiverId, messageText)
                .thenRun(() -> {
                    runOnUiThread(() -> {
                        messageInput.setText("");
                        scrollToBottom();
                        sendButton.setEnabled(true);
                    });
                    loadMessages();
                })
                .exceptionally(e -> {
                    runOnUiThread(() -> {
                        Toast.makeText(ChatActivity.this,
                                "Error sending message", Toast.LENGTH_SHORT).show();
                        sendButton.setEnabled(true);
                    });
                    return null;
                });
    }

    private void scrollToBottom() {
        if (messageAdapter.getItemCount() > 0) {
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messageRepository.removeListener();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}