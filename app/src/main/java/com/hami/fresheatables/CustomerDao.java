package com.hami.fresheatables;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface CustomerDao {
    @Insert
    void insert(Customer customer);

    @Update
    void update(Customer customer);

    @Delete
    void delete(Customer customer);

    @Query("SELECT * FROM customers")
    LiveData<List<Customer>> getAllCustomers();

    @Query("SELECT * FROM customers WHERE name = :name LIMIT 1")
    LiveData<Customer> getCustomerByName(String name);
}
