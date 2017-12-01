package com.tfl.billing;

import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;

import java.util.List;
import java.util.UUID;

public interface MockableDatabase {

    List<Customer> getCustomers();

    boolean isRegisteredId(UUID cardId);

}
