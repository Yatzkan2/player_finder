package com.example.player_finder;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class AuthActivity extends AppCompatActivity {

    private FragmentNavigator fragmentNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_auth_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentNavigator = new FragmentNavigatorImpl(fragmentManager, R.id.auth_frame_container);

        // Show LoginFragment by default
        fragmentNavigator.navigateTo(new LoginFragment());
    }

    public void showLoginFragment() {
        fragmentNavigator.navigateTo(new LoginFragment());
    }

    public void showRegisterFragment() {
        fragmentNavigator.navigateTo(new RegisterFragment());
    }
}
