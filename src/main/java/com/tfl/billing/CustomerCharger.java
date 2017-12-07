package com.tfl.billing;

import com.tfl.billing.adaptors.PaymentSystem;
import com.tfl.external.Customer;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by cosmi_owugxv5 on 12/5/2017.
 */
public class CustomerCharger {
    private JourneyCostCalculator journeyCostCalculator;

    public CustomerCharger()
    {
        this.journeyCostCalculator=new JourneyCostCalculator();
    }

    public void charge(Customer customer, List<Journey> journeys, PaymentSystem paymentSystem)
    {
        BigDecimal totalBill= journeyCostCalculator.customerTotalFor(journeys);
        paymentSystem.charge(customer, journeys, totalBill);
    }
}
