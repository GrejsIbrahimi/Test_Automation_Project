package core.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HPage {
    private WebDriver driver;
    private WebDriverWait wait;
    // Locators for navigation and user actions
    private By womenMenu = By.xpath("//span[text()='Women']");
    private By topsMenu = By.xpath("//span[text()='Tops']");
    private By jacketsMenu = By.xpath("//a[span[text()='Jackets']]");
    private By userMenu = By.xpath("//button[@data-action='customer-menu-toggle']");
    private By signOutLink = By.xpath("//a[contains(text(), 'Sign Out')]");

    public HPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Navigates to the Jackets section under Women > Tops
    public void goToJacketsSection() {
        Actions actions = new Actions(driver);

        WebElement women = wait.until(ExpectedConditions.visibilityOfElementLocated(womenMenu));
        actions.moveToElement(women).perform();

        WebElement tops = wait.until(ExpectedConditions.visibilityOfElementLocated(topsMenu));
        actions.moveToElement(tops).perform();

        WebElement jackets = wait.until(ExpectedConditions.elementToBeClickable(jacketsMenu));
        try {
            jackets.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", jackets);
        }
    }

    // Navigates to the user profile page
    public void goToUserProfile() {
        try {
            WebElement profileLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@href, 'customer/account')]")
            ));
            profileLink.click();
            System.out.println("Navigated to the user profile.");
        } catch (TimeoutException e) {
            System.out.println("Profile link not found.");
            try {
                WebElement profileLink = driver.findElement(By.xpath("//a[contains(@href, 'customer/account')]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", profileLink);
            } catch (NoSuchElementException ex) {
                throw new TimeoutException("Profile not found!");
            }
        }
    }

    // Logs out the user from their account
    public void signOut() {
        By userMenuButton = By.xpath("//button[@data-action='customer-menu-toggle']");
        By signOutLink = By.xpath("//a[contains(text(), 'Sign Out')]");

        Actions actions = new Actions(driver);

        WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(userMenuButton));
        menuButton.click();

        try {
            Thread.sleep(3000); // Wait to ensure dropdown is visible
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement signOut = wait.until(ExpectedConditions.elementToBeClickable(signOutLink));
        signOut.click();
    }
}
