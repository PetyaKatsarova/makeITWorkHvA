package model; /**
 * Supermarket Customer check-out and Cashier simulation
 *
 * @author hbo-ict@hva.nl
 */


import java.time.LocalTime;
import java.util.*;

public class Customer implements Comparable<Customer> {
    private LocalTime queuedAt;      // time of arrival at cashier, this is unique val
    private String zipCode;          // zip-code of the customer
    private Map<Product, Integer> itemsCart = new HashMap<>();     // items purchased by customer
    private int actualWaitingTime;   // actual waiting time in seconds before check-out
    private int actualCheckOutTime;  // actual check-out time at cashier in seconds

    public Customer() {
    }

    public Customer(LocalTime queuedAt, String zipCode) {
        this.queuedAt = queuedAt;
        this.zipCode = zipCode;
    }

    //stap 1: implement relevant overrides of equals(), hashcode(), compareTo for
    //  model classes to be able to use them in sets, maps  TODO
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
        // stap 2: When adding a number of products to the cart,
        //  the number should be adjusted when product already exists in cart TODO
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
        // stap 5: Calculate the total cost of all items, use a stream TODO
        return itemsCart.entrySet().stream().mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();
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
