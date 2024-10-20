package com.example.player_finder;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private LanguageManager languageManager;
    private FragmentNavigator fragmentNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up AppBar (Toolbar)
        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);

        // Set up Language Manager
        languageManager = new LanguageManagerImpl(this);
        languageManager.loadLanguage(); // Load saved language preference

        // Set up Fragment Navigator
        fragmentNavigator = new FragmentNavigatorImpl(getSupportFragmentManager(), R.id.frame_layout);
        if (savedInstanceState == null) {
            fragmentNavigator.navigateTo(new ProfileFragment()); // Default fragment
        }

        // Set up BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_profile) {
                fragmentNavigator.navigateTo(new ProfileFragment());
            } else if (item.getItemId() == R.id.nav_my_games) {
                fragmentNavigator.navigateTo(new MyGamesFragment());
            } else if (item.getItemId() == R.id.nav_friends) {
                fragmentNavigator.navigateTo(new FriendsFragment());
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_language_toggle) {
            languageManager.toggleLanguage(); // Toggle language
            recreate(); // Recreate activity to apply new language
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
