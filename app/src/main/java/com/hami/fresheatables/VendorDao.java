package com.hami.fresheatables;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VendorDao {

    @Insert
    void insert(Vendor vendor);

    @Update
    void update(Vendor vendor);

    @Delete
    void delete(Vendor vendor);

    @Query("SELECT * FROM vendors WHERE id = :id LIMIT 1")
    Vendor getVendorById(int id);

    @Query("SELECT * FROM vendors WHERE username = :username LIMIT 1")
    LiveData<Vendor> getVendorByUsername(String username);

    @Query("SELECT * FROM vendors")
    LiveData<List<Vendor>> getAllVendors();
}
