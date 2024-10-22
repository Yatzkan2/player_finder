package com.example.player_finder;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {
    private static final String PREFERENCES_NAME = "UserSession";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserSessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save user session
    public void saveUserSession(String userId, String email, String username) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USERNAME, username);
        editor.apply(); // Apply changes
    }

    // Get the logged-in user's ID
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    // Get the logged-in user's name
    public String getUserName() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    // Get the logged-in user's email
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    // Clear user session (used for logout)
    public void clearSession() {
        editor.clear(); // Clear all data
        editor.apply();
    }

    // Check if a user is logged in
    public boolean isUserLoggedIn() {
        return getUserId() != null;
    }
}
