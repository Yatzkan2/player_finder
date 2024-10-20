package com.example.player_finder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends BaseActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_submit_button);
        firestore = FirebaseFirestore.getInstance();

        // Set button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        // Get user input
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(email, password)) {
            return; // Exit if validation fails
        }

        // Query Firestore to find user by email
        firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.firestore.QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                String storedPassword = document.getString("password");
                                if (password.equals(storedPassword)) {
                                    // Login success
                                    showSuccessMessage();
                                    // Redirect to MainPageActivity
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class)); //should be changed to Home page
                                    finish(); // Optional: Close LoginActivity
                                } else {
                                    showErrorMessage("Incorrect password!");
                                }
                            } else {
                                showErrorMessage("No user found with this email!");
                            }
                        } else {
                            showErrorMessage("Login failed. Try again later.");
                        }
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
        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
