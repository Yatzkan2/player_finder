package com.example.player_finder;

import java.util.List;

import java.util.UUID;

public class Chat {
    private String chatId; // The unique ID of the chat
    private List<String> participants; // List of participant user IDs
    private String lastMessage; // The last message sent in this chat
    private String lastMessageTimestamp; // Timestamp of the last message sent

    // Constructor generates a new chatId automatically
    public Chat(List<String> participants, String lastMessage, String lastMessageTimestamp) {
        this.chatId = UUID.randomUUID().toString(); // Generate unique chatId
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    // Getter and setter methods
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(String lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}



