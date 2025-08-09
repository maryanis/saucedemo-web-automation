package Pages;

import Utilities.LogsUtils;
import Utilities.Utility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class P01_LoginPage {
    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessageLocator = By.tagName("h3");

    private final WebDriver driver;

    public P01_LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public P01_LoginPage enterUsername(String usernameText) {
        Utility.sendData(driver, username, usernameText);
        return this;
    }

    public P01_LoginPage enterPassword(String passwordText) {
        Utility.sendData(driver, password, passwordText);
        return this;
    }

    public P02_LandingPage clickLoginButton() {
        Utility.clickingOnElement(driver, loginButton);
        return new P02_LandingPage(driver);
    }

    public boolean assertLoginTC(String expectedValue) {
        System.out.println("Actual URL = " + driver.getCurrentUrl());
        System.out.println("Expected URL = " + expectedValue);
        return driver.getCurrentUrl().equals(expectedValue);
    }

    public String getErrorMessage() {
        String errorMessage = Utility.getText(driver, errorMessageLocator);
        LogsUtils.info(errorMessage);
        return errorMessage;
    }
}
