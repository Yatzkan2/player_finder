package com.example.player_finder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "LanguagePrefs";
    private static final String LANGUAGE_KEY = "app_language";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupToolbar(int toolbarId) {
        toolbar = findViewById(toolbarId);
        setSupportActionBar(toolbar); // Defines the custom toolbar as the action bar
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_language_toggle) {
            // Handle language toggle
            String message = getString(R.string.language_toggle_clicked);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            toggleLanguage();
            return true;
        } else if (item.getItemId() == R.id.action_menu) {
            Intent intent = new Intent(this, MainActivity.class); // CHANGED THIS LINE
            startActivity(intent); // CHANGED THIS LINE
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Language toggle functions
    private void toggleLanguage() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String currentLang = prefs.getString(LANGUAGE_KEY, "en"); // Default to English

        if ("en".equals(currentLang)) {
            setLocale("iw"); // Switch to Hebrew
            Toast.makeText(this, "Language switched to Hebrew", Toast.LENGTH_SHORT).show();
        } else {
            setLocale("en"); // Switch back to English
            Toast.makeText(this, "Language switched to English", Toast.LENGTH_SHORT).show();
        }
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        getResources().getConfiguration().setLocale(locale);
        getResources().updateConfiguration(getResources().getConfiguration(), getResources().getDisplayMetrics());

        // Save language to SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LANGUAGE_KEY, lang);
        editor.apply();

        recreate(); // Restart activity to apply the new language
    }

    protected void loadLanguage() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String language = prefs.getString(LANGUAGE_KEY, "en"); // Default to English
        setLocale(language);
    }
}
