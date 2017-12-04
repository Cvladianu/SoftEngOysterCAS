package com.tfl.billing.Adaptors;

import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;

import java.util.List;
import java.util.UUID;

public class AdaptorDatabase implements CustomersDatabase {

    private static AdaptorDatabase instance = new AdaptorDatabase();

    private CustomerDatabase customerDatabase = CustomerDatabase.getInstance();

    public static AdaptorDatabase getInstance() {
        return instance;
    }

    @Override
    public List<Customer> getCustomers() {
        return customerDatabase.getCustomers();
    }

    @Override
    public boolean isRegisteredId(UUID cardId) {
        return customerDatabase.isRegisteredId(cardId);
    }
}
