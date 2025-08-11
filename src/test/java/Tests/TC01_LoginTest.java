package Tests;

import Listeners.IInvokedMethodListenerClass;
import Listeners.ITestResultListenerClass;
import Pages.P01_LoginPage;
import Utilities.DataUtils;
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


public class TC01_LoginTest {

    private String USERNAME = DataUtils.getJasonData("validLogin", "Username");
    private String PASSWORD = DataUtils.getJasonData("validLogin", "Password");


    @BeforeMethod
    public void setup() throws IOException {
        setupDriver(getPropertyValue("environment", "Browser"));
        getDriver().get(getPropertyValue("environment", "BASE_URL"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void validLoginTC() throws IOException {
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLoginButton();
        Assert.assertTrue(new P01_LoginPage(getDriver())
                .assertLoginTC(getPropertyValue("environment", "HOME_URL")));
    }

    @Test
    public void invalidUsernameAndInvalidPasswordTC() throws IOException {
        new P01_LoginPage(getDriver())
                .enterUsername("wrong_user")
                .enterPassword("wrong_password")
                .clickLoginButton();
        String expectedErrorMessage = "Epic sadface: Username and password do not match any user in this service";
        String actualErrorMessage = new P01_LoginPage(getDriver()).getErrorMessage();
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage);
        Assert.assertFalse(new P01_LoginPage(getDriver())
                .assertLoginTC(getPropertyValue("environment", "HOME_URL")));
    }

    @AfterMethod
    public void quit() {
        quitDriver();
    }
}
