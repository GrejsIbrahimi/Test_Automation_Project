package core.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CreateAccountPage {
    private WebDriver driver;

    // Locators for different elements on the registration page
    private By createAccountLink = By.xpath("//a[contains(text(),'Create an Account')]");
    private By firstNameField = By.id("firstname");
    private By lastNameField = By.id("lastname");
    private By emailField = By.id("email_address");
    private By passwordField = By.id("password");
    private By confirmPasswordField = By.id("password-confirmation");
    private By createAccountButton = By.xpath("//button[@title='Create an Account']");
    private By successMessage = By.xpath("//div[@class='message-success success message']");
    private By userMenu = By.xpath("//button[@data-action='customer-menu-toggle']");
    private By signOutLink = By.xpath("//a[contains(text(), 'Sign Out')]");

    public CreateAccountPage(WebDriver driver) {
        this.driver = driver;
    }

    // Navigates to the registration page
    public void navigateToRegistration() {
        driver.findElement(createAccountLink).click();
    }

    // Fills out the registration form with provided details
    public void fillRegistrationForm(String firstName, String lastName, String email, String password) {
        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(confirmPasswordField).sendKeys(password);
    }

    // Clicks the button to submit the registration form
    public void submitRegistration() {
        driver.findElement(createAccountButton).click();
    }

    // Retrieves the success message after account creation
    public String getSuccessMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        return successMsg.getText();
    }

    // Signs out the user from their account
    public void signOut() {
        driver.findElement(userMenu).click();
        driver.findElement(signOutLink).click();
    }
}