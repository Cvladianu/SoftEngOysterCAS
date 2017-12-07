package com.tfl.billing.utils;

import com.tfl.billing.adaptors.PaymentSystem;
import com.tfl.billing.Journey;
import com.tfl.external.Customer;

import java.math.BigDecimal;
import java.util.List;

public class ControllablePaymentSystem implements PaymentSystem {
    private BigDecimal totalBill;
    private Customer customer;
    private List<Journey> journeys;
    @Override
    public void charge(Customer customer, List<Journey> journeys, BigDecimal totalBill) {
        this.totalBill=totalBill;
        this.customer=customer;
        this.journeys=journeys;
    }

    public BigDecimal getTotal() {
        return totalBill;
    }

    public BigDecimal getTotalBill() {
        return totalBill;
    }

    public List<Journey> getJourneys() {
        return journeys;
    }

    public Customer getCustomer() {
        return customer;
    }
}
