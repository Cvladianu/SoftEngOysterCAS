package com.tfl.billing.Adaptors;

import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;

import java.util.List;
import java.util.UUID;

public interface CustomersDatabase {

    List<Customer> getCustomers();

    boolean isRegisteredId(UUID cardId);

}
