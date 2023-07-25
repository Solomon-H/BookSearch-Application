package com.techelevator.ssgeek.dao;

import com.techelevator.ssgeek.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDao implements ProductDao{


    private JdbcTemplate dao;

    public JdbcProductDao(DataSource dataSource) {
        this.dao = new JdbcTemplate(dataSource);
    }

    @Override
    public Product getProduct(int productId) {
        Product product = null;
        String sqlProduct = "SELECT * FROM PRODUCT WHERE product_id = ? ";

        SqlRowSet results = dao.queryForRowSet(sqlProduct, productId);

        if (results.next()) {
            product = mapRowToProduct(results);
        }
        return product;
    }

    @Override
    public List<Product> getProducts() {
        List<Product> productList = new ArrayList<>();
        String sqlProduct = "SELECT * FROM PRODUCT ORDER BY product_id";

        SqlRowSet results = dao.queryForRowSet(sqlProduct);

        while (results.next()) {
           productList.add(mapRowToProduct(results));
        }

        return productList;
    }

    @Override
    public List<Product> getProductsWithNoSales() {
        List<Product> productList = new ArrayList<>();
        String sqlProduct = "SELECT p.name, p.description, p.price, p.image_name " +
                            "FROM product p " +
                            "JOIN line_item li ON p.product_id = li.product_id " +
                            "WHERE sale_id = null " +
                            "ORDER BY product_id ";
        SqlRowSet results = dao.queryForRowSet(sqlProduct);

        while (results.next()) {
            productList.add(mapRowToProduct(results));
        }
        return productList;
    }

    @Override
    public Product createProduct(Product newProduct) {
        Product product = null;
        String sqlCreateProduct = "INSERT INTO product (name, description, price, image_name) " +
                                  "VALUES(?, ?, ?, ?) RETURNING product_id ";

        Integer newId = dao.queryForObject(sqlCreateProduct, Integer.class, newProduct.getName(), newProduct.getDescription(),
                newProduct.getPrice(), newProduct.getImageName());
        return getProduct(newId);
    }

    @Override
    public void updateProduct(Product updatedProduct) {
        String sqlUpdateProduct = "UPDATE product SET name = ?, description = ?, price = ?, image_name = ? WHERE product_id = ? ";
        dao.update(sqlUpdateProduct, updatedProduct.getName(), updatedProduct.getDescription(), updatedProduct.getPrice(), updatedProduct.getImageName(), updatedProduct.getProductId());
    }

    @Override
    public void deleteProduct(int productId) {
        String sqlDelete = "DELETE FROM line_item WHERE product_id = ? ";
        dao.update(sqlDelete, productId);

        sqlDelete = "DELETE FROM product WHERE product_id = ? ";
        dao.update(sqlDelete, productId);

    }

    private Product mapRowToProduct(SqlRowSet results) {
        Product product = new Product();
        product.setProductId(results.getInt("product_id"));
        product.setName(results.getString("name"));
        product.setDescription(results.getString("description"));
        product.setPrice(results.getBigDecimal("price"));
        product.setImageName(results.getString("image_name"));
        return product;
    }
}
