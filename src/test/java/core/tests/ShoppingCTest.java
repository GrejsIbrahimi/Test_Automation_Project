package core.tests;

import core.globals.Globals;
import core.pages.*;
import core.utilities.Test1;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ShoppingCTest extends Test1 {

    // Test class for verifying shopping cart functionality.
    private HPage homePage;
    private ProductPage productPage;
    private ShoppingCPage shoppingPage;

    // Logs in the user before each test method.
    @BeforeMethod
    public void signInToLuma() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginPage();
        loginPage.login(Globals.createdEmail, Globals.password);
        Assert.assertTrue(loginPage.isUserLoggedIn(), "Login failed!"); 
    }

    // Tests adding products to the shopping cart and verifying the order total.
    @Test
    public void testShoppingCart() {
        homePage = new HPage(driver);
        productPage = new ProductPage(driver);
        shoppingPage = new ShoppingCPage(driver);

        // Navigates to the jackets section.
        homePage.goToJacketsSection();

        // Applies color and price filters to the product list.
        productPage.applyColorFilter("Blue");
        productPage.applyPriceFilter("$50.00 - $59.99");

        // Adds all filtered items to the cart.
        productPage.addAllItemsToCart();

        // Verifies the success message is displayed after adding items to the cart.
        Assert.assertTrue(shoppingPage.isSuccessMessageDisplayed(),
                "Success message did not appear after adding to cart!"); 

        // Opens the shopping cart from the success message.
        shoppingPage.openShoppingCartFromSuccessMessage();

        // Verifies that the user is on the shopping cart page.
        Assert.assertTrue(shoppingPage.isOnShoppingCartPage(),
                "Not on the Shopping Cart page!"); 

        // Gets the sum of product prices and the order total from the summary section.
        double sumOfPrices = shoppingPage.getSumOfProductPrices();
        double orderTotal = shoppingPage.getOrderTotal();

        System.out.println("Sum of product prices: $" + sumOfPrices); 
        System.out.println("Order Total from 'Summary' section: $" + orderTotal); 

        // Verifies that the sum of product prices matches the order total.
        Assert.assertEquals(sumOfPrices, orderTotal,
                "Sum of prices does not match Order Total!"); 

        System.out.println("Shopping Cart test completed successfully!"); 
    }
}