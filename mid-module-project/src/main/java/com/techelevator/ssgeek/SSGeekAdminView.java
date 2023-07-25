package com.techelevator.ssgeek;

import com.techelevator.util.BasicConsole;
import com.techelevator.ssgeek.model.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

/**
 * SSGeekAdminView is a class that the SSGeekAdminController uses for gathering information
 * from the user and presenting information to the user. It requires an object that implements
 * the BasicConsole interface to handle the mechanics of reading from and writing to the console.
 */

public class SSGeekAdminView {
    // **************************************************************
    // region Printing to console in color
    // **************************************************************
    /*
    https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println has an example of
    printing in color. Here we use (char)27, which is the same character as '\u001B' (hex 1B == dec 27).
    Escape codes for color
    https://en.wikipedia.org/wiki/ANSI_escape_code#Escape_sequences
    +~~~~~~+~~~~~~+~~~~~~~~~~~+
    |  fg  |  bg  |  color    |
    +~~~~~~+~~~~~~+~~~~~~~~~~~+
    |  30  |  40  |  black    |
    |  31  |  41  |  red      |
    |  32  |  42  |  green    |
    |  33  |  43  |  yellow   |
    |  34  |  44  |  blue     |
    |  35  |  45  |  magenta  |
    |  36  |  46  |  cyan     |
    |  37  |  47  |  white    |
    |  39  |  49  |  default  |
    +~~~~~~+~~~~~~+~~~~~~~~~~~+
    */
    private final String FOREGROUND_DEFAULT = (char) 27 + "[39m";
    private final String FOREGROUND_RED = (char) 27 + "[31m";
    private final String FOREGROUND_GREEN = (char) 27 + "[32m";
    private final String FOREGROUND_BLUE = (char) 27 + "[34m";
    // **************************************************************
    // endregion Printing to console in color
    // **************************************************************

    private final BasicConsole console;

    // Constructor expect a console object to print to.
    public SSGeekAdminView(BasicConsole console) {
        this.console = console;
    }

    // **************************************************************
    // region console pass-through methods
    // **************************************************************

    // printMessage passes call through to console
    public void printMessage(String message) {
        console.printMessage(message);
    }

    // printErrorMessage makes the text color RED
    public void printErrorMessage(String message) {
        console.printErrorMessage(FOREGROUND_RED + message + FOREGROUND_DEFAULT);
    }

    // printBanner makes the text color GREEN
    public void printBanner(String message) {
        console.printBanner(FOREGROUND_GREEN + message + FOREGROUND_DEFAULT);
    }

    // promptForYesNo passes call through to console
    public boolean promptForYesNo(String prompt) {
        return console.promptForYesNo(prompt);
    }

    // getMenuSelection prints a BLUE banner, then passes through to console.
    public String getMenuSelection(String menuTitle, String[] options) {
        printBanner(FOREGROUND_BLUE + menuTitle + FOREGROUND_DEFAULT);
        return console.getMenuSelection(options);
    }

    // **************************************************************
    // endregion console pass-through methods
    // **************************************************************

    // **************************************************************
    // region Print lists of objects to the console
    // **************************************************************

    public void listProducts(List<Product> products) {
        printBanner("All Products");
        printProductList(products);
        console.pauseOutput();
    }

    public void listCustomers(List<Customer> customers) {
        printBanner("All Customers");
        printCustomerList(customers);
        console.pauseOutput();
    }

    private void printProductList(List<Product> products) {
        String heading1 = "  Id         Price  Name                                    ";
        String heading2 = "====  ============  ========================================";
        String formatString = "%4d  %12s  %-40s";
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
        printMessage(heading1);
        printMessage(heading2);
        for (Product product : products) {
            String s = String.format(formatString, product.getProductId(), moneyFormat.format(product.getPrice()), product.getName());
            printMessage(s);
        }
    }

    public void printCustomerList(List<Customer> customers) {
        String heading1 = "  Id  Name                      City                      ST";
        String heading2 = "====  ========================  ========================  ==";
        String formatString = "%4d  %-24s  %-24s  %2s";
        printMessage(heading1);
        printMessage(heading2);
        for (Customer customer : customers) {
            String s = String.format(formatString,
                    customer.getCustomerId(),
                    customer.getName(),
                    customer.getCity(),
                    customer.getState()
            );
            printMessage(s);
        }
    }

    public void printSaleList(List<Sale> sales) {
        String heading1 = "  Id  Customer                        Sale Date     Ship Date   ";
        String heading2 = "====  ==============================  ============  ============";
        String formatString = "%4d  %-30s  %-12s  %-12s";
        DateTimeFormatter dtFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        printMessage(heading1);
        printMessage(heading2);
        for (Sale sale : sales) {
            String s = String.format(formatString,
                    sale.getSaleId(),
                    sale.getCustomerName(),
                    dtFormat.
                            format(sale.getSaleDate()),
                    sale.getShipDate() == null ? "" : dtFormat.format(sale.getShipDate()));
            printMessage(s);
        }
    }

    private void printLineItemList(List<LineItem> lineItems) {
        String heading1 = "Product  Name                                                  Qty         Price        Amount";
        String heading2 = "=======  ==================================================  =====  ============  ============";
        String formatString = "%7d  %-50s  %5d %12.2f  %12.2f";
        printMessage(heading1);
        printMessage(heading2);
        for (LineItem lineItem : lineItems) {
            String productName = lineItem.getProductName();
            if (productName.length() > 50) {
                productName = productName.substring(0, 47) + "...";
            }
            String s = String.format(formatString,
                    lineItem.getProductId(),
                    productName,
                    lineItem.getQuantity(),
                    lineItem.getPrice(),
                    lineItem.getExtendedPrice());
            printMessage(s);
        }
    }
    // **************************************************************
    // endregion Print lists of objects to the console
    // **************************************************************

    // **************************************************************
    // region Prompt the user to select an object from the list
    // **************************************************************

    public Product selectProduct(List<Product> products) {
        while (true) {
            printProductList(products);
            Integer productId = console.promptForInteger("Enter product id [0 to cancel]: ");
            if (productId == null || productId == 0) {
                return null;
            }
            Product selectedProduct = findProductById(products, productId);
            if (selectedProduct != null) {
                return selectedProduct;
            }
            printErrorMessage("That's not a valid id, please try again.");
        }
    }

    public Customer selectCustomer(List<Customer> customers) {
        while (true) {
            printCustomerList(customers);
            Integer customerId = console.promptForInteger("Enter customer id [0 to cancel]: ");
            if (customerId == null || customerId == 0) {
                return null;
            }
            Customer selectedCustomer = findCustomerById(customers, customerId);
            if (selectedCustomer != null) {
                return selectedCustomer;
            }
            printErrorMessage("That's not a valid id, please try again.");
        }
    }

    public Sale selectSale(List<Sale> sales) {
        while (true) {
            printSaleList(sales);
            Integer saleId = console.promptForInteger("Enter sale id for details [0 to return]: ");
            if (saleId == null || saleId == 0) {
                return null;
            }
            Sale selectedSale = findSaleById(sales, saleId);
            if (selectedSale != null) {
                return selectedSale;
            }
            printErrorMessage("That's not a valid id, please try again.");
        }
    }
    // **************************************************************
    // endregion Prompt the user to select an object from the list
    // **************************************************************

    // **************************************************************
    // region Find an object in the list
    // **************************************************************
    private Product findProductById(List<Product> products, int productId) {
        for (Product product : products) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    private Sale findSaleById(List<Sale> sales, int saleId) {
        for (Sale sale : sales) {
            if (sale.getSaleId() == saleId) {
                return sale;
            }
        }
        return null;
    }

    private Customer findCustomerById(List<Customer> customers, int customerid) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerid) {
                return customer;
            }
        }
        return null;
    }
    // **************************************************************
    // endregion Find an object in the list
    // **************************************************************

    // **************************************************************
    // region Print single object details to the console
    // **************************************************************

    public void printCustomerDetail(Customer customer) {
        printMessage(String.format("Customer id: %s", customer.getCustomerId()));
        printMessage(String.format("Customer name: %s", customer.getName()));
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
        printMessage(String.format("Street Address: %s", customer.getStreetAddress1(), " ", customer.getStreetAddress2()));
        printMessage(String.format("City: %s", customer.getCity()));
        printMessage(String.format("State: %s", customer.getState()));
        printMessage(String.format("ZIP Code: %s", customer.getZipCode()));
        console.pauseOutput();
    }

    public void printProductDetail(Product product) {
        printMessage(String.format("Product id: %s", product.getProductId()));
        printMessage(String.format("Product name: %s", product.getName()));
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
        printMessage(String.format("Price: %s", moneyFormat.format(product.getPrice())));
        printMessage(String.format("Image name: %s", product.getImageName()));
        printMessage(String.format("Description: %s", product.getDescription()));
        console.pauseOutput();
    }

    public void printSaleDetail(Sale sale, List<LineItem> lineItems, Customer customer) {
        printBanner(String.format("Details for Sale %s", sale.getSaleId()));

        // Customer
        printMessage(String.format("Customer: %d %s", customer.getCustomerId(), customer.getName()));
        printMessage(String.format("\t%s", customer.getStreetAddress1()));
        if (customer.getStreetAddress2() != null) {
            printMessage(String.format("\t%s", customer.getStreetAddress2()));
        }
        printMessage(String.format("\t%s, %s %s", customer.getCity(), customer.getState(), customer.getZipCode()));

        // Sale
        DateTimeFormatter dtFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        printMessage(String.format("Sale Date: %s", dtFormat.format(sale.getSaleDate())));
        printMessage(String.format("Ship Date: %s", sale.getShipDate() == null ? "** Not shipped **" : dtFormat.format(sale.getShipDate())));

        // Line items
        printMessage("Line Items:");
        printLineItemList(lineItems);
    }

    // **************************************************************
    // endregion Print single object details to the console
    // **************************************************************

    // **************************************************************
    // region Prompt the user for object property information
    // **************************************************************

    public Customer promptForCustomerInformation(Customer existingCustomer) {
        Customer newCustomer = new Customer();
        if (existingCustomer == null) {
            // No default values
            newCustomer.setName(promptForCustomerName(null));
            newCustomer.setStreetAddress1(promptForCustomerStreetAddress1(null));
            newCustomer.setStreetAddress2(promptForCustomerStreetAddress2(null));
            newCustomer.setCity(promptForCustomerCity(null));
            newCustomer.setState(promptForCustomerState(null));
            newCustomer.setZipCode(promptForCustomerZipCode(null));
        } else {
            // This is an update -- make all prompts default to current values
            // Set the id
            newCustomer.setCustomerId(existingCustomer.getCustomerId());
            newCustomer.setName(existingCustomer.getName());
            newCustomer.setStreetAddress1(existingCustomer.getStreetAddress1());
            newCustomer.setStreetAddress2(existingCustomer.getStreetAddress2());
            newCustomer.setCity(existingCustomer.getCity());
            newCustomer.setState(existingCustomer.getState());
            newCustomer.setZipCode(existingCustomer.getZipCode());
        }
        return newCustomer;
    }

    private String promptForCustomerName(String defaultValue) {
        return promptForString("Customer name", true, defaultValue);
    }

    private String promptForCustomerStreetAddress1(String defaultValue) {
        return promptForString("Street 1", true, defaultValue);
    }

    private String promptForCustomerStreetAddress2(String defaultValue) {
        return promptForString("Street 2", false, defaultValue);
    }

    private String promptForCustomerCity(String defaultValue) {
        return promptForString("City", true, defaultValue);
    }

    private String promptForCustomerState(String defaultValue) {
        return promptForString("State (2-letter)", true, defaultValue);
    }

    private String promptForCustomerZipCode(String defaultValue) {
        return promptForZipCode("5-digit Zip Code", true, defaultValue);
    }

    public Product promptForProductInformation(Product existingProduct) {
        Product newProduct = new Product();
        if (existingProduct == null) {
            // No default values
            newProduct.setName(promptForProductName(null));
            newProduct.setDescription(promptForDescription(null));
            newProduct.setPrice(promptForPrice(null));
            newProduct.setImageName(promptForImageName(null));
        } else {
            // This is an update -- make all prompts default to current values
            // Set the id
            newProduct.setProductId(existingProduct.getProductId());
            newProduct.setName(promptForProductName(existingProduct.getName()));
            newProduct.setDescription(promptForDescription(existingProduct.getDescription()));
            newProduct.setPrice(promptForPrice(existingProduct.getPrice()));
            newProduct.setImageName(promptForImageName(existingProduct.getImageName()));
        }
        return newProduct;
    }

    private String promptForProductName(String defaultValue) {
        return promptForString("Product name", true, defaultValue);
    }

    private String promptForDescription(String defaultValue) {
        return promptForString("Description", false, defaultValue);
    }

    private BigDecimal promptForPrice(BigDecimal defaultValue) {
        return promptForBigDecimal("Price", true, defaultValue);
    }

    private String promptForImageName(String defaultValue) {
        return promptForString("Image name", false, defaultValue);
    }

    private String promptWithDefault(String prompt, Object defaultValue) {
        if (defaultValue != null) {
            return prompt + "[" + defaultValue.toString() + "]: ";
        }
        return prompt + ": ";
    }

    private String promptForString(String prompt, boolean required, String defaultValue) {
        prompt = promptWithDefault(prompt, defaultValue);
        while (true) {
            String entry = console.promptForString(prompt);
            if (!entry.isEmpty()) {
                return entry;
            }
            // Entry is empty: see if we have a default, or if empty is OK (!required)
            if (defaultValue != null && !defaultValue.isEmpty()) {
                return defaultValue;
            }
            if (!required) {
                return entry;
            }
            printErrorMessage("A value is required, please try again.");
        }
    }

    private BigDecimal promptForBigDecimal(String prompt, boolean required, BigDecimal defaultValue) {
        prompt = promptWithDefault(prompt, defaultValue);
        while (true) {
            BigDecimal entry = console.promptForBigDecimal(prompt);
            if (entry != null) {
                return entry;
            }
            // Entry is empty: see if we have a default, or if empty is OK (!required)
            if (defaultValue != null) {
                return defaultValue;
            }
            if (!required) {
                return BigDecimal.valueOf(0.0);
            }
            printErrorMessage("A value is required, please try again.");
        }
    }

    private String promptForZipCode(String prompt, boolean required, String defaultValue) {
        prompt = promptWithDefault(prompt, defaultValue);
        while (true) {
            String entry = console.promptForString(prompt);

            if (!entry.isEmpty()) {
                // Zip code entry must have a length of 5
                if (entry.length() == 5) {
                    return entry;
                }
                printErrorMessage("Zip Code must have a length of 5, please try again.");
            }
            // Entry is empty: see if we have a default, or if empty is OK (!required)
            if (defaultValue != null && !defaultValue.isEmpty()) {
                return defaultValue;
            }
            if (!required) {
                return entry;
            }
            printErrorMessage("A value is required, please try again.");
        }
    }
    // **************************************************************
    // endregion Prompt the user for object property information
    // **************************************************************

}
