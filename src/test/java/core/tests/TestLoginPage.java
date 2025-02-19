package core.tests;

import core.pages.HPage;
import core.pages.LoginPage;
import core.utilities.Test1;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestLoginPage extends Test1{

    // Test class for verifying login functionality.
    @Test
    public void testSignIn() {

        LoginPage loginPage = new LoginPage(driver);
        HPage homePage = new HPage(driver);

        // Navigates to the login page.
        loginPage.openLoginPage();

        // Defines test data for login.
        String email = "grejsibrahimi@gmail.com";
        String password = "grejs123!";
        loginPage.login(email, password);

        // Verifies the logged-in username after successful login.
        String loggedInUsername = loginPage.getLoggedInUsername();
        Assert.assertTrue(loggedInUsername.contains("Grejs Ibrahimi"), "Login failed!");

        // Signs out after successful login verification.
        homePage.signOut();
    }
}