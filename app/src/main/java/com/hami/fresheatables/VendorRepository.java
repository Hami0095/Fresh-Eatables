package com.hami.fresheatables;

import android.app.Application;

import androidx.lifecycle.LiveData;

public class VendorRepository {
    private VendorDao vendorDao;

    public VendorRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        vendorDao = db.vendorDao();
    }

    public void insert(Vendor vendor) {
        AppDatabase.databaseWriteExecutor.execute(() -> vendorDao.insert(vendor));
    }

    public LiveData<Vendor> getVendorByUsername(String username) {
        return vendorDao.getVendorByUsername(username);
    }
    public Vendor getVendorById(int id) {
        return vendorDao.getVendorById(id);
    }

    // Add more methods as needed
}
