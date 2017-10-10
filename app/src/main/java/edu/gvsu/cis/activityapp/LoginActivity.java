package edu.gvsu.cis.activityapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText textEmail, textPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        textEmail = (EditText) findViewById(R.id.textEmail);
        textPassword = (EditText) findViewById(R.id.textPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener((click) -> {
            loginUser(textEmail.getText().toString(), textPassword.getText().toString());
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Transition to the main UI.
            openMainView(currentUser);
        } else {
            // Continue with this activity.
            Toast.makeText(LoginActivity.this, "User is null.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser(String email, String password) {
        if (validCredentials(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((@NonNull Task<AuthResult> task) -> {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    // Transition to the main UI.
                    openMainView(currentUser);
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "Invalid credentials.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openMainView(FirebaseUser user) {
        System.out.println("User is logged in!");
    }

    private boolean validCredentials(String email, String password) {
        // TODO - Test email regex and make sure password is valid.
        return email != null && email.length() > 0 && password != null && password.length() > 0;
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((@NonNull Task<AuthResult> task) -> {
            if (task.isSuccessful()) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                // Transition to the main UI.
                openMainView(currentUser);
            } else {
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
