package Pages;

import Utilities.Utility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class P04_CheckoutPage {
    private final WebDriver driver;

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By zipCode = By.id("postal-code");
    private final By continueButton = By.id("continue");


    public P04_CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    public P05_Overview clickOnContinueButton() {
        Utility.clickingOnElement(driver, continueButton);
        return new P05_Overview(driver);
    }

    public P04_CheckoutPage fillingInformation(String firstnameText, String lastnameText, String postalCode) {
        Utility.sendData(driver, firstName, firstnameText);
        Utility.sendData(driver, lastName, lastnameText);
        Utility.sendData(driver, zipCode, postalCode);
        return this;
    }
}
