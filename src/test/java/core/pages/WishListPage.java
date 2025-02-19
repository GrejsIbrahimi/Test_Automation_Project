package core.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WishListPage {
    // Represents the Wish List page in the application.
    private final WebDriver driver;
    private final WebDriverWait wait;
    private By successMessage = By.cssSelector(".message-success");

    // Constructor to initialize WebDriver and WebDriverWait.
    public WishListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Checks if the success message is displayed.
    public boolean isSuccessMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Retrieves the count of items in the Wish List.
    public int getWishListItemCount() {
        try {
            // Find and click the user button to open the user menu.
            WebElement userButton = driver.findElement(By.cssSelector("span.customer-name"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", userButton);
            userButton.click();
            System.out.println("Clicked on the user icon.");

            // Find and click the Wish List link in the user menu.
            WebElement wishListLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.customer-menu a[href*='wishlist']")
            ));
            wishListLink.click();
            System.out.println("'My Wish List' section."); 

            // Extract the count of items from the Wish List.
            String countText = ((JavascriptExecutor) driver).executeScript(
                    "return document.querySelector('.counter.qty')?.innerText"
            ).toString().trim();
            System.out.println("Number of products in Wish List: " + countText); 

            // Parse the count as an integer.
            String numericPart = countText.replaceAll("[^0-9]", "");

            if (numericPart.isEmpty()) {
                System.err.println("No valid number found in Wish List."); 
                return 0;
            }
            return Integer.parseInt(numericPart);
        } catch (TimeoutException e) {
            throw new RuntimeException("Timeout occurred while finding the number of products."); 
        } catch (NumberFormatException e) {
            throw new RuntimeException("The number of products is not in a valid format."); 
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while reading the Wish List."); 
        }
    }
}