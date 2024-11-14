package com.hami.fresheatables;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private List<Product> filteredProductList = new ArrayList<>();
    private ProductRepo productRepo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productRepo = new ProductRepo(getApplication());

        ImageView profileImageView = findViewById(R.id.profileImage);
        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UserActivity.class);
            startActivity(intent);
        });

        // Initialize RecyclerView and Adapter
        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        productAdapter = new ProductAdapter(this, filteredProductList, this);
        productRecyclerView.setAdapter(productAdapter);

        // Search functionality
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });

        productRepo.getAllProducts().observe(this, products -> {
            if (products != null) {
                productList = products;
                filteredProductList.clear();
                filteredProductList.addAll(products); // Show all products initially
                productAdapter.setProducts(filteredProductList);
                productAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Fetched " + products.size() + " products", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up category buttons in the drawer
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);

        Button vegetablesButton = headerView.findViewById(R.id.vegetablesButton);
        Button fruitsButton = headerView.findViewById(R.id.fruitsButton);
        Button dryFruitsButton = headerView.findViewById(R.id.dryFruitsButton);

        vegetablesButton.setOnClickListener(v -> filterProductsByCategory("Vegetables"));
        fruitsButton.setOnClickListener(v -> filterProductsByCategory("Fruits"));
        dryFruitsButton.setOnClickListener(v -> filterProductsByCategory("Dry Fruits"));
    }

    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        filteredProductList.clear();
        filteredProductList.addAll(filteredList);
        productAdapter.notifyDataSetChanged();
    }

    private void filterProductsByCategory(String category) {
        filteredProductList.clear();
        for (Product product : productList) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filteredProductList.add(product);
            }
        }
        productAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Showing " + category, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("title", product.getTitle());
        intent.putExtra("description", product.getDescription());
        intent.putExtra("price", product.getPrice());
        intent.putExtra("image", product.getImage());
        intent.putExtra("category", product.getCategory());
        startActivity(intent);
    }
}
