package com.example.player_finder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private UserSessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize session manager
        sessionManager = new UserSessionManager(requireContext());

        // Retrieve user data from the session
        String userName = sessionManager.getUserName();

        // Find the TextView and set the user's name
        TextView userNameTextView = view.findViewById(R.id.username_textview);
        userNameTextView.setText(userName);

        return view;
    }
}
