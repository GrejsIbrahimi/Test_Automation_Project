package core.tests;

import core.globals.Globals;
import core.pages.*;
import core.utilities.Test1;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EmptySCTest extends Test1 {

    // Test class for verifying empty shopping cart functionality.
    private ShoppingCPage shoppingCardPage;

    // Logs in the user before each test method.
    @BeforeMethod
    public void signInToLuma() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginPage();
        loginPage.login(Globals.createdEmail, Globals.password);
        Assert.assertTrue(loginPage.isUserLoggedIn(), "❌ Login failed!"); // Changed to English
    }

    // Tests emptying the shopping cart.
    @Test
    public void testEmptyShoppingCart() {
        shoppingCardPage = new ShoppingCPage(driver);

        // Navigates to the shopping cart page.
        shoppingCardPage.goToShoppingCart();

        // Gets the initial item count in the shopping cart.
        int initialCount = shoppingCardPage.getNumberOfCartItems();
        System.out.println(" Initial number of items: " + initialCount); 

        // Checks if the cart was initially empty.
        if (initialCount == 0) {
            System.out.println("Cart was initially empty. No items to delete."); 
            Assert.assertTrue(shoppingCardPage.isCartEmpty(),
                    "Cart should be empty and display the appropriate message."); 
            return;
        }

        // Deletes items from the cart until it is empty.
        while (shoppingCardPage.getNumberOfCartItems() > 0) {
            int countBefore = shoppingCardPage.getNumberOfCartItems();
            shoppingCardPage.deleteFirstCartItem();
            int countAfter = shoppingCardPage.getNumberOfCartItems();

            // Verifies the item count decreased after deletion.
            Assert.assertEquals(countAfter, countBefore - 1,
                    "Number of items did not decrease after deletion!"); 
            System.out.println("Item deleted. Remaining items: " + countAfter); 
        }

        // Verifies the cart is empty after deleting all items.
        Assert.assertTrue(shoppingCardPage.isCartEmpty(),
                "Cart is not empty after deleting all items!"); 
        System.out.println("Cart is empty."); 
        // Closes the browser.
        driver.quit();
    }
}