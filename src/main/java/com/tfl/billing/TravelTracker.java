package com.tfl.billing;

import com.oyster.*;
import com.tfl.billing.adaptors.AdaptorDatabase;
import com.tfl.billing.adaptors.AdaptorPaymentSystem;
import com.tfl.billing.adaptors.CustomersDatabase;
import com.tfl.billing.adaptors.PaymentSystem;
import com.tfl.external.Customer;

import java.util.*;

public class TravelTracker implements ScanListener {
    //trying to refactor eventLog into a map
    private final HashMap<UUID, ArrayList<JourneyEvent>> eventLog;
    private final Set<UUID> currentlyTravelling;
    private final CustomersDatabase customersDatabase;
    private final PaymentSystem paymentSystem;
    private final CustomerCharger customerCharger;


    public TravelTracker()
    {
        this.eventLog=new HashMap<UUID, ArrayList<JourneyEvent> >();
        this.currentlyTravelling= new HashSet<>();
        this.customersDatabase = AdaptorDatabase.getInstance();
        this.paymentSystem= AdaptorPaymentSystem.getInstance();
        this.customerCharger = new CustomerCharger();
    }
    //Dependency injection
    public TravelTracker(HashMap<UUID, ArrayList<JourneyEvent>> eventLog, Set<UUID> currentlyTravelling, CustomersDatabase customersDatabase, PaymentSystem adaptorPaymentSystem)
    {
        this.eventLog=eventLog;
        this.currentlyTravelling=currentlyTravelling;
        this.customersDatabase = customersDatabase;
        this.paymentSystem=adaptorPaymentSystem;
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
        List<JourneyEvent> customerJourneyEvents;
        customerJourneyEvents=eventLog.get(customer.cardId());

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

    private void addToMap(UUID key, JourneyEvent event)
    {
        eventLog.putIfAbsent(key, new ArrayList<JourneyEvent>());
        eventLog.get(key).add(event);
    }

    @Override
    public void cardScanned(UUID cardId, UUID readerId) {
        if (currentlyTravelling.contains(cardId)) {
            addToMap(cardId,new JourneyEnd(cardId, readerId));
            //eventLog.get(cardId).add(new JourneyEnd(cardId, readerId));
            currentlyTravelling.remove(cardId);
        } else {
            if (customersDatabase.isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                addToMap(cardId,new JourneyEnd(cardId, readerId));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }
}
