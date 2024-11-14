// ProductRepo.java
package com.hami.fresheatables;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepo {
    private final ProductDao productDao;
    private VendorDao vendorDao;
    private final ExecutorService executorService;

    public ProductRepo(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        productDao = db.productDao();
        vendorDao = db.vendorDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Insert a product
    public void insert(Product product) {
        // First, check if the vendor exists
        Vendor vendor = vendorDao.getVendorById(product.getVendorId());
        if (vendor != null) {
            // If vendor exists, insert the product
            productDao.insert(product);
        } else {
            // Log error or show message that the vendor doesn't exist
            Log.e("ProductRepo", "No Vendor found with ID: " + product.getVendorId());
        }
    }

    // Update a product
    public void update(Product product) {
        executorService.execute(() -> productDao.update(product));
    }

    // Delete a product
    public void delete(Product product) {
        executorService.execute(() -> productDao.delete(product));
    }

    // Get a product by its ID
    public LiveData<Product> getProductById(int id) {
        return productDao.getProductById(id);
    }

    // Get all products by a specific vendor
    public LiveData<List<Product>> getProductsByVendor(int vendorId) {
        return productDao.getProductsByVendor(vendorId);
    }

    // Get all products in a specific category
    public LiveData<List<Product>> getProductsByCategory(String category) {
        return productDao.getProductsByCategory(category);
    }

    // Get all products
    public LiveData<List<Product>> getAllProducts() {
        return productDao.getAllProducts();
    }
}
