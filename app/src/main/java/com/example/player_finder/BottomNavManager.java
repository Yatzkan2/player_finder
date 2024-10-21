package com.example.player_finder;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentManager;

public class BottomNavManager {

    private final FragmentNavigator fragmentNavigator;

    public BottomNavManager(FragmentManager fragmentManager, int containerId) {
        this.fragmentNavigator = new FragmentNavigatorImpl(fragmentManager, containerId);
    }

    public void setupBottomNavigation(BottomNavigationView bottomNav) {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                fragmentNavigator.navigateTo(new ProfileFragment());
            } else if (itemId == R.id.nav_my_games) {
                fragmentNavigator.navigateTo(new MyGamesFragment());
            } else if (itemId == R.id.nav_friends) {
                fragmentNavigator.navigateTo(new FriendsFragment());
            }
            return true;
        });
    }
}
