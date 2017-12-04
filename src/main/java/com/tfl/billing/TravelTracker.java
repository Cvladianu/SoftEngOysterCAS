package com.tfl.billing;

import com.oyster.*;
import com.tfl.billing.Adaptors.AdaptorDatabase;
import com.tfl.billing.Adaptors.AdaptorPaymentSystem;
import com.tfl.billing.Adaptors.CustomersDatabase;
import com.tfl.billing.Adaptors.PaymentSystem;
import com.tfl.external.Customer;

import java.math.BigDecimal;
import java.util.*;

public class TravelTracker implements ScanListener {

    static final BigDecimal OFF_PEAK_JOURNEY_PRICE = new BigDecimal(2.40);
    static final BigDecimal PEAK_JOURNEY_PRICE = new BigDecimal(3.20);

    private final List<JourneyEvent> eventLog;
    private final Set<UUID> currentlyTravelling;
    private final CustomersDatabase adaptorDatabase;
    private final PaymentSystem paymentSystem;


    public TravelTracker()
    {
        this.eventLog=new ArrayList<>();
        this.currentlyTravelling= new HashSet<>();
        this.adaptorDatabase= AdaptorDatabase.getInstance();
        this.paymentSystem= AdaptorPaymentSystem.getInstance();
    }
    //Dependency injection
    public TravelTracker(List<JourneyEvent> eventlog, Set<UUID> currentlyTravelling, CustomersDatabase adaptorDatabase)
    {
        this.eventLog=eventlog;
        this.currentlyTravelling=currentlyTravelling;
        this.adaptorDatabase=adaptorDatabase;
        this.paymentSystem=AdaptorPaymentSystem.getInstance();
    }

    public TravelTracker(List<JourneyEvent> eventlog, Set<UUID> currentlyTravelling, CustomersDatabase adaptorDatabase, PaymentSystem adaptorPaymentSystem)
    {
        this.eventLog=eventlog;
        this.currentlyTravelling=currentlyTravelling;
        this.adaptorDatabase=adaptorDatabase;
        this.paymentSystem=adaptorPaymentSystem;
    }

    public TravelTracker(List<JourneyEvent> eventLog, Set<UUID> currentlyTravelling) {
        this.eventLog=eventLog;
        this.currentlyTravelling=currentlyTravelling;
        this.adaptorDatabase=AdaptorDatabase.getInstance();
        this.paymentSystem=AdaptorPaymentSystem.getInstance();
    }

    public void chargeAccounts() {
        CustomersDatabase customerDatabase = adaptorDatabase;

        List<Customer> customers = customerDatabase.getCustomers();
        for (Customer customer : customers) {
            totalJourneysFor(customer);
        }
    }

    private void totalJourneysFor(Customer customer) {

        JourneyCostCalculator journeyCostCalculator= new JourneyCostCalculator();

        List<JourneyEvent> customerJourneyEvents = new ArrayList<JourneyEvent>();
        for (JourneyEvent journeyEvent : eventLog) {
            if (journeyEvent.cardId().equals(customer.cardId())) {
                customerJourneyEvents.add(journeyEvent);
            }
        }

        List<Journey> journeys = new ArrayList<Journey>();

        JourneyEvent start = null;
        for (JourneyEvent event : customerJourneyEvents) {
            if (event instanceof JourneyStart) {
                start = event;
            }
            if (event instanceof JourneyEnd && start != null) {
                journeys.add(new Journey(start, event));
                start = null;
            }
        }

        BigDecimal customerTotal =journeyCostCalculator.customerTotalFor(journeys);
        paymentSystem.charge(customer, journeys, customerTotal);
    }

    public void connect(OysterCardReader... cardReaders) {
        for (OysterCardReader cardReader : cardReaders) {
            cardReader.register(this);
        }
    }

    @Override
    public void cardScanned(UUID cardId, UUID readerId) {
        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEnd(cardId, readerId));
            currentlyTravelling.remove(cardId);
        } else {
            if (adaptorDatabase.isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }
}
