package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.Customer;
import com.techelevator.ssgeek.model.LineItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class JdbcLineItemDaoTest extends BaseDaoTests {


    private JdbcLineItemDao dao;

    @Before
    public void setup() {
        dao = new JdbcLineItemDao(dataSource);
    }

    @Test
    public void getLineItem_By_Sale_returns_list_of_all_lineItem() {
        List<LineItem> actualLineItem = dao.getLineItemsBySale(1);
        Assert.assertEquals(3, actualLineItem.size());

        actualLineItem = dao.getLineItemsBySale(2);
        Assert.assertEquals(2, actualLineItem.size());

        actualLineItem = dao.getLineItemsBySale(3);
        Assert.assertEquals(1, actualLineItem.size());
    }

}