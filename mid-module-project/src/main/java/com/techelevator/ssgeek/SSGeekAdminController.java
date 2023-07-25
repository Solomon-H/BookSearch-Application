package com.techelevator.ssgeek;

import com.techelevator.ssgeek.dao.*;
import com.techelevator.ssgeek.model.*;
import com.techelevator.util.BasicConsole;

import java.time.LocalDate;
import java.util.List;

/**
 * SSGeekAdminController orchestrates all of its operations through a series of menus. It relies
 * on other classes for the details of interacting with the user and with the database.
 */

public class SSGeekAdminController {

    // The view manages all the user interaction, inputs and outputs.
    private final SSGeekAdminView view;

    // The controller expects these DAO's to be "injected" into via its constructor
    private ProductDao productDao;
    private CustomerDao customerDao;
    private SaleDao saleDao;
    private LineItemDao lineItemDao;

    public SSGeekAdminController(BasicConsole console, CustomerDao customerDao, ProductDao productDao, SaleDao saleDao, LineItemDao lineItemDao) {
        view = new SSGeekAdminView(console);
        this.productDao = productDao;
        this.customerDao = customerDao;
        this.saleDao = saleDao;
        this.lineItemDao = lineItemDao;
    }

    /**
     * The run() method starts the program flow by running the main menu.
     */
    public void run() {
        displayMainMenu();
    }

    /**
     * A loop which displays the main program menu, and responds to the user's selection
     */
    private void displayMainMenu() {
        // Menu options
        final String CUSTOMER_MENU = "Customer admin menu";
        final String PRODUCT_MENU = "Product admin menu";
        final String SALES_MENU = "Sales admin menu";
        final String EXIT = "Exit the program";
        final String[] MENU_OPTIONS = {CUSTOMER_MENU, PRODUCT_MENU, SALES_MENU, EXIT};

        boolean finished = false;

        // The menu loop
        while (!finished) {
            String selection =
                    view.getMenuSelection("Welcome to Solar System Geek Administration", MENU_OPTIONS);
            switch (selection) {
                case CUSTOMER_MENU:
                    displayCustomerMenu();
                    break;
                case PRODUCT_MENU:
                    displayProductMenu();
                    break;
                case SALES_MENU:
                    displaySalesMenu();
                    break;
                case EXIT:
                    // Set finished to true so the loop exits.
                    finished = true;
                    break;
            }
        }
    }

    /**
     * A loop which displays the customer sub-menu, and responds to the user's selection
     */
    private void displayCustomerMenu() {
        // Menu options
        final String CUSTOMER_LIST = "List all customers";
        final String CUSTOMER_DETAILS = "View customer details";
        final String CUSTOMER_ADD = "Add customer";
        final String CUSTOMER_UPDATE = "Update customer details";
        final String DONE = "Main menu";
        final String[] MENU_OPTIONS = {CUSTOMER_LIST, CUSTOMER_DETAILS, CUSTOMER_ADD, CUSTOMER_UPDATE, DONE};

        boolean finished = false;

        // The menu loop
        while (!finished) {
            String selection = view.getMenuSelection("Customer administration menu", MENU_OPTIONS);
            switch (selection) {
                case CUSTOMER_LIST:
                    listCustomers();
                    break;
                case CUSTOMER_DETAILS:
                    displayCustomer();
                    break;
                case CUSTOMER_ADD:
                    addCustomer();
                    break;
                case CUSTOMER_UPDATE:
                    updateCustomer();
                    break;
                case DONE:
                    // Set finished to true so the loop exits.
                    finished = true;
                    break;
            }
        }
    }

    /**
     * A loop which displays the product sub-menu, and responds to the user's selection
     */
    private void displayProductMenu() {
        // Menu options
        final String PRODUCT_LIST = "List all products";
        final String PRODUCT_DETAILS = "View product details";
        final String PRODUCT_ADD = "Add product";
        final String PRODUCT_UPDATE = "Update product details";
        final String PRODUCT_DELETE = "Delete product";
        final String DONE = "Main menu";
        final String[] MENU_OPTIONS = {PRODUCT_LIST, PRODUCT_DETAILS, PRODUCT_ADD, PRODUCT_UPDATE, PRODUCT_DELETE, DONE};

        boolean finished = false;

        // The menu loop
        while (!finished) {
            String selection = view.getMenuSelection("Product administration menu", MENU_OPTIONS);
            switch (selection) {
                case PRODUCT_LIST:
                    listProducts();
                    break;
                case PRODUCT_DETAILS:
                    displayProduct();
                    break;
                case PRODUCT_ADD:
                    addProduct();
                    break;
                case PRODUCT_UPDATE:
                    updateProduct();
                    break;
                case PRODUCT_DELETE:
                    deleteProduct();
                    break;
                case DONE:
                    // Set finished to true so the loop exits.
                    finished = true;
                    break;
            }
        }
    }

    /**
     * A loop which displays the sales sub-menu, and responds to the user's selection
     */
    private void displaySalesMenu() {
        // Menu options
        final String SALES_LIST_CUSTOMER = "List sales orders for a customer";
        final String SALES_LIST_PRODUCT = "List sales orders for a product";
        final String SALES_SHIP = "Ship a sales order";
        final String SALES_DELETE = "Delete a sales order";
        final String DONE = "Main menu";
        final String[] MENU_OPTIONS = {SALES_LIST_CUSTOMER, SALES_LIST_PRODUCT, /* SALES_DETAILS, */SALES_SHIP, SALES_DELETE, DONE};

        boolean finished = false;

        // The menu loop
        while (!finished) {
            String selection = view.getMenuSelection("Sales administration menu", MENU_OPTIONS);
            switch (selection) {
                case SALES_LIST_CUSTOMER:
                    listSalesForCustomer();
                    break;
                case SALES_LIST_PRODUCT:
                    listSalesForProduct();
                    break;
                case SALES_SHIP:
                    updateSaleShipDate();
                    break;
                case SALES_DELETE:
                    deleteSale();
                    break;
                case DONE:
                    // Set finished to true so the loop exits.
                    finished = true;
                    break;
            }
        }
    }

    //*******************************************************
    //region Customer menu actions
    //*******************************************************

    private void listCustomers() {
        // Make sure we have the appropriate DAOs
        if (customerDao == null) {
            view.printErrorMessage("You must implement CustomerDao and pass it into the controller for this option to work.");
            return;
        }

        // Use the DAO to get the list
        List<Customer> customers = customerDao.getCustomers();
        // Use the view to display to the user
        view.listCustomers(customers);
    }

    private void displayCustomer() {
        // Make sure we have the appropriate DAOs
        if (customerDao == null) {
            view.printErrorMessage("You must implement CustomerDao and pass it into the controller for this option to work.");
            return;
        }
        // Get the list of customers so the user can choose one
        List<Customer> customers = customerDao.getCustomers();

        // Display the list of customers and ask for selection
        Customer customer = view.selectCustomer(customers);
        if (customer == null) {
            // User cancelled
            return;
        }
        // Show details to the user
        view.printCustomerDetail(customer);
    }

    private void addCustomer() {
        // Make sure we have the appropriate DAOs
        if (customerDao == null) {
            view.printErrorMessage("You must implement CustomerDao and pass it into the controller for this option to work.");
            return;
        }

        // Prompt the user for customer information
        Customer newCustomer = view.promptForCustomerInformation(null);

        // If customer is null, user cancelled
        if (newCustomer == null) {
            return;
        }
        // Call the DAO to add the new customer
        newCustomer = customerDao.createCustomer(newCustomer);
        // Inform the user
        view.printMessage("Customer " + newCustomer.getCustomerId() + " has been created.");
    }

    private void updateCustomer() {
        // Make sure we have the appropriate DAOs
        if (customerDao == null) {
            view.printErrorMessage("You must implement CustomerDao and pass it into the controller for this option to work.");
            return;
        }

        // Get the list of customers so the user can choose one
        List<Customer> customers = customerDao.getCustomers();

        // Display the list of customers and ask for selection
        Customer customer = view.selectCustomer(customers);
        if (customer == null) {
            // User cancelled
            return;
        }
        // Prompt the user for customer information
        customer = view.promptForCustomerInformation(customer);

        // Call the DAO to update the customer
        customerDao.updateCustomer(customer);
        // Inform the user
        view.printMessage("Customer has been updated.");
    }
    //*******************************************************
    //endregion Customer menu actions
    //*******************************************************

    //*******************************************************
    //region Product menu actions
    //*******************************************************

    private void listProducts() {
        // Make sure we have the appropriate DAOs
        if (productDao == null) {
            view.printErrorMessage("You must implement ProductDao and pass it into the controller for this option to work.");
            return;
        }

        // Use the DAO to get the list
        List<Product> products = productDao.getProducts();
        // Use the view to display to the user
        view.listProducts(products);
    }

    private void displayProduct() {
        // Make sure we have the appropriate DAOs
        if (productDao == null) {
            view.printErrorMessage("You must implement ProductDao and pass it into the controller for this option to work.");
            return;
        }
        // Get the list of products so the user can choose one
        List<Product> products = productDao.getProducts();

        // Display the list of products and ask for selection
        Product product = view.selectProduct(products);
        if (product == null) {
            // User cancelled
            return;
        }
        // Show details to the user
        view.printProductDetail(product);
    }

    private void addProduct() {
        // Make sure we have the appropriate DAOs
        if (productDao == null) {
            view.printErrorMessage("You must implement ProductDao and pass it into the controller for this option to work.");
            return;
        }

        // Prompt the user for product information
        Product newProduct = view.promptForProductInformation(null);

        // If product is null, user cancelled
        if (newProduct == null) {
            return;
        }
        // Call the DAO to add the new product
        newProduct = productDao.createProduct(newProduct);
        // Inform the user
        view.printMessage("Product " + newProduct.getProductId() + " has been created.");
    }

    private void updateProduct() {
        // Make sure we have the appropriate DAOs
        if (productDao == null) {
            view.printErrorMessage("You must implement ProductDao and pass it into the controller for this option to work.");
            return;
        }

        // Get the list of products so the user can choose one
        List<Product> products = productDao.getProducts();

        // Display the list of products and ask for selection
        Product product = view.selectProduct(products);
        if (product == null) {
            // User cancelled
            return;
        }
        // Prompt the user for product information
        product = view.promptForProductInformation(product);

        // Call the DAO to update the product
        productDao.updateProduct(product);
        // Inform the user
        view.printMessage("Product has been updated.");
    }

    private void deleteProduct() {
        // Make sure we have the appropriate DAOs
        if (productDao == null) {
            view.printErrorMessage("You must implement ProductDao and pass it into the controller for this option to work.");
            return;
        }

        // Get the list of products so the user can choose one
        List<Product> products = productDao.getProductsWithNoSales();

        // Display the list of products and ask for selection
        if (products.size() == 0) {
            // Nothing can be deleted because there are orders
            view.printErrorMessage("There are no products that may be deleted!");
        }
        Product product = view.selectProduct(products);
        if (product == null) {
            // User cancelled
            return;
        }
        // Prompt the user for confirmation
        boolean isConfirmed = view.promptForYesNo("Are you sure you want to DELETE product '" + product.getName() + "'?");
        if (!isConfirmed) {
            // User cancelled
            return;
        }

        // Call the DAO to delete the product
        productDao.deleteProduct(product.getProductId());
        // Inform the user
        view.printMessage("Product has been deleted.");
    }
    //*******************************************************
    //endregion Product menu actions
    //*******************************************************

    //*******************************************************
    // region Sales menu actions
    //*******************************************************

    private void displaySale(Sale sale) {
        // Make sure we have the appropriate DAOs
        if (lineItemDao == null) {
            view.printErrorMessage("You must implement LineItemDao and pass it into the controller for this option to work.");
            return;
        }

        // Get customer and line-item information
        List<LineItem> lineItems = lineItemDao.getLineItemsBySale(sale.getSaleId());
        Customer customer = customerDao.getCustomer(sale.getCustomerId());

        // Show all details to the user
        view.printSaleDetail(sale, lineItems, customer);
    }

    private void listSalesForCustomer() {
        // Get a list of customers so the user can choose
        List<Customer> customers = customerDao.getCustomers();

        // Prompt the user to select a customer
        Customer customer = view.selectCustomer(customers);
        if (customer == null) {
            // user cancelled
            return;
        }

        // Make sure we have the appropriate DAOs
        if (saleDao == null) {
            view.printErrorMessage("You must implement SaleDao and pass it into the controller for this option to work.");
            return;
        }

        // Get the list of sales for the customer
        List<Sale> sales = saleDao.getSalesByCustomerId(customer.getCustomerId());
        if (sales.size() == 0) {
            // Nothing can be shown
            view.printErrorMessage("There are no sales to this customer!");
            return;
        }
        // Show the list
        Sale selectedSale = view.selectSale(sales);

        if (selectedSale == null) {
            // User doesn't want to see any details
            return;
        }
        // Gather details for an order and display to the user
        displaySale(selectedSale);
    }

    private void listSalesForProduct() {
        // Make sure we have the appropriate DAOs
        if (productDao == null || saleDao == null) {
            view.printErrorMessage("You must implement ProductDao and SaleDao and pass them into the controller for this option to work.");
            return;
        }

        // Get a list of products so the user can choose
        List<Product> products = productDao.getProducts();

        // Prompt the user to select a product
        Product product = view.selectProduct(products);
        if (product == null) {
            // user cancelled
            return;
        }

        // Get the list of sales for the product
        List<Sale> sales = saleDao.getSalesByProductId(product.getProductId());
        if (sales.size() == 0) {
            // Nothing can be shown
            view.printErrorMessage("There are no sales for this product!");
            return;
        }
        // Show the list
        Sale selectedSale = view.selectSale(sales);

        if (selectedSale == null) {
            // User doesn't want to see any details
            return;
        }
        // Gather details for an order and display to the user
        displaySale(selectedSale);
    }

    private void updateSaleShipDate() {
        // Make sure we have the appropriate DAOs
        if (saleDao == null) {
            view.printErrorMessage("You must implement SaleDao and pass it into the controller for this option to work.");
            return;
        }
        // Get the list of sales not shipped so the user can choose one
        List<Sale> sales = saleDao.getSalesUnshipped();

        // Inform the user if there are no qualifying sales
        if (sales.size() == 0) {
            // Nothing can be shipped because there are no unshipped orders
            view.printErrorMessage("There are no sales that need to be shipped!");
            return;
        }

        // Display the list of sales and ask for selection
        Sale sale = view.selectSale(sales);

        if (sale == null) {
            // user cancelled
            return;
        }

        // Sell the ship date to today
        sale.setShipDate(LocalDate.now());

        // Update the sale in the database
        saleDao.updateSale(sale);

        // Inform the user
        view.printMessage(String.format("Sales order %d has been shipped", sale.getSaleId()));
    }

    private void deleteSale() {
        // Make sure we have the appropriate DAOs
        if (saleDao == null) {
            view.printErrorMessage("You must implement SaleDao and pass it into the controller for this option to work.");
            return;
        }

        // Get the list of sales so the user can choose one. Only unshipped sales may be deleted.
        List<Sale> sales = saleDao.getSalesUnshipped(); // You can't delete a completed order

        // Inform the user if there are no qualifying sales
        if (sales.size() == 0) {
            // Nothing can be shipped because there are no unshipped orders
            view.printErrorMessage("There are no unshipped sales that may be deleted!");
            return;
        }

        // Display the list of sales and ask for selection
        Sale sale = view.selectSale(sales);

        if (sale == null) {
            // user cancelled
            return;
        }

        // Prompt the user for confirmation
        boolean isConfirmed = view.promptForYesNo(String.format("Are you sure you want to DELETE sale %d to %s?", sale.getSaleId(), sale.getCustomerName()));
        if (!isConfirmed) {
            // User cancelled
            return;
        }

        // Call the DAO to delete the sale
        saleDao.deleteSale(sale.getSaleId());

        // Inform the user
        view.printMessage("Sale has been deleted.");
    }
    //*******************************************************
    //endregion Sales menu actions
    //*******************************************************

}
