package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


public class JdbcCustomerDao implements CustomerDao{

    private JdbcTemplate dao;

    public JdbcCustomerDao(DataSource dataSource) {
        this.dao = new JdbcTemplate(dataSource);
    }

    @Override
    public Customer getCustomer(int customerId) {
        Customer customer = null;
        String sqlCustomers = "SELECT customer_id, name, street_address1, street_address2, city, state, zip_code " +
                                "FROM customer " +
                                "WHERE customer_id = ? ";
        SqlRowSet results = dao.queryForRowSet(sqlCustomers, customerId);

        if (results.next()) {
              customer = mapRowToCustomer(results);
        }
                return customer;
    }

    @Override
    public List<Customer> getCustomers() {
        List<Customer> customerList = new ArrayList<>();
        String sqlCustomers = "SELECT customer_id, name, street_address1, street_address2, city, state, zip_code " +
                              "FROM customer " +
                              "ORDER BY customer_id ";
        SqlRowSet results = dao.queryForRowSet(sqlCustomers);

        while (results.next()) {
            customerList.add(mapRowToCustomer(results));
        }
        return customerList;
    }

    @Override
    public Customer createCustomer(Customer newCustomer) {
        String sqlNewCustomer = "INSERT INTO customer (name, street_address1, street_address2, city, state, zip_code) " +
                                "VALUES (?, ?, ?, ?, ?, ?) RETURNING customer_id;";
        Integer newId = dao.queryForObject(sqlNewCustomer, Integer.class, newCustomer.getName(), newCustomer.getStreetAddress1(), newCustomer.getStreetAddress2(), newCustomer.getCity(),
                newCustomer.getState(), newCustomer.getZipCode());

        return getCustomer(newId);
    }

    @Override
    public void updateCustomer(Customer updatedCustomer) {
        String sqlCustomerUpdate = "UPDATE customer " +
                "SET name = ?, street_address1 = ?, street_address2 = ?, city = ?, state = ?, zip_code = ? " +
                "WHERE customer_id = ?";
        dao.update(sqlCustomerUpdate, updatedCustomer.getName(), updatedCustomer.getStreetAddress1(), updatedCustomer.getStreetAddress2(), updatedCustomer.getCity(),
                updatedCustomer.getState(), updatedCustomer.getZipCode(), updatedCustomer.getCustomerId());
    }

    private Customer mapRowToCustomer(SqlRowSet results) {
        Customer customer = new Customer();
        customer.setCustomerId(results.getInt("customer_id"));
        customer.setName(results.getString("name"));
        customer.setStreetAddress1(results.getString("street_Address1"));
        customer.setStreetAddress2(results.getString("street_Address2"));
        customer.setCity(results.getString("city"));
        customer.setState(results.getString("state"));
        customer.setZipCode(results.getString("zip_code"));
        return customer;
    }
}
