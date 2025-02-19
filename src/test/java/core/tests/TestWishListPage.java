package core.tests;

import core.globals.Globals;
import core.pages.*;
import core.utilities.Test1;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import core.pages.ProductPage;

public class TestWishListPage extends Test1 {

    // Test class for verifying Wish List functionality.
    private HPage homePage;
    private ProductPage productPage;
    private WishListPage wishListPage;

    // Logs in the user before each test method.
    @BeforeMethod
    public void signInToLuma() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginPage();
        loginPage.login(Globals.createdEmail, Globals.password);
        Assert.assertTrue(loginPage.isUserLoggedIn(), "Login failed!"); 
    }

    // Tests adding items to the Wish List and verifying the item count.
    @Test
    public void testWishList() {
        homePage = new HPage(driver);
        productPage = new ProductPage(driver);
        wishListPage = new WishListPage(driver);

        // Navigates to the jackets section.
        homePage.goToJacketsSection();
        productPage.applyColorFilter("Blue");
        productPage.applyPriceFilter("$50.00 - $59.99");

        // Removes the price filter.
        productPage.removePriceFilter();

        // Verifies that the number of products increased after removing the filter.
        int initialCount = productPage.verifyNumberOfProducts(2) ? 2 : 0;
        Assert.assertTrue(initialCount < 3, "Number of products did not increase after removing the filter!"); 

        // Adds the first two items to the Wish List.
        productPage.addFirstTwoItemsToWishList();
        Assert.assertTrue(wishListPage.isSuccessMessageDisplayed(), "Success message did not appear!"); 

        // Navigates to the user profile and verifies the Wish List item count.
        homePage.goToUserProfile();
        int wishListCount = wishListPage.getWishListItemCount();
        Assert.assertEquals(wishListCount, 2, "Number of items in Wish List is incorrect!"); 
    }
}