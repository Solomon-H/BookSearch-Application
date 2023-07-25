package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.Customer;

import java.util.List;

public interface CustomerDao {
    /**
     * Get a customer from the datastore that has the given id.
     * If the id is not found, return null.
     *
     * @param customerId The id of the customer.
     * @return A filled out Customer object, null if the customerId isn't in the database.
     */
    Customer getCustomer(int customerId);

    /**
     * Get all customers from the datastore, ordered by customer_id.
     *
     * @return All customers as Customer objects in a List.
     */
    List<Customer> getCustomers();

    /**
     * Add a new customer into the datastore.
     *
     * @param newCustomer the Customer object to add.
     * @return The added Customer object with its new id filled in.
     */
    Customer createCustomer(Customer newCustomer);

    /**
     * Update a customer to the datastore. Only called on customers that
     * are already in the datastore.
     *
     * @param updatedCustomer The Customer object to update.
     */
    void updateCustomer(Customer updatedCustomer);

}
