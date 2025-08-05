package Tests;

import Listeners.IInvokedMethodListenerClass;
import Listeners.ITestResultListenerClass;
import Pages.P01_LoginPage;
import Pages.P02_LandingPage;
import Utilities.DataUtils;
import Utilities.Utility;
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
public class TC02_LandingPage {
    private String USERNAME = DataUtils.getJasonData("validLogin", "Username");
    private String PASSWORD = DataUtils.getJasonData("validLogin", "Password");


    @BeforeMethod
    public void setup() throws IOException {
        setupDriver(getPropertyValue("environment", "Browser"));
        getDriver().get(getPropertyValue("environment", "BASE_URL"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void checkNumberOfSelectedProductsTC() {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton();
        new P02_LandingPage(getDriver())
                .addAllProductsToCart();
        Assert.assertTrue(new P02_LandingPage(getDriver())
                .comparingNumberOfProductsWithSelected());
    }

    @Test
    public void addingRandomProductsToCartTC() {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton();
        new P02_LandingPage(getDriver())
                .selectRandomProducts(3, 6);
        Assert.assertTrue(new P02_LandingPage(getDriver())
                .comparingNumberOfProductsWithSelected());

    }

    @Test
    public void clickOnCartIconTC() throws IOException {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton();
        new P02_LandingPage(getDriver())
                .selectRandomProducts(3, 6)
                .clickOnCartIcon();
        Assert.assertTrue(Utility.verifyUrl(getDriver(), DataUtils.getPropertyValue("environment", "CART_URL")));

    }

    @AfterMethod
    public void quit() {
        quitDriver();
    }
}
