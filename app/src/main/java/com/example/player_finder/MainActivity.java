package com.example.player_finder;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;  // Ensure this is imported
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarManager appBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize AppBarManager with "true" indicating this is MainActivity
        Toolbar toolbar = findViewById(R.id.myToolBar);
        appBarManager = new AppBarManager(this, true);
        appBarManager.setupAppBar(toolbar);  // Set up the AppBar

        // Initialize BottomNavManager locally
        BottomNavManager bottomNavManager = new BottomNavManager(getSupportFragmentManager(), R.id.frame_layout);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNavManager.setupBottomNavigation(bottomNav);  // Set up BottomNavigationView

        // Default fragment on initial load
        if (savedInstanceState == null) {
            bottomNavManager.setupBottomNavigation(bottomNav);  // Load the default fragment
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return appBarManager.onCreateOptionsMenu(menu);  // Delegate menu inflation to AppBarManager
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return appBarManager.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);  // Delegate to AppBarManager
    }
}
