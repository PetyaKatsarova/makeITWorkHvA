package tests;

import model.Customer;
import model.Product;
import model.Supermarket;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import utilities.SupermarketBuilder;

import java.time.LocalTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class SupermarketTest {

    private Supermarket supermarket1;
    private Supermarket supermarket2;
    private Supermarket supermarket5;

    @BeforeEach
    void setup() {
        supermarket1 = new SupermarketBuilder("src/main/resources/jambi1Json.txt").initializeSupermarket().addProducts().addCustomers().create();
        supermarket2 = new SupermarketBuilder("src/main/resources/jambi2Json.txt").initializeSupermarket().addProducts().addCustomers().create();
        supermarket5 = new SupermarketBuilder("src/main/resources/jambi5Json.txt").initializeSupermarket().addProducts().addCustomers().create();
    }

    @Test
    void t031_customersAndProductsAreLoadedFromFile() {
        t032_customersAndProductsAreLoadedFromFile(this.supermarket1, 5, 1, 10);
        t032_customersAndProductsAreLoadedFromFile(this.supermarket2, 5, 2, 3);
        t032_customersAndProductsAreLoadedFromFile(this.supermarket5, 25, 6, 25);
    }

    private void t032_customersAndProductsAreLoadedFromFile(Supermarket supermarket,
                                                            int nProducts, int nCustomers, int nItems) {
        assertEquals(nProducts, supermarket.getProducts().size());
        assertEquals(nCustomers, supermarket.getCustomers().size());
        assertEquals(nItems, supermarket.getTotalNumberOfItems());
    }

    @Test
    void t033_numberOfProductsByZipcodeIsCorrect() {
        // Get products from supermarket1 purchases
        Product p1 = getProductByCode(supermarket1, "VOE001");
        Product p2 = getProductByCode(supermarket1, "LEV001");
        Product p3 = getProductByCode(supermarket1, "ZUI003");
        // Tests for supermarket1
        assertEquals(Set.of("1015MF"), supermarket1.findNumberOfProductsByZipcode().keySet());
        Map<Product, Integer> numberOfProducts = supermarket1.findNumberOfProductsByZipcode().get("1015MF");
        assertEquals( Set.of(p1, p2, p3 ), numberOfProducts.keySet());
        assertEquals(2, numberOfProducts.get(p1));
        assertEquals(3, numberOfProducts.get(p2));
        assertEquals(5, numberOfProducts.get(p3));
        // Get products from supermarket2 purchases
        p2 = getProductByCode(supermarket2, "BRO001");
        // Tests for supermarket2
        assertEquals(supermarket2.findNumberOfProductsByZipcode().keySet(), Set.of("1013MF"));
        numberOfProducts = supermarket2.findNumberOfProductsByZipcode().get("1013MF");
        assertEquals( Set.of(p1, p2), numberOfProducts.keySet());
        assertEquals(1, numberOfProducts.get(p1));
        assertEquals(2, numberOfProducts.get(p2));
        // Get products from supermarket5 purchases
        p1 = getProductByCode(supermarket5, "SCH001");
        p2 = getProductByCode(supermarket5, "BRO001");
        p3 = getProductByCode(supermarket5, "LEV001");
        Product p4 = getProductByCode(supermarket5, "ZUI003");
//        // Tests for supermarket5
        assertEquals(supermarket5.findNumberOfProductsByZipcode().keySet(),
                Set.of("1015DK", "1015DP", "1014DA", "1016DK"));
        numberOfProducts = supermarket5.findNumberOfProductsByZipcode().get("1015DK");
        assertEquals(numberOfProducts.keySet(), Set.of(p3));
        assertEquals(6, numberOfProducts.get(p3));
        numberOfProducts = supermarket5.findNumberOfProductsByZipcode().get("1015DP");
        assertEquals(numberOfProducts.keySet(), Set.of(p1, p2));
        assertEquals(1, numberOfProducts.get(p1));
        assertEquals(2, numberOfProducts.get(p2));
        numberOfProducts = supermarket5.findNumberOfProductsByZipcode().get("1014DA");
        assertEquals(numberOfProducts.keySet(), Set.of(p1, p3, p4));
        assertEquals(2, numberOfProducts.get(p1));
        assertEquals(5, numberOfProducts.get(p3));
        assertEquals(9, numberOfProducts.get(p4));
         numberOfProducts = supermarket5.findNumberOfProductsByZipcode().get("1016DK");
        assertEquals(numberOfProducts.keySet().size(), 0);
    }

    private Product getProductByCode(Supermarket supermarket, String code) {
        return supermarket.getProducts().stream().filter(product -> product.getCode().equals(code)).findFirst().get();
    }

    @Test
    void t041_highestBillIsCorrect() {
        // stap 6: write the testcode TODO
        assertThat(supermarket1.findHighestBill(), is(closeTo(33.85, 0.001)));
        assertThat(supermarket2.findHighestBill(), is(closeTo(4.75, 0.001)));
        assertThat(supermarket5.findHighestBill(), is(closeTo(46.71, 0.001)));
        //        Assertions.fail("Test not yet implemented ");
    }

    @Test
    void t042_mostPayingCustomerIsCorrect() {
        // TODO stap 6: write the testcode
        // Test for supermarket1
        Customer mostPayingCustomer1 = supermarket1.findMostPayingCustomer();
        double highestBill1 = mostPayingCustomer1.calculateTotalBill();
        supermarket1.getCustomers().forEach(customer -> {
            Assertions.assertTrue(highestBill1 >= customer.calculateTotalBill(),
                    "Found a customer with a higher bill in supermarket1");
        });

        // Test for supermarket2
        Customer mostPayingCustomer2 = supermarket2.findMostPayingCustomer();
        double highestBill2 = mostPayingCustomer2.calculateTotalBill();
        supermarket2.getCustomers().forEach(customer -> {
            Assertions.assertTrue(highestBill2 >= customer.calculateTotalBill(),
                    "Found a customer with a higher bill in supermarket2");
        });

        // Test for supermarket5
        Customer mostPayingCustomer5 = supermarket5.findMostPayingCustomer();
        double highestBill5 = mostPayingCustomer5.calculateTotalBill();
        supermarket5.getCustomers().forEach(customer -> {
            Assertions.assertTrue(highestBill5 >= customer.calculateTotalBill(),
                    "Found a customer with a higher bill in supermarket5");
        });
    }


    @Test
    void t043_customerPerIntervalIsCorrect() {
        Map<LocalTime, Integer> customersPerInterval = produceCustomersPerInterval(supermarket1, 5);
        assertEquals(customersPerInterval.keySet(), Set.of(LocalTime.parse("12:00:00")));
        assertEquals(customersPerInterval.get(LocalTime.parse("12:00:00")), 1);

        customersPerInterval = produceCustomersPerInterval(supermarket2, 5);
        assertEquals(customersPerInterval.keySet(), Set.of(LocalTime.parse("12:00:00")));
        assertEquals(customersPerInterval.get(LocalTime.parse("12:00:00")), 2);

        customersPerInterval = produceCustomersPerInterval(supermarket5, 1);
        assertEquals(customersPerInterval.keySet(), Set.of(LocalTime.parse("12:00:00"),
                LocalTime.parse("12:01:00"), LocalTime.parse("12:02:00"),
                LocalTime.parse("12:03:00"), LocalTime.parse("12:04:00")));
        assertEquals(customersPerInterval.get(LocalTime.parse("12:00:00")), 4);
        assertEquals(customersPerInterval.get(LocalTime.parse("12:01:00")), 1);
        assertEquals(customersPerInterval.get(LocalTime.parse("12:04:00")), 1);
    }

    private Map<LocalTime, Integer> produceCustomersPerInterval(Supermarket supermarket, int minutes){
        return supermarket.countCustomersPerInterval(minutes);
    }

    @Test
    void t051_totalRevenueIsCorrect() {
        // stap 6: write the testcode DONE
//       Assertions.fail("Test not yet implemented ");
        assertThat(supermarket1.findTotalRevenue(), is(closeTo(33.85, 0.001)));
        assertThat(supermarket2.findTotalRevenue(), is(closeTo(5.25, 0.001)));
        assertThat(supermarket5.findTotalRevenue(), is(closeTo(102.74,0.001)));
    }

    @Test
    void t052_averageRevenueIsCorrect() {
        assertThat(supermarket1.findAverageRevenue(), is(closeTo(33.85, 0.001)));
        assertThat(supermarket2.findAverageRevenue(), is(closeTo(2.625, 0.001)));
        assertThat(supermarket5.findAverageRevenue(), is(closeTo(17.123, 0.001)));
    }

    @Test
    void t053_mostPopularProductIsCorrect() {
        assertEquals(produceMostPopularProductsDescriptions(supermarket1),
                Set.of("Douwe Egberts snelfilter 500g", "Verse scharreleieren 4 stuks", "Calve Pindakaas 650g" ));
        assertEquals(produceMostPopularProductsDescriptions(supermarket2), Set.of("Croissant"));
        assertEquals(produceMostPopularProductsDescriptions(supermarket5), Set.of("Calve Pindakaas 650g"));
    }

    private Set<String> produceMostPopularProductsDescriptions(Supermarket supermarket) {
        return supermarket.findMostPopularProducts().stream().
                map(product -> product.getDescription()).collect(Collectors.toSet());
    }

    @Test
    void t054_revenueByZipcodeIsCorrect() {
        assertEquals(supermarket1.getRevenueByZipcode().keySet(), Set.of("1015MF"));
        assertEquals(supermarket2.getRevenueByZipcode().keySet(), Set.of("1013MF"));
        assertThat(supermarket5.getRevenueByZipcode().keySet().toString(), is(Set.of("1014DA, 1015DK, 1015DP, 1016DK").toString()));
        assertThat(supermarket1.getRevenueByZipcode().get("1015MF"), is(closeTo(33.85,0.0001)));
        assertThat(supermarket2.getRevenueByZipcode().get("1013MF"), is(closeTo(5.25,0.0001)));
        assertThat(supermarket5.getRevenueByZipcode().get("1014DA"), is(closeTo(56.79,0.0001)));
        assertThat(supermarket5.getRevenueByZipcode().get("1015DK"), is(closeTo(38.10,0.0001)));
        assertThat(supermarket5.getRevenueByZipcode().get("1015DP"), is(closeTo(7.85,0.0001)));
        assertThat(supermarket5.getRevenueByZipcode().get("1016DK"), is(closeTo(0.0,0.0001)));
    }


    @Test
    void t055_mostBoughtProductByZipCodeIsCorrect() {
        assertThat(supermarket1.findMostBoughtProductByZipcode().get("1015MF").getDescription(), Matchers.is("Verse scharreleieren 4 stuks"));
        assertThat(supermarket2.findMostBoughtProductByZipcode().get("1013MF").getDescription(), Matchers.is("Croissant"));
        assertThat(supermarket5.findMostBoughtProductByZipcode().get("1014DA").getDescription(), Matchers.is("Verse scharreleieren 4 stuks"));
        assertThat(supermarket5.findMostBoughtProductByZipcode().get("1015DK").getDescription(), Matchers.is("Calve Pindakaas 650g"));
        assertThat(supermarket5.findMostBoughtProductByZipcode().get("1015DP").getDescription(), Matchers.is("Croissant"));
        assertNull(supermarket5.findMostBoughtProductByZipcode().get("1016DK"));
    }

    @Test // PETYA
    void t056_getTotalNumberOfItems() {
        assertEquals(10, supermarket1.getTotalNumberOfItems());
        assertEquals(3, supermarket2.getTotalNumberOfItems());
        assertEquals(25, supermarket5.getTotalNumberOfItems());
    }

}