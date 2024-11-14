// VendorActivity.java (updated)
package com.hami.fresheatables;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VendorActivity extends AppCompatActivity {

    private EditText productTitleEditText, productDescriptionEditText, productPriceEditText;
    private Spinner productCategorySpinner;
    private ImageView productImageView;
    private Button uploadProductButton;
    private Uri productImageUri;
    private static final int PICK_IMAGE = 1;

    // Declare ProductService instead of ProductRepo
    private ProductService productService;
    private ProductRepo productRepo;
    private int vendorId;
    private Vendor currentVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        // Initialize repository
        AppDatabase appDatabase = AppDatabase.getDatabase(this);  // Assuming this method gives you the AppDatabase instance
        productService = new ProductService(appDatabase.productDao(), appDatabase.vendorDao());

        loadCurrentVendor();

        // Initialize UI components
        productTitleEditText = findViewById(R.id.productTitleEditText2);
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText2);
        productPriceEditText = findViewById(R.id.productPriceEditText2);
        productCategorySpinner = findViewById(R.id.productCategorySpinner2);
        productImageView = findViewById(R.id.productImageView2);
        uploadProductButton = findViewById(R.id.uploadProductButton2);

        // Product Category Setup
        String[] categories = {"Vegetables", "Fruits", "Dry Fruits"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productCategorySpinner.setAdapter(adapter);

        // Image view
        productImageView.setOnClickListener(v -> openGallery());

        uploadProductButton.setOnClickListener(v -> {
            String title = productTitleEditText.getText().toString().trim();
            String description = productDescriptionEditText.getText().toString().trim();
            double price = Double.parseDouble(productPriceEditText.getText().toString().trim());
            String selectedCategory = productCategorySpinner.getSelectedItem().toString();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || price <= 0) {
                Toast.makeText(VendorActivity.this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = new Product(title, description, price, "default_image_url", selectedCategory, 1); // Assume vendor ID = 1

            // Insert the product into the database
            productService.insertVendorAndProduct(currentVendor, product);

            Log.d("VendorActivity", "Product added: " + title);

            Toast.makeText(VendorActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

    }

    private void loadCurrentVendor() {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = sharedPref.getString("username", null);

        if (username != null) {
            AppDatabase appDatabase = AppDatabase.getDatabase(this);  // Assuming this method gives you the AppDatabase instance
            VendorDao vendorDao = appDatabase.vendorDao();
            vendorDao.getVendorByUsername(username).observe(this, vendor -> {
                if (vendor != null) {
                    currentVendor = vendor;
                    Log.d("VendorActivity", "Current Vendor: " + vendor.getUsername());
                } else {
                    Log.e("VendorActivity", "Vendor not found for username: " + username);
                    Toast.makeText(VendorActivity.this, "Vendor not found", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            productImageUri = data.getData();
            productImageView.setImageURI(productImageUri);
        }
    }

    private void uploadProduct() {
        String title = productTitleEditText.getText().toString();
        String description = productDescriptionEditText.getText().toString();
        String category = productCategorySpinner.getSelectedItem().toString();

        double price;
        try {
            price = Double.parseDouble(productPriceEditText.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (productImageUri == null) {
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product(title, description, price, productImageUri.toString(), category, vendorId);
        productRepo.insert(product);

        Toast.makeText(this, "Product uploaded successfully", Toast.LENGTH_SHORT).show();

        // Clear the fields after upload
        clearFields();
    }

    private void clearFields() {
        productTitleEditText.setText("");
        productDescriptionEditText.setText("");
        productPriceEditText.setText("");
        productCategorySpinner.setSelection(0);
        productImageView.setImageResource(R.drawable.user);  // Placeholder image
        productImageUri = null;
    }
}
