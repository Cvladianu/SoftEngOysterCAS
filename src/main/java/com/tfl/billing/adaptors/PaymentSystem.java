package com.tfl.billing.adaptors;

import com.tfl.billing.Journey;
import com.tfl.external.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentSystem {

    void charge(Customer customer, List<Journey> journeys, BigDecimal totalBill);
}
