package model;

import java.util.Objects;

/**
 * Supermarket Customer check-out
 *
 * @author hbo-ict@hva.nl
 */
// petya adds implemets Comparable<>
public class Product implements Comparable<Product> {
//    petya
//    private static Set<String> codes = new HashSet<>();
    private String code;            // a unique product code; identical codes designate identical products
    private String description;     // the product description, useful for reporting
    private double price;           // the product's price

    public Product() {
    }

    public Product(String code, String description, double price) {
//        petya
//        if (codes.contains(code))
//            throw new IllegalArgumentException("Product code must be unique.The code: "+code+" is already in use.");
        this.code = code;
        this.description = description;
        this.price = price;
//        codes.add(code);
    }

    // TODO Stap 1: implement relevant overrides of equals(), hashcode(), compareTo for
    //  model classes to be able to use them in sets, maps

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Product product = (Product) obj;
//        return Objects.equals(code, product.code) &&
//                Objects.equals(description, product.description) &&
//                Double.compare(product.price, price) == 0;
        return Objects.equals(code, product.code); // code should be unique
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
//        return Objects.hash(code, description, price);
    }

    //  what to compareTo: this is to sort later by? price?hash
    @Override
    public int compareTo(Product o) {
        return this.code.compareTo(o.code);
    }

//    public String toString() {
//        return description;
//    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

}
