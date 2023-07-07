package model; /**
 * Supermarket Customer check-out and Cashier simulation
 *
 * @author hbo-ict@hva.nl
 */


import java.time.LocalTime;
import java.util.*;

public class Customer implements Comparable<Customer> {
    //    petya
//    private static Set<LocalTime> customersQueuedAt = new HashSet<>();
    //    We gaan er van uit dat de aankomsttijd van een klant uniek is.
    private LocalTime queuedAt;      // time of arrival at cashier
    private String zipCode;          // zip-code of the customer
    private Map<Product, Integer> itemsCart = new HashMap<>();     // items purchased by customer
    private int actualWaitingTime;   // actual waiting time in seconds before check-out
    private int actualCheckOutTime;  // actual check-out time at cashier in seconds

    public Customer() {
    }

    public Customer(LocalTime queuedAt, String zipCode) {
//        if (customersQueuedAt.contains(queuedAt))
//            throw new IllegalArgumentException("QueuedAt must be unique: " + queuedAt + " is already in use.");
        this.queuedAt = queuedAt;
        this.zipCode = zipCode;
//        customersQueuedAt.add(queuedAt);
    }

    // TODO stap 1: implement relevant overrides of equals(), hashcode(), compareTo for
    //  model classes to be able to use them in sets, maps
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Customer customer = (Customer) obj;
        return Objects.equals(queuedAt, customer.queuedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queuedAt);
    }

    //  what to compareTo: this is to sort later by? price?
    @Override
    public int compareTo(Customer o) {
        return this.queuedAt.compareTo(o.queuedAt);
    }

    /**
     * calculate the total number of items purchased by this customer
     * @return
     */
    public int getNumberOfItems() {
        int numItems = 0;
        // TODO stap 2: Calculate the total number of items
        for (Map.Entry<Product, Integer> entry : itemsCart.entrySet()) {
            numItems += entry.getValue();
        }
        return numItems;
    }


    public void addToCart(Product product, int number) {
        // TODO stap 2: When adding a number of products to the cart,
        //  the number should be adjusted when product already exists in cart
        if (product.getCode() == null)
            throw new IllegalArgumentException("Product code cant be null");

        boolean productExistsInCart = false;
        for (Map.Entry<Product, Integer> item : itemsCart.entrySet()) {
            if (product.getCode().equals(item.getKey().getCode())) {
                itemsCart.put(item.getKey(), item.getValue()+number);
                productExistsInCart = true;
                break;
            }
        }
        if (!productExistsInCart)
            itemsCart.put(new Product(product.getCode(), product.getDescription(), product.getPrice()), number);
    }

    public double calculateTotalBill() {
//        double totalBill = 0.0; ORIGINAL CODE
        // stap 5: Calculate the total cost of all items, use a stream TODO
        return itemsCart.entrySet().stream().mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();
//        return totalBill; ORIGINAL CODE
    }

    public String toString() {
        StringBuilder result = new StringBuilder("queuedAt: " + queuedAt);
        result.append("\nzipCode: " + zipCode);
        result.append("\nPurchases:");
        for (Product product : itemsCart.keySet()) {
            result.append("\n\t" + product.getDescription() + ": " + itemsCart.get(product));
        }
        result.append("\n");
        return result.toString();
    }


    public LocalTime getQueuedAt() {
        return queuedAt;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Map<Product, Integer> getItemsCart() {
        return itemsCart;
    }

}
