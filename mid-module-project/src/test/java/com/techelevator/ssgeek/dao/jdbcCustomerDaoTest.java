package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Callable;

public class JdbcCustomerDaoTest extends BaseDaoTests{

    private static final Customer CUSTOMER_1 =
            new Customer(1,"Customer 1", "Addr 1-1", "Addr 1-2", "City 1", "S1", "11111");
    private static final Customer CUSTOMER_2 =
            new Customer(2,"Customer 2", "Addr 2-1", "Addr 2-2", "City 2", "S2", "22222");
    private static final Customer CUSTOMER_3 =
            new Customer(3,"Customer 3", "Addr 3-1", null, "City 3", "S3", "33333");
    private static final Customer CUSTOMER_4 =
            new Customer(4,"Customer 4", "Addr 4-1", null, "City 4", "S4", "44444");


    private JdbcCustomerDao dao;

    @Before
    public void setup() {
        dao = new JdbcCustomerDao(dataSource);
    }

    @Test

    public void getCustomer_returns_correct_customer_for_id() {
        Customer actualCustomer = dao.getCustomer(CUSTOMER_1.getCustomerId());
        Assert.assertNotNull(actualCustomer);
        assertCustomerMatch(CUSTOMER_1, actualCustomer);

        actualCustomer = dao.getCustomer(CUSTOMER_2.getCustomerId());
        Assert.assertNotNull(actualCustomer);
        assertCustomerMatch(CUSTOMER_2, actualCustomer);

        actualCustomer = dao.getCustomer(CUSTOMER_3.getCustomerId());
        Assert.assertNotNull(actualCustomer);
        assertCustomerMatch(CUSTOMER_3, actualCustomer);

        actualCustomer = dao.getCustomer(CUSTOMER_4.getCustomerId());
        Assert.assertNotNull(actualCustomer);
        assertCustomerMatch(CUSTOMER_4, actualCustomer);

    }

    @Test
    public void getCustomer_returns_null_when_id_not_found() {
        Customer actualCustomer = dao.getCustomer(99);
        Assert.assertNull(actualCustomer);

    }

    @Test
    public void getCustomers_returns_list_of_all_customers() {

        List<Customer> actualCustomer = dao.getCustomers();
        Assert.assertEquals(4, actualCustomer.size());
        assertCustomerMatch(CUSTOMER_1, actualCustomer.get(0));
        assertCustomerMatch(CUSTOMER_2, actualCustomer.get(1));
        assertCustomerMatch(CUSTOMER_3, actualCustomer.get(2));
        assertCustomerMatch(CUSTOMER_4, actualCustomer.get(3));
    }

    @Test
    public void createCustomer_returns_customer_with_id_and_expected_values() {
        Customer newCustomer = getCustomerRowMap("Customer 5", "Addr 5-1", "Addr 5-2",
                "City 5", "S5", "55555");

        Customer createCustomer = dao.createCustomer(newCustomer);

        Integer newId = createCustomer.getCustomerId();
        Customer retrievedCustomer= dao.getCustomer(newId);

        assertCustomerMatch(createCustomer, retrievedCustomer);

    }

    @Test
    public void updateCustomer() {
        Customer customerUpdate = dao.getCustomer(CUSTOMER_1.getCustomerId());

        customerUpdate.setName("Customer 7");
        customerUpdate.setStreetAddress1("Addr 7-1");
        customerUpdate.setStreetAddress2("Addr 7-2");
        customerUpdate.setCity("7");
        customerUpdate.setState("S7");
        customerUpdate.setZipCode("77777");

        dao.updateCustomer(customerUpdate);

        Customer retrievedCustomer = dao.getCustomer(CUSTOMER_1.getCustomerId());
        assertCustomerMatch(customerUpdate, retrievedCustomer );

    }

    private Customer getCustomerRowMap(String name, String streetAddress1, String streetAddress2,
                                         String city, String state, String zipcode) {
       Customer customer = new Customer();
       customer.setName(name);
       customer.setStreetAddress1(streetAddress1);
       customer.setStreetAddress2(streetAddress2);
       customer.setCity(city);
       customer.setState(state);
       customer.setZipCode(zipcode);
       return customer;
    }

    private void assertCustomerMatch(Customer expected, Customer actual) {
        Assert.assertEquals(expected.getCustomerId(), actual.getCustomerId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getStreetAddress1(), actual.getStreetAddress1());
        Assert.assertEquals(expected.getStreetAddress2(), actual.getStreetAddress2());
        Assert.assertEquals(expected.getCity(), actual.getCity());
        Assert.assertEquals(expected.getState(), actual.getState());
        Assert.assertEquals(expected.getZipCode(), actual.getZipCode());

    }
}