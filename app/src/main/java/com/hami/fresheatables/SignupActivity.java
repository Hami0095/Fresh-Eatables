package com.hami.fresheatables;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText;
    private CustomerRepository customerRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        customerRepo = new CustomerRepository(getApplication());
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username", username);
                editor.putString("password", password);
                editor.putBoolean("is_logged_in", true);
                editor.apply();
                // Adding data to the Database too:
                Customer newCustomer = new Customer(username, email, password, "", "");
                customerRepo.insert(newCustomer);
                startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}
