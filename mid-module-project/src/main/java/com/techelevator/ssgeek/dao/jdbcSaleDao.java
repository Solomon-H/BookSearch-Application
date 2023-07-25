package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.Sale;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcSaleDao implements SaleDao{

    private JdbcTemplate dao;

    public JdbcSaleDao(DataSource dataSource) {
        this.dao = new JdbcTemplate(dataSource);
    }

    @Override
    public Sale getSale(int saleId) {
        Sale sale = null;
        String sqlSale = "SELECT sale_id, c.customer_id, c.name, sale_date, ship_date " +
                         "FROM sale as s " +
                         "JOIN customer as c ON s.customer_id = c.customer_id " +
                         "WHERE sale_id = ? ";

        SqlRowSet results = dao.queryForRowSet(sqlSale, saleId);

        if (results.next()) {
            sale = mapRowToSale(results);
        }
        return sale;
    }

    @Override
    public List<Sale> getSalesUnshipped() {
        List<Sale> saleList = new ArrayList<>();
        String sqlSale = "SELECT sale_id, c.customer_id, c.name, sale_date, ship_date " +
                         "FROM sale as s " +
                         "JOIN customer as c ON s.customer_id = c.customer_id " +
                         "WHERE ship_date IS NULL " +
                         "ORDER BY sale_id";
        SqlRowSet results = dao.queryForRowSet(sqlSale);
        while (results.next()) {
            Sale sale = mapRowToSale(results);
            saleList.add(sale);
        }
        return saleList;
    }

    @Override
    public List<Sale> getSalesByCustomerId(int customerId) {
        List<Sale> saleList = new ArrayList<>();
        String sqlSale = "SELECT s.sale_id, c.customer_id, c.name, sale_date, ship_date " +
                         "FROM sale as s " +
                         "JOIN customer as c ON s.customer_id = c.customer_id " +
                         "WHERE c.customer_id = ? ";
        SqlRowSet results = dao.queryForRowSet(sqlSale, customerId);
        while (results.next()) {
            Sale sale = mapRowToSale(results);
            saleList.add(sale);
        }
        return saleList;
    }

    @Override
    public List<Sale> getSalesByProductId(int productId) {
        List<Sale> saleList = new ArrayList<>();
        String sqlSale ="SELECT s.sale_id, c.customer_id, c.name, p.product_id, sale_date, ship_date " +
                        "FROM sale s " +
                        "JOIN customer c ON s.customer_id = c.customer_id " +
                        "JOIN line_item li ON s.sale_id = li.sale_id " +
                        "JOIN product p ON p.product_id = li.product_id " +
                        "WHERE p.product_id = ? " +
                        "ORDER BY sale_id ";
        SqlRowSet results = dao.queryForRowSet(sqlSale, productId);
        while (results.next()) {
            Sale sale = mapRowToSale(results);
            saleList.add(sale);
        }
        return saleList;
    }

    @Override
    public Sale createSale(Sale newSale) {
        String saleNewSale = "INSERT INTO sale (customer_id, sale_date, ship_date) " +
                             "VALUES (?, ?, ?) RETURNING sale_id;";
        Integer newId = dao.queryForObject(saleNewSale, Integer.class, newSale.getCustomerId(), newSale.getSaleDate(),
                newSale.getShipDate());
        return getSale(newId);
    }

    @Override
    public void updateSale(Sale updatedSale) {
       String sqlSaleUpdate = "UPDATE sale " +
                              "SET customer_id = ?, sale_date = ?, ship_date = ? " +
                              "WHERE sale_id = ? ";
       dao.update(sqlSaleUpdate, updatedSale.getCustomerId(), updatedSale.getSaleDate(), updatedSale.getShipDate(),
               updatedSale.getSaleId());
    }

    @Override
    public void deleteSale(int saleId) {
        String sqlDeleteSale = "DELETE FROM sale WHERE sale_id = ? ";
        dao.update(sqlDeleteSale, saleId);

    }


    private Sale mapRowToSale(SqlRowSet results) {
        Sale sale = new Sale();
        sale.setSaleId(results.getInt("sale_id"));
        sale.setCustomerId(results.getInt("customer_id"));
        sale.setSaleDate(results.getDate("sale_date").toLocalDate());
        if (results.getDate("ship_date") != null) {
        sale.setShipDate(results.getDate("ship_date").toLocalDate()); }
        else {
            sale.setShipDate(null);
        }
        return sale;
    }
}
