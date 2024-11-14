package com.hami.fresheatables;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class CustomerRepository {
    private CustomerDao customerDao;
    private LiveData<List<Customer>> allCustomers;

    public CustomerRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        customerDao = db.customerDao();
        allCustomers = customerDao.getAllCustomers();
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public LiveData<Customer> getCustomerByName(String name) {
        return customerDao.getCustomerByName(name);
    }
    public void insert(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> customerDao.insert(customer));
    }

    public void update(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> customerDao.update(customer));
    }

    public void delete(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> customerDao.delete(customer));
    }
}
