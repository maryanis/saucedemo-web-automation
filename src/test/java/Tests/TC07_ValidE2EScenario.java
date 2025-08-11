package Tests;

import Listeners.IInvokedMethodListenerClass;
import Listeners.ITestResultListenerClass;
import Pages.P01_LoginPage;
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
public class TC07_ValidE2EScenario {
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

    @Test
    public void ValidE2E() throws IOException {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton()
                .selectRandomProducts(3, 6)
                .clickOnCartIcon()
                .clickOnCheckoutButton()
                .fillingInformation(FIRSTNAME, LASTNAME, ZIPCODE)
                .clickOnContinueButton()
                .clickOnFinishButton()
                .clickOnBackToHomeButton();

        Assert.assertTrue(Utility.verifyUrl(getDriver(), DataUtils.getPropertyValue("environment", "HOME_URL")));
    }

    @Test
    public void ValidE2EWithSortingProducts() throws IOException {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton()
                .sortProductsByPriceFromHighToLow()
                .sortProductsByPriceFromLowToHigh()
                .sortProductsAlphabeticallyAtoZ()
                .sortProductsAlphabeticallyZtoA()
                .selectRandomProducts(3, 6)
                .clickOnCartIcon()
                .clickOnCheckoutButton()
                .fillingInformation(FIRSTNAME, LASTNAME, ZIPCODE)
                .clickOnContinueButton()
                .clickOnFinishButton()
                .clickOnBackToHomeButton();

        Assert.assertTrue(Utility.verifyUrl(getDriver(), DataUtils.getPropertyValue("environment", "HOME_URL")));
    }

    @Test
    public void ValidE2EWithRemovingProductsFromCartTC() throws IOException {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton()
                .addAllProductsToCart()
                .clickOnCartIcon()
                .removingAllProducts()
                .clickOnContinueShoppingButton()
                .selectRandomProducts(4, 6)
                .clickOnCartIcon()
                .clickOnCheckoutButton()
                .fillingInformation(FIRSTNAME, LASTNAME, ZIPCODE)
                .clickOnContinueButton()
                .clickOnFinishButton()
                .clickOnBackToHomeButton();

        Assert.assertTrue(Utility.verifyUrl(getDriver(), DataUtils.getPropertyValue("environment", "HOME_URL")));
    }

    @AfterMethod
    public void quit() {
        quitDriver();
    }

}
