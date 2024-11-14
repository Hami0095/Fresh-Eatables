package com.hami.fresheatables;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class UserActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, phoneEditText, addressEditText;
    private Button updateButton, convertToVendorButton, uploadProductButton;
    private ImageView profileImageView;

    private CustomerRepository customerRepo;
    private VendorRepository vendorRepo;
    private Customer currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Initialize UI components
        usernameEditText = findViewById(R.id.usernameEditText1);
        emailEditText = findViewById(R.id.emailEditText1);
        profileImageView = findViewById(R.id.profileImageView1);
        updateButton = findViewById(R.id.updateButton1);
        convertToVendorButton = findViewById(R.id.convertToVendorButton1);
        uploadProductButton = findViewById(R.id.uploadProductButton);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);

        // Initialize CustomerRepo and load current user data
        customerRepo = new CustomerRepository(getApplication());
        vendorRepo = new VendorRepository(getApplication());
        loadCurrentUser();

        updateButton.setOnClickListener(v -> updateUserProfile());
        convertToVendorButton.setOnClickListener(v -> convertToVendor());
        uploadProductButton.setOnClickListener(v -> navigateToVendorActivity());
    }

    // Method to update user profile
    private void updateUserProfile() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setName(username);
        currentUser.setEmail(email);
        customerRepo.update(currentUser);
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }

    // Method to convert to vendor account
    private void convertToVendor() {
        String username = currentUser.getName();
        String password = currentUser.getPassword();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if (isUsernameAndPasswordCorrect(username, password)) {
            // Create a new Vendor entity with current user details
            Vendor newVendor = new Vendor(
                    currentUser.getName(),
                    currentUser.getPassword(),
                    address,
                    currentUser.getProfilePicture(), // Assuming you have a method to get profile picture from Customer
                    phone
            );

            // Insert new Vendor in database
            vendorRepo.insert(newVendor);

            // Update current customer status to vendor
            currentUser.setVendor(true);
            customerRepo.update(currentUser);
            loadCurrentUser();  // Re-load user to reflect changes
            Toast.makeText(this, "Converted to vendor successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUsernameAndPasswordCorrect(String username, String password) {
        return username.equals(currentUser.getName()) && password.equals(currentUser.getPassword());
    }

    // Method to load current user data and adjust UI accordingly
    private void loadCurrentUser() {
        String currentUserName = getCurrentUserName();
        if (currentUserName == null) {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        customerRepo.getCustomerByName(currentUserName).observe(this, customer -> {
            if (customer != null) {
                currentUser = customer;
                usernameEditText.setText(customer.getName());
                emailEditText.setText(customer.getEmail());

                // Show or hide the Convert to Vendor button and other UI elements based on isVendor status
                if (customer.isVendor()) {
                    convertToVendorButton.setVisibility(View.GONE);
                    uploadProductButton.setVisibility(View.VISIBLE);
                    phoneEditText.setVisibility(View.VISIBLE);
                    addressEditText.setVisibility(View.VISIBLE);
                } else {
                    convertToVendorButton.setVisibility(View.VISIBLE);
                    uploadProductButton.setVisibility(View.GONE);
                    phoneEditText.setVisibility(View.GONE);
                    addressEditText.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(this, "User details not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentUserName() {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPref.getString("username", null);
    }

    private void navigateToVendorActivity() {
        // Pass vendor information (for now, use current user's name and description)
        Intent intent = new Intent(UserActivity.this, VendorActivity.class);
        intent.putExtra("vendorName", currentUser.getName());
        intent.putExtra("vendorDescription", currentUser.getEmail());
        intent.putExtra("vendorPhone", phoneEditText.getText().toString());
        intent.putExtra("vendorAddress", addressEditText.getText().toString());
        intent.putExtra("vendorId", currentUser.getId());
        startActivity(intent);
    }

}
