package com.example.player_finder;

import android.content.Intent;  // Add this import for Intent
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AppBarManager {

    private final AppCompatActivity activity;  // Cast directly to AppCompatActivity
    private final boolean isMainActivity;
    private final LanguageManager languageManager;
    private UserSessionManager sessionManager;

    public AppBarManager(AppCompatActivity activity, boolean isMainActivity) {
        this.activity = activity;
        this.isMainActivity = isMainActivity;
        this.languageManager = new LanguageManagerImpl(activity);  // Initialize LanguageManager internally
        this.languageManager.loadLanguage();  // Load saved language preference
        this.sessionManager = new UserSessionManager(activity);
    }

    // Set up the toolbar as the action bar and configure it
    public void setupAppBar(Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);  // No need for casting anymore

        // Ensure the title is visible
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
            activity.setTitle(R.string.app_name);  // Set the app name as the title
        }
    }

    // Inflate the correct menu based on whether it's MainActivity or AuthActivity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isMainActivity) {
            // Inflate menu with logout option in MainActivity
            activity.getMenuInflater().inflate(R.menu.appbar_menu, menu);
        } else {
            // Inflate menu without logout in AuthActivity
            activity.getMenuInflater().inflate(R.menu.appbar_menu_auth, menu);
        }
        return true;
    }

    // Handle menu item selections (both language toggle and logout)
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_language_toggle) {
            toggleLanguage();
            return true;
        } else if (isMainActivity && item.getItemId() == R.id.action_logout) {
            handleLogout();
            return true;
        }
        return false;
    }

    // Method to toggle the language
    private void toggleLanguage() {
        languageManager.toggleLanguage();
        activity.recreate();  // Recreate the activity to apply new language
    }

    // Method to handle logout action
    private void handleLogout() {
        sessionManager.clearSession();

        Intent intent = new Intent(activity, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();  // Close the current MainActivity
    }
}
