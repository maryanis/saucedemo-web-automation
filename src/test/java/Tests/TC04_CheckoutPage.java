package Tests;

import Listeners.IInvokedMethodListenerClass;
import Listeners.ITestResultListenerClass;
import Pages.P01_LoginPage;
import Pages.P04_CheckoutPage;
import Utilities.DataUtils;
import Utilities.Utility;
import com.github.javafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

import static DriverFactory.DriverFactory.*;
import static Utilities.DataUtils.getPropertyValue;

@Listeners({IInvokedMethodListenerClass.class, ITestResultListenerClass.class})

public class TC04_CheckoutPage {
    private final String FIRSTNAME = DataUtils.getJasonData("info", "Firstname") + "-" + Utility.getTimestamp();
    private final String LASTNAME = DataUtils.getJasonData("info", "Lastname") + "-" + Utility.getTimestamp();
    private final String ZIPCODE = new Faker().number().digits(5);
    private String USERNAME = DataUtils.getJasonData("validLogin", "Username");
    private String PASSWORD = DataUtils.getJasonData("validLogin", "Password");

    @BeforeMethod
    public void setup() throws IOException {
        setupDriver(getPropertyValue("environment", "Browser"));
        getDriver().get(getPropertyValue("environment", "BASE_URL"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(groups = "E2E", priority = 6)
    public void checkoutInformationTC() throws IOException {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton()
                .selectRandomProducts(3, 6)
                .clickOnCartIcon()
                .clickOnCheckoutButton()
                .fillingInformation(FIRSTNAME, LASTNAME, ZIPCODE)
                .clickOnContinueButton();

        Assert.assertTrue(Utility.verifyUrl(getDriver(), DataUtils.getPropertyValue("environment", "OVERVIEW_URL")));
    }

    @Test
    public void clickOnCancelButton() throws IOException {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton()
                .selectRandomProducts(3, 6)
                .clickOnCartIcon()
                .clickOnCheckoutButton()
                .clickOnCancelButton();
        Assert.assertTrue(Utility.verifyUrl(getDriver(), DataUtils.getPropertyValue("environment", "CART_URL")));
    }

    @Test
    public void checkoutInvalidInformationTC() throws IOException {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton()
                .selectRandomProducts(3, 6)
                .clickOnCartIcon()
                .clickOnCheckoutButton()
                .fillingInformation("", "", "")
                .clickOnContinueButton();

        String expectedErrorMessage = "Error: First Name is required";
        String actualErrorMessage = new P04_CheckoutPage(getDriver()).getErrorMessage();
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage);
        Assert.assertTrue(Utility.verifyUrl(getDriver(), DataUtils.getPropertyValue("environment", "CHECKOUT_URL")));
    }

    @AfterMethod
    public void quit() {
        quitDriver();
    }
}
