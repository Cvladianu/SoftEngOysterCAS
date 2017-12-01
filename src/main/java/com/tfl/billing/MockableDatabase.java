package com.tfl.billing;

import com.tfl.external.Customer;

import java.util.List;
import java.util.UUID;

public interface MockableDatabase {

    MockableDatabase getInstance();

    List<Customer> getCustomers();

    boolean isRegisteredId(UUID cardId);

}
