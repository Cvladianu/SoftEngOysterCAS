package com.tfl.billing.Utils;

import com.tfl.billing.Adaptors.PaymentSystem;
import com.tfl.billing.Journey;
import com.tfl.external.Customer;

import java.math.BigDecimal;
import java.util.List;

public class ControllablePaymentSystem implements PaymentSystem {
    BigDecimal total = new BigDecimal(0);
    @Override
    public void charge(Customer customer, List<Journey> journeys, BigDecimal totalBill) {
        total=totalBill;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
