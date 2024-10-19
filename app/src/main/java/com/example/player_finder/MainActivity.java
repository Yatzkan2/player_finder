package com.example.player_finder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final String PREFS_NAME = "LanguagePrefs";
    private static final String LANGUAGE_KEY = "app_language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.myToolBar);


        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu); // CHANGED THIS LINE
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_language_toggle) {
            // Handle the language toggle button click here
            Toast.makeText(this, "Language toggle clicked", Toast.LENGTH_SHORT).show();
            toggleLanguage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //toggle language functions
    private void toggleLanguage() {
        // Get current language from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String currentLang = prefs.getString(LANGUAGE_KEY, "en"); // Default to English

        // Switch language
        if ("en".equals(currentLang)) {
            setLocale("iw"); // Use "iw" for Hebrew
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

        // Save the selected language to SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LANGUAGE_KEY, lang);
        editor.apply();

        // Restart activity to apply the new language
        recreate(); // CHANGED THIS LINE
    }

    private void loadLanguage() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String language = prefs.getString(LANGUAGE_KEY, "en"); // Default to English
        setLocale(language); // Set the locale based on the stored preference
    }

}