package com.hami.fresheatables;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM products WHERE id = :id")
    LiveData<Product> getProductById(int id);

    @Query("SELECT * FROM vendors WHERE id = :vendorId LIMIT 1")
    Vendor getVendorById(int vendorId);

    @Query("SELECT * FROM products WHERE vendorId = :vendorId")
    LiveData<List<Product>> getProductsByVendor(int vendorId);

    @Query("SELECT * FROM products WHERE category = :category")
    LiveData<List<Product>> getProductsByCategory(String category);

    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAllProducts();
}
