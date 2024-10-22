package com.example.player_finder;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.CompletableFuture;

public class ProfileFragment extends Fragment {

    private TextView tvUsername, tvEmail;
    private EditText etNewEmail, etNewPassword;
    private Button btnUpdateEmail, btnUpdatePassword;

    private UserSessionManager sessionManager;
    private DatabaseManager databaseManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize session manager and DatabaseManager
        sessionManager = new UserSessionManager(getContext());
        databaseManager = new DatabaseManager();

        // Initialize views
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        etNewEmail = view.findViewById(R.id.etNewEmail);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        btnUpdateEmail = view.findViewById(R.id.btnUpdateEmail);
        btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);

        // Display user data
        displayUserData();

        // Set up button listeners
        btnUpdateEmail.setOnClickListener(v -> updateEmail());
        btnUpdatePassword.setOnClickListener(v -> updatePassword());

        return view;
    }

    private void displayUserData() {
        String username = sessionManager.getUserName();
        String email = sessionManager.getEmail();

        tvUsername.setText(username);
        tvEmail.setText(email);
    }

    private void updateEmail() {
        String newEmail = etNewEmail.getText().toString().trim();
        if (TextUtils.isEmpty(newEmail)) {
            etNewEmail.setError("Email is required");
            return;
        }

        // Update email in Firestore
        CompletableFuture<Void> updateEmailFuture = databaseManager.updateUserFieldById(sessionManager.getUserId(), "email", newEmail);
        updateEmailFuture.thenAccept(aVoid -> {
            // Update email locally in session
            sessionManager.saveUserSession(sessionManager.getUserId(), newEmail, sessionManager.getUserName());
            getActivity().runOnUiThread(() -> {
                tvEmail.setText(newEmail);
                Toast.makeText(getActivity(), "Email updated successfully", Toast.LENGTH_SHORT).show();
            });
        }).exceptionally(e -> {
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to update email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            return null;
        });
    }

    private void updatePassword() {
        String newPassword = etNewPassword.getText().toString().trim();
        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError("Password is required");
            return;
        }

        // Update password in Firestore
        CompletableFuture<Void> updatePasswordFuture = databaseManager.updateUserFieldById(sessionManager.getUserId(), "password", newPassword);
        updatePasswordFuture.thenAccept(aVoid -> {
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_SHORT).show());
        }).exceptionally(e -> {
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to update password: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            return null;
        });
    }
}
