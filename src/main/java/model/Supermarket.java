package model;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Supermarket Customer and purchase statistics
 *
 * @author hbo-ict@hva.nl
 */
public class Supermarket {

    private String name;                 // name of the case for reporting purposes
    private Set<Product> products;      // a set of products that is being sold in the supermarket
    private Set<Customer> customers;   // a set of customers that have visited the supermarket
    private LocalTime openTime;         // start time of the simulation
    private LocalTime closingTime;      // end time of the simulation
    private static final int INTERVAL_IN_MINUTES = 15; // to use for number of customers and revenues per 15 minute intervals

    public Supermarket() {
        initializeCollections();
    }

    public Supermarket(String name, LocalTime openTime, LocalTime closingTime) {
        this.name = name;
        this.setOpenTime(openTime);
        this.setClosingTime(closingTime);
        initializeCollections();
    }

    public void initializeCollections() {
        // stap 3: create empty data structures for products and customers TODO
        products = new HashSet<>();
        customers = new HashSet<>();
    }

    public int getTotalNumberOfItems() {
        int totalItems = 0;
        // stap 3: calculate the total number of shopped items of all customers TODO
        for (Customer c : customers) {
            totalItems += c.getNumberOfItems();
        }
        return totalItems;
    }

    private void printErrorMessage() {
        System.out.println("No products or customers have been set up...");
    }

    private boolean checkSetupErrorProductCustomers() {
        return this.customers == null || this.products == null ||
                this.customers.size() == 0 || this.products.size() == 0;
    }

    /**
     * report statistics of data of products
     */
    public void printProductStatistics() {
        if (checkSetupErrorProductCustomers()) {
            printErrorMessage();
            return;
        }
        System.out.printf("\nCustomer Statistics of '%s' between %s and %s\n",
                this.name, this.openTime, this.closingTime);
        System.out.println("\n>>>>> Product Statistics of all purchases <<<<<");
        System.out.printf("%d customers have shopped %d items out of %d different products\n",
                this.customers.size(), this.getTotalNumberOfItems(), this.products.size());
        System.out.println();
        System.out.println(">>> Products and total number bought:");
        System.out.println();
        // stap 3: display the description of the products and total number bought per product. TODO
        findNumberOfProductsBought().forEach((key, value) -> {
            System.out.printf("%-31s %3d\n", key.getDescription(), value);
        });
        System.out.println();
        System.out.println(">>> Products and zipcodes");
        System.out.println();
        // structure is: Map<Product, Set<String>> findZipcodesPerProduct()
        findZipcodesPerProduct().forEach((p, setZipcodes) -> {
            System.out.printf("%s:\n\t", p.getDescription());
            String zipCodesStr = String.join(", ", setZipcodes);
            System.out.println(zipCodesStr);
        });

        System.out.println();
        System.out.println(">>> Most popular products");
        System.out.println();
        // TODO stap 5: display the product(s) that most customers bought
        System.out.println("Product(s) bought by most customers: ");

        System.out.println();
        System.out.println(">>> Most bought products per zipcode");
        System.out.println();
        // TODO stap 5: display most bought products per zipcode

        System.out.println();
    }

    /**
     * report statistics of the input data of customer
     */
    public void printCustomerStatistics() {
        if (checkSetupErrorProductCustomers()) {
            printErrorMessage();
            return;
        }
        System.out.println("\n>>>>> Customer Statistics of all purchases <<<<<");
        System.out.println();
        //stap 4: calculate and show the customer(s) with the highest bill and most paying customer DONE
        System.out.printf("Customer that has the highest bill of %.2f euro: \n", findHighestBill());
        System.out.println(findMostPayingCustomer()); // changed Customer.toString: from product, to product.getDescription()
        System.out.println();
        System.out.println(">>> Time intervals with number of customers\n");
        Map<LocalTime, Integer> customersPerQuarterhour = countCustomersPerInterval(INTERVAL_IN_MINUTES);
        // TODO stap 4: display the times of an interval and number of customers.
//         public Map<LocalTime, Integer> countCustomersPerInterval(int minutes)
        customersPerQuarterhour.forEach((timeInterval, count) -> {
            System.out.printf("Between %s and %s the number of customers was %d\n", timeInterval, timeInterval.plusMinutes(INTERVAL_IN_MINUTES), count);
        });
    }

    /**
     * report statistics of the input data of customer
     */
    public void printRevenueStatistics() {
        System.out.println("\n>>>>> Revenue Statistics of all purchases <<<<<");
        // TODO stap 5: calculate and show the total revenue and the average revenue
        System.out.printf("\nTotal revenue = %.2f\nAverage revenue per customer = %.2f\n", findTotalRevenue(), findAverageRevenue());
        System.out.println();
        System.out.print(">>> Revenues per zip-code:\n");
        System.out.println();
        // TODO stap 5 calculate and show total revenues per zipcode, use forEach and lambda expression
        Map<String, Double> revenues = this.getRevenueByZipcode();

        System.out.println();
        System.out.printf(">>> Revenues per interval of %d minutes\n", INTERVAL_IN_MINUTES);
        System.out.println();
        // TODO stap 5: show the revenues per time interval of 15 minutes, use forEach and lambda expression

    }

    /**
     * @return Map with total number of purchases per product
     */
    public Map<Product, Integer> findNumberOfProductsBought() {
        //stap 3: create an appropriate data structure for the products and their numbers bought TODO
        //  and calculate the contents
        Map<Product, Integer> productsBought = new TreeMap<>(Comparator.comparing(Product::getDescription));
        for (Customer c : customers) {
            for (Map.Entry<Product, Integer> item : c.getItemsCart().entrySet()) {
                Product product = item.getKey();
                if (productsBought.containsKey(product))
                    productsBought.put(product, item.getValue() + productsBought.get(product));
                else
                    productsBought.put(product, item.getValue());
            }
        }
        return productsBought;
    }

    /**
     * builds a map of products and set of zipcodes where product has been bought
     *
     * @return Map with set of zipcodes per product
     */

    public Map<Product, Set<String>> findZipcodesPerProduct() {
        // stap 3: create an appropriate data structure for the products and their zipcodes TODO
        //  and find all the zipcodes per product
        Map<Product, Set<String>> productsWithZipcodes = new HashMap<>();

        for (Customer c : customers) {
            for (Product p : c.getItemsCart().keySet()) {
                // get the set of zip codes for this product, or create a new set if it doesn't exist yet
                Set<String> zipCodes = productsWithZipcodes.getOrDefault(p, new HashSet<>());
                zipCodes.add(c.getZipCode());
                productsWithZipcodes.put(p, zipCodes);
            }
        }
        return productsWithZipcodes;
    }

    /**
     * builds a map of zipcodes with maps of products with number bougth
     *
     * @return Map with map of product and number per zipcode
     */
//        // TODO stap 3: create an appropriate data structure for zipcodes -> NB ** GIVES ERRORS WHEN THE ZIPCODE HAS NULL PURCHASES IN TESTS....
//        //  and the collection of products with numbers bought
//        //  find all the product per zipcode and the number bought by a customer with the zipcode
    public Map<String, Map<Product, Integer>> findNumberOfProductsByZipcode() {
        Map<String, Map<Product, Integer>> resultMap = new HashMap<>();
        for (Customer c : customers) {
            c.getItemsCart().forEach((product, count) -> {
                Map<Product, Integer> currentProductsForZip;
                if (resultMap.containsKey(c.getZipCode())) {
                    currentProductsForZip = resultMap.get(c.getZipCode());
                    currentProductsForZip.put(product, currentProductsForZip.getOrDefault(product, 0) + count);
                } else {
                    currentProductsForZip = new HashMap<>();
                    if (count != 0)
                        currentProductsForZip.put(product, count);
                }
                if (!currentProductsForZip.isEmpty())
                    resultMap.put(c.getZipCode(), currentProductsForZip);
                else
                    resultMap.put(c.getZipCode(), null);
            });
        }
        return resultMap;
    }


    /**
     * calculates a map with number of customers per time interval that is also ordered by time
     *
     * @return Map with number of customers per time interval
     */
    public Map<LocalTime, Integer> countCustomersPerInterval(int minutes) {
//         stap 4: create an appropriate data structure for the number per interval  and calculate its contents DONE

        Map<LocalTime, Integer> result = new TreeMap<>(); // Initialize result map with 0 counts for all intervals
        LocalTime time = openTime;
        while (!time.isAfter(closingTime)) {
            if (!time.equals(closingTime))
                result.put(time, 0);
            time = time.plusMinutes(minutes);
        }
        for (Customer c : customers) {
            LocalTime queuedAt = c.getQueuedAt();
            LocalTime intervalStarts = queuedAt.truncatedTo(ChronoUnit.HOURS).plusMinutes((queuedAt.getMinute() / minutes) * minutes); //bellow are explanations
            if (result.containsKey(intervalStarts))
                result.put(intervalStarts, result.get(intervalStarts) + 1);
        }
        return result;
    }
    /**
     * queuedAt.truncatedTo(ChronoUnit.HOURS):round down the queuedAt time to the start of the hour. For example, if queuedAt is 10:45, this will give you 10:00.
     * queuedAt.getMinute(): This gets the minutes component of the queuedAt time. In our example, this would be 45.
     * (queuedAt.getMinute()/minutes): This is integer division of the minutes component by the interval length. If the interval length is, for example,
     * 15 minutes, and the minutes component is 45, then this would be 45 / 15 = 3.
     * (queuedAt.getMinute()/minutes) * minutes: This essentially rounds down the minutes component to the nearest interval. With our example values,
     * this would be 3 * 15 = 45.
     * queuedAt.truncatedTo(ChronoUnit.HOURS).plusMinutes((queuedAt.getMinute()/minutes) * minutes): Finally, this adds the rounded minutes back to the
     * start of the hour. In our example, this would be 10:00 + 45 minutes = 10:45.
     * So, if queuedAt is 10:45 and the interval length is 15 minutes, then intervalStarts would be 10:45, because 10:45 is the start of the 15 minute
     * interval that 10:45 falls within.
     * Keep in mind that because this is integer division, the remainder is discarded. For example, if queuedAt is 10:46, the result would still be 10:45, because 46 minutes is still within the 10:45-11:00 interval.
     * */


    /**
     * @return value of the highest bill
     */
    public double findHighestBill() {
        // stap 4: use a stream to find the highest bill DONE
        return customers.stream()
                .mapToDouble(Customer::calculateTotalBill)
                .max()
                .orElse(0.0);
//        return 0.0; original code
    }

    /**
     * @return customer with highest bill
     */
    public Customer findMostPayingCustomer() {
        // stap 4: use a stream and the highest bill to find the most paying customer DONE
        return customers.stream()
                .max(Comparator.comparing(Customer::calculateTotalBill))
                .orElse(null);
    }

    /**
     * calculates the total revenue of all customers purchases
     *
     * @return total revenue
     */
    public double findTotalRevenue() {
        // TODO Stap 5: use a stream to find the total of all bills
        return 0.0;
    }

    /**
     * calculates the average revenue of all customers purchases
     *
     * @return average revenue
     */
    public double findAverageRevenue() {
        // TODO Stap 5: use a stream to find the average of the bills
        return 0.0;
    }

    /**
     * calculates a map of aggregated revenues per zip code that is also ordered by zip code
     *
     * @return Map with revenues per zip code
     */
    public Map<String, Double> getRevenueByZipcode() {
        // TODO Stap 5: create an appropriate data structure for the revenue
        //  use stream and collector to find the content

        return null;
    }

    /**
     * finds the product(s) found in the most carts of customers
     *
     * @return Set with products bought by most customers
     */
    public Set<Product> findMostPopularProducts() {
        // TODO Stap 5: create an appropriate data structure for the most popular products and find its contents

        return null;
    }

    /**
     * calculates a map of most bought products per zip code that is also ordered by zip code
     * if multiple products have the same maximum count, just pick one.
     *
     * @return Map with most bought product per zip code
     */
    public Map<String, Product> findMostBoughtProductByZipcode() {
        // TODO Stap 5: create an appropriate data structure for the mostBought
        //  and calculate its contents

        return null;
    }

    /**
     * calculates a map of revenues per time interval based on the length of the interval in minutes
     *
     * @return Map with revenues per interval
     */
    public Map<LocalTime, Double> calculateRevenuePerInterval(int minutes) {
        // TODO Stap 5: create an appropiate data structure for the revenue per time interval
        //  Start time of an interval is a key. Find the total revenue for each interval

        return null;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }


}
