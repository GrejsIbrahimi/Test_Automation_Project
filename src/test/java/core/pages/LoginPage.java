package core.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for login elements
    private By signInLink = By.xpath("//a[contains(text(),'Sign In')]");
    private By emailField = By.id("email");
    private By passwordField = By.id("pass");
    private By signInButton = By.id("send2");
    private By userMenu = By.xpath("//div[@class='panel header']//span[@class='logged-in']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Opens the login page by clicking the "Sign In" link
    public void openLoginPage() {
        wait.until(ExpectedConditions.elementToBeClickable(signInLink)).click();
    }

    // Checks if the user is logged in by verifying the presence of the user menu
    public boolean isUserLoggedIn() {
        try {
            WebElement userMenu = driver.findElement(By.xpath("//div[@class='panel header']//span[@class='logged-in']"));
            return userMenu.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Performs login by entering email, password, and clicking the login button
    public void login(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(signInButton).click();
    }

    // Retrieves the logged-in username from the user menu
    public String getLoggedInUsername() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(userMenu)).getText();
    }
}