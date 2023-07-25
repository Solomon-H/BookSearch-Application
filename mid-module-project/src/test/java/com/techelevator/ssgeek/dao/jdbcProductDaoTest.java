package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.Customer;
import com.techelevator.ssgeek.model.Product;
import com.techelevator.ssgeek.model.Sale;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

public class JdbcProductDaoTest extends BaseDaoTests{

    private static final Product PRODUCT_1 = new Product(1,"Product 1", "Description 1",
            new BigDecimal(9.99), "product-1.png");
    private static final Product PRODUCT_2 = new Product(2,"Product 2", "Description 2",
            new BigDecimal(19.00), "product-2.png");
    private static final Product PRODUCT_3 = new Product(3,"Product 3", "Description 3",
            new BigDecimal(123.45), "product-3.png");
    private static final Product PRODUCT_4 = new Product(4,"Product 4", "Description 4",
            new BigDecimal(0.99), "product-4.png");

    private JdbcProductDao dao;

    @Before
    public void setup() {
        dao = new JdbcProductDao(dataSource);
    }

    @Test
    public void getProduct_returns_correct_Product_for_id() {
        Product actualProduct = dao.getProduct(PRODUCT_1.getProductId());
        Assert.assertNotNull(actualProduct);

    }

    @Test
    public void getProducts() {
        List<Product> actualProduct = dao.getProducts();
        Assert.assertEquals(4,actualProduct.size());

    }

    @Test
    public void getProductsWithNoSales__returnsCorrectProduct() {
        List<Product> actualProduct = dao.getProductsWithNoSales();
        Assert.assertEquals(1, actualProduct.size());
    }

    @Test
    public void createProduct() {
        Product newProduct = getProductRowMap("Product 7", "Description 7", new BigDecimal("12.12"),
                "product-7.png");
        Product createProduct = dao.createProduct(newProduct);
        Integer newId = createProduct.getProductId();
        Product retrievedProduct= dao.getProduct(newId);
        assertProductMatch(createProduct, retrievedProduct);
    }

    @Test
    public void updateProduct() {
        Product productUpdate = dao.getProduct(1);

        productUpdate.setName("Product 5");
        productUpdate.setDescription("Description 5");
        productUpdate.setPrice(new BigDecimal("10.99"));
        productUpdate.setImageName("product-5.png");

        dao.updateProduct(productUpdate);

        Product retrievedProduct = dao.getProduct(1);
        assertProductMatch(productUpdate, retrievedProduct);
    }

    @Test
    public void deleteProduct() {

        dao.deleteProduct(PRODUCT_1.getProductId());
        Product retrievedProduct = dao.getProduct(PRODUCT_1.getProductId());
        Assert.assertNull(retrievedProduct);

    }

    private Product getProductRowMap(String name, String description, BigDecimal price,
                                       String imageName) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageName(imageName);
        return product;
    }

    private void assertProductMatch(Product expected, Product actual) {
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        Assert.assertEquals(expected.getPrice(), actual.getPrice());
        Assert.assertEquals(expected.getImageName(), actual.getImageName());
    }
}