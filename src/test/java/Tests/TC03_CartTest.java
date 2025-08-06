package Tests;

import Listeners.IInvokedMethodListenerClass;
import Listeners.ITestResultListenerClass;
import Pages.P01_LoginPage;
import Pages.P02_LandingPage;
import Pages.P03_CartPage;
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

public class TC03_CartTest {

    private String USERNAME = DataUtils.getJasonData("validLogin", "Username");
    private String PASSWORD = DataUtils.getJasonData("validLogin", "Password");


    @BeforeMethod
    public void setup() throws IOException {
        setupDriver(getPropertyValue("environment", "Browser"));
        getDriver().get(getPropertyValue("environment", "BASE_URL"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void comparingPricesTC() throws IOException {
        String totalPriceLanding = new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton()
                .selectRandomProducts(2, 6)
                .getTotalPriceOfSelectedProducts();

        Assert.assertNotEquals(totalPriceLanding, "0.00", "No selected items found on Landing (total=0.00)");

        new P02_LandingPage(getDriver()).clickOnCartIcon();

        P03_CartPage cartPage = new P03_CartPage(getDriver());
        Assert.assertTrue(new P03_CartPage(getDriver()).comparePrices(totalPriceLanding));
    }

    @Test
    public void clickOnCheckoutButton() throws IOException {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton()
                .selectRandomProducts(3, 6)
                .clickOnCartIcon()
                .clickOnCheckoutButton();
        Assert.assertTrue(Utility.verifyUrl(getDriver(), DataUtils.getPropertyValue("environment", "CHECKOUT_URL")));

    }

    @Test
    public void clickOnContinueShoppingButton() throws IOException {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton()
                .selectRandomProducts(3, 6)
                .clickOnCartIcon()
                .clickOnContinueShoppingButton();
        Assert.assertTrue(Utility.verifyUrl(getDriver(), DataUtils.getPropertyValue("environment", "HOME_URL")));
    }

    @AfterMethod
    public void quit() {
        quitDriver();
    }
}
