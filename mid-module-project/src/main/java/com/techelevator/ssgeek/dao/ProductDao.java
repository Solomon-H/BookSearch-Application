package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.Product;

import java.util.List;

public interface ProductDao {

    /**
     * Get a product from the datastore that has the given id.
     * If the id is not found, return null.
     *
     * @param productId The id of the product.
     * @return A filled out Product object, or null if the id is invalid.
     */
    Product getProduct(int productId);

    /**
     * Get all products from the datastore, ordered by product_id.
     *
     * @return All products as Product objects in a List.
     */
    List<Product> getProducts();

    /**
     * Get a list of product for which there are no sales yet, ordered by product_id. Only these products may
     * be deleted form the datastore.
     *
     * @return All products as Product objects in a List.
     */
    List<Product> getProductsWithNoSales();

    /**
     * Add a new product into the datastore.
     *
     * @param newProduct The Product object to add.
     * @return The added Product object with its new id filled in.
     */
    Product createProduct(Product newProduct);

    /**
     * Update a product in the datastore. Only called on products that
     * are already in the datastore.
     *
     * @param updatedProduct The Product object to update.
     */
    void updateProduct(Product updatedProduct);

    /**
     * Remove a product from the datastore.
     *
     * @param productId The id of the product to remove. If the id doesn't exist, no error will occur.
     */
    void deleteProduct(int productId);

}
