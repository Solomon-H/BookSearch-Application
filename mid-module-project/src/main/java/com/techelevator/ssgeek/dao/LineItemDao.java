package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.LineItem;

import java.util.List;

public interface LineItemDao {
    /**
     * Get all line items associated with a sale, ordered by line_item_id.
     * @param saleId The id of the sale to get line items from.
     * @return All line items for a sale as LineItem objects in a List.
     */
    List<LineItem> getLineItemsBySale(int saleId);

}
