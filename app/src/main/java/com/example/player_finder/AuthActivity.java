package com.example.player_finder;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity {

    private FragmentNavigator fragmentNavigator;
    private LanguageManager languageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_auth_activity);

        // COMMENT OUT THESE LINES AFTER A SINGLE SEEDING OF THE DATABASE
        //###############################################
        //DatabaseManager dbManager = new DatabaseManager();
        //dbManager.seedDatabase();
        //###############################################

        // Initialize LanguageManager
        languageManager = new LanguageManagerImpl(this);

        // Set up the fragment navigator for switching between login and register fragments
        fragmentNavigator = new FragmentNavigatorImpl(getSupportFragmentManager(), R.id.auth_frame_container);

        // Show LoginFragment by default
        fragmentNavigator.navigateTo(new LoginFragment());

        // Set up click listener for language icon
        ImageView languageIcon = findViewById(R.id.language_icon);
        languageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageManager.toggleLanguage();
                recreate(); // Recreate activity to apply the language change
            }
        });
    }

    public void showLoginFragment() {
        if (fragmentNavigator != null) {
            fragmentNavigator.navigateTo(new LoginFragment());
        }
    }

    public void showRegisterFragment() {
        if (fragmentNavigator != null) {
            fragmentNavigator.navigateTo(new RegisterFragment());
        }
    }
}
