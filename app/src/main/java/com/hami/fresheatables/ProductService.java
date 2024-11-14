package com.hami.fresheatables;

import android.util.Log;

import androidx.room.Transaction;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductService {

    private ProductDao productDao;
    private VendorDao vendorDao;
    private Executor executor;

    public ProductService(ProductDao productDao, VendorDao vendorDao) {
        this.productDao = productDao;
        this.vendorDao = vendorDao;
        executor = Executors.newSingleThreadExecutor();
    }

    @Transaction
    public void insertVendorAndProduct(Vendor vendor, Product product) {
        executor.execute(() -> {
            Vendor existingVendor = vendorDao.getVendorById(vendor.getId());
            if (existingVendor == null) {
                // Insert vendor only if it doesn't exist
                vendorDao.insert(vendor);
            }
            product.setVendorId(vendor.getId());
            productDao.insert(product);
            Log.d("ProductService", "Vendor and Product inserted successfully");
        });
    }

}
