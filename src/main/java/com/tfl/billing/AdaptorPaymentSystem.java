package com.tfl.billing;

import com.tfl.external.Customer;
import com.tfl.external.PaymentsSystem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
