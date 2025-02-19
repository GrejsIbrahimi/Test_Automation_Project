package core.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductPage {
    // Represents the Product Page in the application.
    private final WebDriver driver;
    private final WebDriverWait wait;
    private JavascriptExecutor jsExecutor;

    private By removePriceButton = By.xpath("//a[@class='action remove' and contains(@title, 'Remove Price')]");

    By loader = By.cssSelector(".loading-mask");
    private By productItem = By.cssSelector(".product-item");

    // Constructor to initialize WebDriver, WebDriverWait, and JavascriptExecutor.
    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    // Applies a color filter to the products.
    public void applyColorFilter(String color) {
        By colorFilter = By.xpath("//div[contains(@class, 'filter-options-title') and text()='Color']");
        WebElement colorFilterElement = wait.until(ExpectedConditions.elementToBeClickable(colorFilter));
        colorFilterElement.click();
        By colorOption = By.xpath("//a[contains(@href, 'color=50')]/div");
        WebElement colorElement = wait.until(ExpectedConditions.elementToBeClickable(colorOption));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", colorElement);
        colorElement.click();
        waitForProductsToUpdate();
    }

    // Applies a price filter to the products.
    public void applyPriceFilter(String priceText) {
        By priceFilter = By.xpath("//div[contains(@class, 'filter-options-title') and text()='Price']");
        WebElement priceFilterElement = wait.until(ExpectedConditions.elementToBeClickable(priceFilter));
        priceFilterElement.click();
        By priceOption = By.xpath("//a[contains(@href, 'price=50-60')]");
        WebElement priceElement = wait.until(ExpectedConditions.elementToBeClickable(priceOption));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", priceElement);
        priceElement.click();
        waitForProductsToUpdate();
    }


 // Verifies that the color filter has been applied correctly.
    public boolean verifyColorFilterApplied(String expectedColor) {
        int retries = 3;
        while (retries > 0) {
            try {
                List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//div[@class='products wrapper grid products-grid']")
                ));

                for (WebElement product : products) {
                    try {
                        product.findElement(By.xpath("//div[@id='option-label-color-93-item-50' and contains(@class, 'selected')]"));
                    } catch (NoSuchElementException e) {
                        System.out.println("Product does not have the color label: " + expectedColor); 
                        return false;
                    }
                }
                return true;
            } catch (StaleElementReferenceException e) {
                System.out.println("Stale element! Retrying... (" + retries + ")"); 
                retries--;
            }
        }
        throw new RuntimeException("Failed to verify color '" + expectedColor + "' after multiple attempts!"); 
    }

    // Verifies the number of displayed products.
    public boolean verifyNumberOfProducts(int expectedCount) {
        int actualCount = getNumberOfDisplayedProducts();
        System.out.println("Check: Expected: " + expectedCount + ", Found: " + actualCount); 
        return actualCount == expectedCount;
    }

    // Verifies that the prices of displayed products are within the expected range.
    public boolean verifyPriceRange(double minPrice, double maxPrice) {
        List<WebElement> priceElements = driver.findElements(By.xpath("//ol[@class='products list items product-items']//span[@class='price']"));
        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replace("$", "").replace(",", "").trim();
            try {
                double price = Double.parseDouble(priceText);
                if (price < minPrice || price > maxPrice) {
                    System.out.println("Product has price outside the range: $" + price); 
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Could not read price: " + priceText); 
                return false;
            }
        }
        return true;
    }
  
 // Waits for the product list to update after applying filters.
    private void waitForProductsToUpdate() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
        } catch (TimeoutException e) {
            System.err.println("Spinner ('loading-mask') did not disappear in time."); 
        }

        waitForAjaxToComplete();

        try {
            WebElement grid = driver.findElement(By.cssSelector(".products.wrapper.grid.products-grid"));
            wait.until(ExpectedConditions.stalenessOf(grid));
        } catch (Exception ignored) {}
    }

    // Waits for AJAX requests to complete.
    public void waitForAjaxToComplete() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15)).until(
                    webDriver -> (Boolean) ((JavascriptExecutor) webDriver)
                            .executeScript("return window.jQuery != undefined && jQuery.active === 0 && document.readyState === 'complete'")
            );
            System.out.println("AJAX completed."); 
        } catch (TimeoutException e) {
            System.err.println("AJAX did not complete within the time."); 
        }
    }
    // Waits for the product grid to update.
    public void waitForProductGridUpdate() {
        WebElement grid = driver.findElement(By.cssSelector(".products.wrapper.grid.products-grid"));
        wait.until(ExpectedConditions.stalenessOf(grid)); // Waits for grid to change
    }

 // Gets the number of displayed product items.
    public int getNumberOfDisplayedProducts() {
        try {
            waitForAjaxToComplete();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));

            List<WebElement> visibleProducts = driver.findElements(
                    By.cssSelector("p#toolbar-amount.toolbar-amount\n")
            );
            int actualCount = visibleProducts.size();
            System.out.println("Actual number of products (VISIBLE & ACTIVE): " + actualCount); 
            return actualCount;
        } catch (TimeoutException e) {
            System.out.println("Timeout occurred while waiting for products."); 
            return 0;
        }
    }

    // Removes the price filter.
    public void removePriceFilter() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));

            WebElement removeButton = wait.until(ExpectedConditions.presenceOfElementLocated(removePriceButton));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", removeButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeButton);

            waitForProductsToUpdate();

            System.out.println("Price filter removed successfully."); 
        } catch (TimeoutException e) {
            throw new RuntimeException("Remove Price button not found within the specified time."); 
        }
    }
 // Waits for the overlay to disappear.
    public void waitForOverlayToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-mask")));
        } catch (TimeoutException e) {
            System.out.println("Overlay did not disappear within the specified time."); 
        }
    }
    // Gets the list of Wish List buttons.
    private List<WebElement> getWishListButtons() {
        return driver.findElements(By.cssSelector(".actions-secondary .towishlist"));
    }
    // Adds the first two items to the Wish List.
    public void addFirstTwoItemsToWishList() {
        List<WebElement> wishListButtons = getWishListButtons();

        if (!wishListButtons.isEmpty()) {
            WebElement firstButton = wishListButtons.get(0);
            scrollAndClick(firstButton);
            System.out.println("Product 1 added to Wish List."); 

            driver.navigate().back();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-item")));
            System.out.println("Returned to the product list."); 

            wishListButtons = getWishListButtons();

            if (wishListButtons.size() > 1) {
                WebElement secondButton = wishListButtons.get(1);
                scrollAndClick(secondButton);
                System.out.println("Product 2 added to Wish List."); 
            } else {
                throw new TimeoutException("⏳ No second product to add to Wish List."); 
            }
        } else {
            throw new TimeoutException("No products to add to Wish List."); 
        }
    }
    // Scrolls to and clicks a button.
    private void scrollAndClick(WebElement button) {
        try {
            try {
                jsExecutor.executeScript("document.querySelector('.overlay, .popup').style.display='none';");
            } catch (Exception e) {
                System.out.println("No overlay or it was not removed."); 
            }

            jsExecutor.executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
            wait.until(ExpectedConditions.elementToBeClickable(button));

            jsExecutor.executeScript("arguments[0].click();", button);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".message-success")));
        } catch (ElementClickInterceptedException e) {
            System.out.println("Click was blocked by another element. Retrying."); 
            jsExecutor.executeScript("arguments[0].click();", button);
        } catch (TimeoutException e) {
            System.out.println("Timeout occurred while waiting for the button."); 
            throw e;
        }
    }
    // Adds all items to the shopping cart.
    public void addAllItemsToCart() {
        List<WebElement> productItems = driver.findElements(By.cssSelector(".product-item"));
        for (int i = 0; i < productItems.size(); i++) {
            try {
                WebElement item = productItems.get(i);
                WebElement sizeOption = item.findElement(By.cssSelector(".swatch-option.text"));
                WebElement addToCartButton = item.findElement(By.cssSelector(".actions-primary button.tocart"));

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", sizeOption);
                sizeOption.click();

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartButton);
                addToCartButton.click();

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));
                System.out.println("Product " + (i + 1) + " added to cart."); 
            } catch (Exception e) {
                System.out.println("Product " + (i + 1) + " was not added due to an error."); 
            }
        }
    }
 }