package core.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ShoppingCPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By successMessage = By.cssSelector(".message-success");
    private By shoppingCartLink = By.cssSelector(".message-success a[href*='checkout/cart']");
    private By cartItemPrices = By.cssSelector("td.col.subtotal span.price");
    private By orderTotal = By.cssSelector(".grand.totals .price");

    public ShoppingCPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    // Check if the success message is displayed
    public boolean isSuccessMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    // Click the shopping cart link from the success message
    public void openShoppingCartFromSuccessMessage() {
        WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(shoppingCartLink));
        cartLink.click();
    }
    // Check if the user is on the shopping cart page
    public boolean isOnShoppingCartPage() {
        return driver.getTitle().contains("Shopping Card");
    }

    // Get the sum of all product prices in the cart
    public double getSumOfProductPrices() {
        List<WebElement> prices = driver.findElements(cartItemPrices);
        double sum = 0.0;
        for (WebElement priceElement : prices) {
            String priceText = priceElement.getText().replace("$", "").replace(",", "").trim();
            sum += Double.parseDouble(priceText);
        }
        return sum;
    }
    // Get the total order price
    public double getOrderTotal() {
        WebElement totalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(orderTotal));
        String totalText = totalElement.getText().replace("$", "").replace(",", "").trim();
        return Double.parseDouble(totalText);
    }
    // Get the total number of items in the cart
    public int getNumberOfCartItems() {
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart.item"));
        return cartItems.size();
    }
    // Get the number of visible cart items
    public int getNumberOfCartItemst() {
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart.item"));
        long visibleItems = cartItems.stream().filter(WebElement::isDisplayed).count();
        return (int) visibleItems;
    }
    // Delete the first item in the cart
    public void deleteFirstCartItem() {
        try {
            WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("a.action.action-delete")
            ));
            deleteButton.click();
            wait.until(ExpectedConditions.invisibilityOf(deleteButton));
            System.out.println("The first item was deleted.");
        } catch (TimeoutException e) {
            throw new RuntimeException("The delete button was not found.");
        }
    }
    // Check if the cart is empty
    public boolean isCartEmpty() {
        try {
            WebElement emptyMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='cart-empty']/p[contains(text(),'You have no items in your shopping cart')]")
            ));
            return emptyMessage.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    // Navigate to the shopping cart page
    public void goToShoppingCart() {
        try {
            WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".action.showcart")
            ));
            cartIcon.click();
            System.out.println("Clicked on the cart icon.");

            WebElement miniCart = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".block-minicart")
            ));
            System.out.println("Mini Cart appeared.");

            WebElement viewAndEditCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@class='action viewcart']//span[contains(text(),'View and Edit Cart')]")
            ));
            viewAndEditCartButton.click();
            System.out.println("Clicked 'View and Edit Cart'. Shopping cart opened.");
            wait.until(ExpectedConditions.titleContains("Shopping Cart"));
            System.out.println("We are on the 'Shopping Cart' page.");
        } catch (TimeoutException e) {
            throw new RuntimeException("Failed to open the Shopping Cart!");
        }
    }
}