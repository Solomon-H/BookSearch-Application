package com.techelevator.ssgeek.model;

import java.time.LocalDate;

public class Sale {
    private int saleId;
    private int customerId;
    private LocalDate saleDate;
    private LocalDate shipDate;
    // NOTE: When sales are read, the DAO will join to the customer table and pull the customer name
    // to save an additional lookup later. When you display a sale, you almost always want to display the
    // customer name.
    // You can use this technique for any related field, but customer name should be enough.
    private String customerName;

    public Sale() {}

    public Sale(int saleId, int customerId, LocalDate saleDate, LocalDate shipDate) {
        this.saleId = saleId;
        this.customerId = customerId;
        this.saleDate = saleDate;
        this.shipDate = shipDate;

    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public LocalDate getShipDate() {
        return shipDate;
    }

    public void setShipDate(LocalDate shipDate) {
        this.shipDate = shipDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
