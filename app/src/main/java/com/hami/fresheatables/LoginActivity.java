package com.hami.fresheatables;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            String savedUsername = sharedPref.getString("username", "");
            String savedPassword = sharedPref.getString("password", "");

            if (username.equals(savedUsername) && password.equals(savedPassword)) {
                sharedPref.edit().putBoolean("is_logged_in", true).apply();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }else{
                new AlertDialog.Builder(LoginActivity.this).setTitle("Login Failed").setMessage("Invalid username or password").setPositiveButton("OK", null).show();
            }
        });
    }

    // Make sure this method is public and accepts a View parameter
    public void openSignupActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}
