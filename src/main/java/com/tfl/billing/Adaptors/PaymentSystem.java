package com.tfl.billing.Adaptors;

import com.tfl.billing.Journey;
import com.tfl.external.Customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentSystem {

    void charge(Customer customer, List<Journey> journeys, BigDecimal totalBill);
}
