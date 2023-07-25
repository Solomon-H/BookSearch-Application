package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.Sale;

import java.util.List;

public interface SaleDao {
    /**
     * Get a sale from the datastore that has the given id.
     * If the id is not found, return null.
     *
     * @param saleId The id of the sale.
     * @return A filled out Sale object, null if the id is not found in the database.
     */
    Sale getSale(int saleId);

    /**
     * Get all sales from the datastore that have not been shipped (ship date is null),
     * ordered by sale_id. Only unshipped orders may be shipped or deleted.
     *
     * @return All unshipped sales as Sale objects in a List.
     */
    List<Sale> getSalesUnshipped();

    /**
     * Get all sales from the datastore for a given customer, ordered by sale_id.
     *
     * @param customerId The id of the customer.
     * @return All sales as Sale objects in a List.
     */
    List<Sale> getSalesByCustomerId(int customerId);

    /**
     * Get all sales from the datastore for a given product, ordered by sale_id.
     *
     * @param productId The id of the product.
     * @return All sales as Sale objects in a List.
     */
    List<Sale> getSalesByProductId(int productId);

    /**
     * Add a new sale into the datastore.
     *
     * @param newSale The Sale object to add.
     * @return The added Sale object with its new id filled in.
     */
    Sale createSale(Sale newSale);

    /**
     * Update a sale to the datastore. Only called on sales that
     * are already in the datastore.
     *
     * @param updatedSale The Sale object to update.
     */
    void updateSale(Sale updatedSale);

    /**
     * Remove a sale from the datastore.
     *
     * @param saleId The id of the sale to remove. If the id doesn't exist, no error will occur.
     */
    void deleteSale(int saleId);

}
