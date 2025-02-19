package core.tests;

import core.globals.Globals;
import core.pages.CreateAccountPage;
import core.utilities.Test1;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCreateAccountPage extends Test1 {

    // Test class for verifying account creation functionality.
    @Test
    public void testCreateAccount() {

        CreateAccountPage createAccount = new CreateAccountPage(driver);

        // Navigates to the registration page.
        createAccount.navigateToRegistration();

        // Defines test data for account creation.
        String firstName = "test";
        String lastName = "test";
        String email = "test" + System.currentTimeMillis() + "@gmail.com";
        String password = "Test123!";
        Globals.createdEmail = email; // Stores the created email for later use.
        createAccount.fillRegistrationForm(firstName, lastName, email, password);

        // Submits the registration form.
        createAccount.submitRegistration();

        // Verifies the success message after registration.
        String successMsg = createAccount.getSuccessMessage();
        Assert.assertTrue(successMsg.contains("Thank you for registering"), "Registration failed!");

        // Signs out after successful registration.
        createAccount.signOut();
    }
}