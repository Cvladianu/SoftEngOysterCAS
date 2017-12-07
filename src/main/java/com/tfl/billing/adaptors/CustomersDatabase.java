package com.tfl.billing.adaptors;

import com.tfl.external.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomersDatabase {

    List<Customer> getCustomers();

    boolean isRegisteredId(UUID cardId);

}
