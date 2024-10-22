package com.example.player_finder;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;  // Import MenuItem
import androidx.annotation.NonNull;  // Import NonNull annotation
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AuthActivity extends AppCompatActivity {

    private FragmentNavigator fragmentNavigator;
    private AppBarManager appBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_auth_activity);

        // COMMENT OUT THESE LINES AFTER A SINGLE SEEDING OF THE DATABASE
        //###############################################
        //DatabaseManager dbManager = new DatabaseManager();
        //dbManager.seedDatabase();
        //###############################################

        // Initialize AppBarManager with "false" indicating this is AuthActivity
        Toolbar toolbar = findViewById(R.id.myToolBar);
        appBarManager = new AppBarManager(this, false);
        appBarManager.setupAppBar(toolbar);  // Set up the AppBar

        // Set up the fragment navigator for switching between login and register fragments
        fragmentNavigator = new FragmentNavigatorImpl(getSupportFragmentManager(), R.id.auth_frame_container);

        // Show LoginFragment by default
        fragmentNavigator.navigateTo(new LoginFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return appBarManager.onCreateOptionsMenu(menu);  // Delegate menu inflation to AppBarManager
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return appBarManager.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);  // Delegate to AppBarManager
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
