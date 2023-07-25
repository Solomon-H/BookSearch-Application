package com.techelevator.ssgeek.model;

import java.math.BigDecimal;

public class LineItem {
    private int lineItemId;
    private int saleId;
    private int productId;
    private int quantity;
    // NOTE: When line-items are read, the DAO will join to the product table and pull the product name and price
    // to save an additional lookup later.When you display a line-item, you almost always want to display the
    // product name.
    // You can use this technique for any related field, but product name and price should be enough for now.
    private String productName;
    private BigDecimal price;


    public int getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(int lineItemId) {
        this.lineItemId = lineItemId;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        this.price.setScale(2);
    }

    public BigDecimal getExtendedPrice() {
        return price.multiply(BigDecimal.valueOf(quantity, 0));
    }

    public LineItem() {}

    public LineItem(int lineItemId, int saleId, int productId, int quantity, String productName, BigDecimal price) {
        this.lineItemId = lineItemId;
        this.saleId = saleId;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.price = price;
        this.price.setScale(2);
    }
}
