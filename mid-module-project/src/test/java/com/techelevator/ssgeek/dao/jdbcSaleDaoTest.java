package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.Customer;
import com.techelevator.ssgeek.model.Sale;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class JdbcSaleDaoTest extends BaseDaoTests{

    private static final Sale SALE_1 =
            new Sale(1, 1, LocalDate.parse("2022-01-01"), null);
    private static final Sale SALE_2 =
            new Sale(2, 1, LocalDate.parse("2022-02-01"), LocalDate.parse("2022-02-02"));
    private static final Sale SALE_3 =
            new Sale(3, 2, LocalDate.parse("2022-03-01"), null);
    private static final Sale SALE_4 =
            new Sale(4, 2, LocalDate.parse("2022-01-01"), LocalDate.parse("2022-01-02"));


    private JdbcSaleDao dao;

    @Before
    public void setup() {
        dao = new JdbcSaleDao(dataSource);
    }


    @Test
    public void getSale_returns_correct_sale_for_id() {
        Sale sale = dao.getSale(1);
        Assert.assertNotNull(sale);
        assertSaleMatch(SALE_1, sale);

        sale = dao.getSale(SALE_2.getSaleId());
        Assert.assertNotNull(sale);
        assertSaleMatch(SALE_2, sale);

    }

    @Test
    public void getSales_returns_Unshipped_products() {
       List<Sale> saleList = dao.getSalesUnshipped();
       Assert.assertEquals(2, saleList.size());
       assertSaleMatch(SALE_1, saleList.get(0));
       assertSaleMatch(SALE_3, saleList.get(1));
    }

    @Test
    public void getSales_By_Customer_Id() {
        List<Sale> saleList = dao.getSalesByCustomerId(1);
        Assert.assertEquals(2, saleList.size());
       assertSaleMatch(SALE_1, saleList.get(0));
        assertSaleMatch(SALE_2, saleList.get(1));

        saleList = dao.getSalesByCustomerId(2);
        Assert.assertEquals(2, saleList.size());
        assertSaleMatch(SALE_3, saleList.get(0));
        assertSaleMatch(SALE_4, saleList.get(1));

        saleList = dao.getSalesByCustomerId(99);
        Assert.assertEquals(0, saleList.size());

    }

    @Test
    public void getSales_By_Product_Id() {
        List<Sale> saleList = dao.getSalesByProductId(SALE_1.getCustomerId());
        Assert.assertEquals(3, saleList.size());
         assertSaleMatch(SALE_1, saleList.get(0));
         assertSaleMatch(SALE_2, saleList.get(1));

        saleList = dao.getSalesByProductId(SALE_2.getCustomerId());
        Assert.assertEquals(3, saleList.size());
//        assertSaleMatch(SALE_3, saleList.get(0));
//        assertSaleMatch(SALE_4, saleList.get(1));

        saleList = dao.getSalesByProductId(99);
        Assert.assertEquals(0, saleList.size());
    }

    @Test
    public void createSale() {
        Sale newSale = getSaleRowMap(1, LocalDate.parse("2022-04-04"),
                           LocalDate.parse("2022-03-10"));

        Sale createSale = dao.createSale(newSale);

        Integer newId = createSale.getSaleId();
        Sale retrievedSale = dao.getSale(newId);

        assertSaleMatch(createSale, retrievedSale);

    }

    @Test
    public void updateSale() {
        Sale saleUpdate = dao.getSale(SALE_1.getSaleId());

        saleUpdate.setCustomerId(1);
        saleUpdate.setSaleDate(LocalDate.parse("2022-03-03"));
        saleUpdate.setShipDate(LocalDate.parse("2022-03-30"));

        dao.updateSale(saleUpdate);

        Sale retrievedSale = dao.getSale(SALE_1.getSaleId());
        assertSaleMatch(saleUpdate, retrievedSale);
    }

    @Test
    public void deleteSale() {
        dao.deleteSale(SALE_4.getSaleId());

        Sale retrievedSale = dao.getSale(SALE_4.getSaleId());
        Assert.assertNull(retrievedSale);
    }

    private Sale getSaleRowMap(int customerId, LocalDate saleDate, LocalDate shipDate) {
        Sale sale = new Sale();
        sale.setCustomerId(customerId);
        sale.setSaleDate(saleDate);
        sale.setShipDate(shipDate);
        return sale;
    }

    private void assertSaleMatch(Sale expected, Sale actual) {
        Assert.assertEquals(expected.getSaleId(), actual.getSaleId());
        Assert.assertEquals(expected.getCustomerId(), actual.getCustomerId());
        Assert.assertEquals(expected.getSaleDate(), actual.getSaleDate());
        Assert.assertEquals(expected.getShipDate(), actual.getShipDate());
        Assert.assertEquals(expected.getCustomerName(), actual.getCustomerName());

    }

}