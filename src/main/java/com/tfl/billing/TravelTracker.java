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
    private final CustomersDatabase customersDatabase;
    private final PaymentSystem paymentSystem;
    private final CustomerCharger customerCharger;


    public TravelTracker()
    {
        this.eventLog=new ArrayList<>();
        this.currentlyTravelling= new HashSet<>();
        this.customersDatabase = AdaptorDatabase.getInstance();
        this.paymentSystem= AdaptorPaymentSystem.getInstance();
        this.customerCharger = new CustomerCharger();
    }
    //Dependency injection
    public TravelTracker(List<JourneyEvent> eventlog, Set<UUID> currentlyTravelling, CustomersDatabase customersDatabase)
    {
        this.eventLog=eventlog;
        this.currentlyTravelling=currentlyTravelling;
        this.customersDatabase = customersDatabase;
        this.paymentSystem=AdaptorPaymentSystem.getInstance();
        this.customerCharger = new CustomerCharger();
    }

    public TravelTracker(List<JourneyEvent> eventlog, Set<UUID> currentlyTravelling, CustomersDatabase customersDatabase, PaymentSystem adaptorPaymentSystem)
    {
        this.eventLog=eventlog;
        this.currentlyTravelling=currentlyTravelling;
        this.customersDatabase = customersDatabase;
        this.paymentSystem=adaptorPaymentSystem;
        this.customerCharger = new CustomerCharger();
    }

    public TravelTracker(List<JourneyEvent> eventLog, Set<UUID> currentlyTravelling) {
        this.eventLog=eventLog;
        this.currentlyTravelling=currentlyTravelling;
        this.customersDatabase =AdaptorDatabase.getInstance();
        this.paymentSystem=AdaptorPaymentSystem.getInstance();
        this.customerCharger = new CustomerCharger();
    }

    public void chargeAccounts() {
        CustomersDatabase customerDatabase = customersDatabase;

        List<Customer> customers = customerDatabase.getCustomers();
        for (Customer customer : customers) {
            totalJourneysFor(customer);
        }
    }

    private void totalJourneysFor(Customer customer) {
        List<Journey> journeys = journeysFor(customer);

        customerCharger.charge(customer, journeys, paymentSystem);
    }

    private List<Journey> journeysFor(Customer customer)
    {
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

        return journeys;
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
            if (customersDatabase.isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }
}
