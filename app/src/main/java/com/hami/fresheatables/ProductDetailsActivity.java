package com.hami.fresheatables;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productTitleTextView, productPriceTextView, productCategoryTextView, productDescriptionTextView;
    private Button buyProductButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize views
        productImageView = findViewById(R.id.productImageView1);
        productTitleTextView = findViewById(R.id.productTitleTextView1);
        productPriceTextView = findViewById(R.id.productPriceTextView1);
        productCategoryTextView = findViewById(R.id.productCategoryTextView1);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView1);
        buyProductButton = findViewById(R.id.buyProductButton1);

        // Get product details from intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            double price = intent.getDoubleExtra("price", 0.0);
            String imageUrl = intent.getStringExtra("image");
            String category = intent.getStringExtra("category");

            // Set product details in views
            productTitleTextView.setText(title);
            productPriceTextView.setText("Price: $" + price);
            productCategoryTextView.setText("Category: " + category);
            productDescriptionTextView.setText(description);

            // Load product image
            Glide.with(this).load(imageUrl).into(productImageView);
        }

        // Set buy button listener
        buyProductButton.setOnClickListener(v -> showBuyConfirmationDialog());
    }

    private void showBuyConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Purchase")
                .setMessage("Do you want to buy this product?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Handle purchase confirmation
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
