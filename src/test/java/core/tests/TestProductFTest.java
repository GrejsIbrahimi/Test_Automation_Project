package core.tests;

import core.globals.Globals;
import core.pages.HPage;
import core.pages.LoginPage;
import core.pages.ProductPage;
import core.utilities.Test1;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import java.util.List;

public class TestProductFTest extends Test1 {

    // Test class for verifying product filter functionality.
    private HPage homePage;
    private ProductPage productPage;

    // Logs in the user before each test method.
    @BeforeMethod
    public void signInToLuma() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginPage();
        loginPage.login(Globals.createdEmail, Globals.password);
        Assert.assertTrue(loginPage.isUserLoggedIn(), "Login failed!");
    }
    // Tests product filtering by color and price range.
    @Test
    public void testPageFilters() {
        homePage = new HPage(driver);
        productPage = new ProductPage(driver);

        // Navigates to the jackets section.
        homePage.goToJacketsSection();

        // Applies color filter and verifies it.
        String color = "Blue";
        productPage.applyColorFilter(color);
        Assert.assertTrue(productPage.verifyColorFilterApplied(color),
                "Some products do not have the expected color!"); 

        // Applies price range filter.
        String priceRange = "$50.00 - $59.99";
        productPage.applyPriceFilter(priceRange);

        // Gets the actual count of displayed products after filters are applied.
        int actualCount = productPage.getNumberOfDisplayedProducts();
        System.out.println("Number after filters: " + actualCount); 
        Assert.assertTrue(productPage.verifyNumberOfProducts(2),
                "Number of products is incorrect! Expected: 2, Found: " + actualCount); 

        // Verifies that all displayed products are within the expected price range.
        Assert.assertTrue(productPage.verifyPriceRange(50.00, 59.99),
                "Some products are outside the expected price range!"); 
    }

    // Gets the number of displayed product items.
    public int getNumberOfDisplayedProducts() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver is null! Ensure it is initialized correctly."); 
        }
        List elements = driver.findElements(By.cssSelector(".product-item"));
        return elements.size();
    }
}