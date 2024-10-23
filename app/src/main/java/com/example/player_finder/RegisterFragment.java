package com.example.player_finder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterFragment extends Fragment {

    private EditText usernameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private FirebaseFirestore firestore;
    private UserSessionManager sessionManager;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register, container, false);

        // Initialize views
        usernameInput = view.findViewById(R.id.register_username_input);
        emailInput = view.findViewById(R.id.register_email_input);
        passwordInput = view.findViewById(R.id.register_password_input);
        confirmPasswordInput = view.findViewById(R.id.register_confirm_password_input);
        Button registerButton = view.findViewById(R.id.register_submit_button);
        Button goToLoginButton = view.findViewById(R.id.go_to_login_button);

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();
        sessionManager = new UserSessionManager(getContext());

        // Set register button click listener
        registerButton.setOnClickListener(v -> registerUser());

        // Navigate to login fragment
        goToLoginButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((AuthActivity) getActivity()).showLoginFragment();
            }
        });

        return view;
    }

    private void registerUser() {
        // Validate inputs
        if (!validateInputs()) {
            return; // Exit if validation fails
        }

        // Get user input
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Create a User object
        User user = new User(username, email, password); // Avoid storing raw passwords in production

        // Add user to Firebase
        firestore.collection("users").add(user)
                .addOnSuccessListener(documentReference -> {
                    // Save user session
                    userId = documentReference.getId();
                    sessionManager.saveUserSession(userId, email, username);
                    showSuccessMessage();
                    // Navigate to MainActivity
                    if (getActivity() != null) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();  // Close AuthActivity
                    }
                })
                .addOnFailureListener(e -> showErrorMessage("Failure: " + e.getMessage()));
    }

    private boolean validateInputs() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorMessage("All fields are required!");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showErrorMessage("Invalid email format!");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showErrorMessage("Passwords do not match!");
            return false;
        }

        return true; // All validations passed
    }

    private void showSuccessMessage() {
        Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
