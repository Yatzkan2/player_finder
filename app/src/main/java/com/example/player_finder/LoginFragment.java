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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {

    private EditText emailInput;
    private EditText passwordInput;
    private FirebaseFirestore firestore;
    private UserSessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        // Initialize views
        emailInput = view.findViewById(R.id.email_input);
        passwordInput = view.findViewById(R.id.password_input);
        Button loginButton = view.findViewById(R.id.login_submit_button);
        Button goToRegisterButton = view.findViewById(R.id.go_to_register_button);

        // Initialize Firestore and UserSessionManager
        firestore = FirebaseFirestore.getInstance();
        sessionManager = new UserSessionManager(getContext());

        // Set login button click listener
        loginButton.setOnClickListener(v -> loginUser());

        // Navigate to register fragment
        goToRegisterButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((AuthActivity) getActivity()).showRegisterFragment();
            }
        });

        return view;
    }

    private void loginUser() {
        // Get user input
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(email, password)) {
            return; // Exit if validation fails
        }

        // Query to firebase to find user by email
        firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String storedPassword = document.getString("password");
                        String username = document.getString("username");
                        String userId = document.getId();

                        if (password.equals(storedPassword)) {
                            // Login success
                            showSuccessMessage();
                            // Save user session
                            sessionManager.saveUserSession(userId, email, username);

                            // Navigate to MainActivity
                            if (getActivity() != null) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();  // Close AuthActivity
                            }
                        } else {
                            showErrorMessage("Incorrect password!");
                        }
                    } else {
                        showErrorMessage("No user found with this email!");
                    }
                });
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showErrorMessage("Both fields are required!");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showErrorMessage("Invalid email format!");
            return false;
        }

        return true;
    }

    private void showSuccessMessage() {
        Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
