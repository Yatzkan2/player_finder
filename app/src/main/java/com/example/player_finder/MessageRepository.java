package com.example.player_finder;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MessageRepository {
    private static final String TAG = "MessageRepository";
    private final FirebaseFirestore db;
    private ListenerRegistration messagesListener;

    public MessageRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    // Send a new message
    public CompletableFuture<Void> sendMessage(String senderId, String receiverId, String text) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Map<String, Object> message = new HashMap<>();
        message.put("senderId", senderId);
        message.put("receiverId", receiverId);
        message.put("text", text);
        message.put("timestamp", new Date());
        message.put("isRead", false);

        db.collection("messages")
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Message sent successfully");
                    future.complete(null);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error sending message", e);
                    future.completeExceptionally(e);
                });

        return future;
    }

    // Get all messages between two users with real-time updates
    public void getMessages(String userId1, String userId2, OnMessagesLoadedListener listener) {
        // Create a query that gets messages in both directions
        Query query = db.collection("messages")
                .whereIn("senderId", List.of(userId1, userId2))
                .whereIn("receiverId", List.of(userId1, userId2))
                .orderBy("timestamp", Query.Direction.ASCENDING);

        // Remove any existing listener
        if (messagesListener != null) {
            messagesListener.remove();
        }

        // Add real-time listener
        messagesListener = query.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.e(TAG, "Listen failed.", e);
                listener.onError(e);
                return;
            }

            List<Message> messages = new ArrayList<>();
            if (snapshots != null) {
                for (QueryDocumentSnapshot doc : snapshots) {
                    Message message = doc.toObject(Message.class);
                    messages.add(message);
                }
            }
            listener.onMessagesLoaded(messages);
        });
    }

    // Mark a message as read
    public CompletableFuture<Void> markMessageAsRead(String messageId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        db.collection("messages")
                .document(messageId)
                .update("isRead", true)
                .addOnSuccessListener(aVoid -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    // Clean up listener when no longer needed
    public void removeListener() {
        if (messagesListener != null) {
            messagesListener.remove();
        }
    }

    // Interface for message loading callback
    public interface OnMessagesLoadedListener {
        void onMessagesLoaded(List<Message> messages);
        void onError(Exception e);
    }
}