package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.LineItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcLineItemDao implements LineItemDao{
    private JdbcTemplate dao;

    public JdbcLineItemDao(DataSource dataSource) {
        this.dao = new JdbcTemplate(dataSource);
    }

    @Override
    public List<LineItem> getLineItemsBySale(int saleId) {
        List<LineItem> lineItemList = new ArrayList<>();
        String sqlLineItem = "SELECT sale_id, p.product_id, p.name, p.price, quantity " +
                             "FROM line_item as li " +
                             "JOIN product as p ON p.product_id = li.product_id " +
                             "WHERE sale_id = ? ";

        SqlRowSet results = dao.queryForRowSet(sqlLineItem, saleId);
        while (results.next()) {
           LineItem lineItem = mapRowToLineItem(results);
           lineItemList.add(lineItem);
        }
        return lineItemList;
    }

    private LineItem mapRowToLineItem (SqlRowSet results) {
        LineItem lineItem = new LineItem();
       // lineItem.setLineItemId(results.getInt("line_item_id"));
        lineItem.setSaleId(results.getInt("sale_id"));
        lineItem.setProductId(results.getInt("product_id"));
        lineItem.setQuantity(results.getInt("quantity"));
        lineItem.setProductName(results.getString("name"));
        lineItem.setPrice(results.getBigDecimal("price"));
        return lineItem;
    }
}
