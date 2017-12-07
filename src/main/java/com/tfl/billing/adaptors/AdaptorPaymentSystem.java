package com.tfl.billing.adaptors;

import com.tfl.billing.Journey;
import com.tfl.external.Customer;
import com.tfl.external.PaymentsSystem;

import java.math.BigDecimal;
import java.util.List;

public class AdaptorPaymentSystem implements PaymentSystem {
    private static AdaptorPaymentSystem instance= new AdaptorPaymentSystem();
    private PaymentsSystem paymentsSystem= PaymentsSystem.getInstance();

    public static AdaptorPaymentSystem getInstance() {
        return instance;
    }

    @Override
    public void charge(Customer customer, List<Journey> journeys, BigDecimal totalBill) {
        paymentsSystem.charge(customer, journeys, totalBill);
    }

}
