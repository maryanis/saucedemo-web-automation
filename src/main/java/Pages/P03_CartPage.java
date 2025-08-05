package Pages;

import Utilities.LogsUtils;
import Utilities.Utility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;


public class P03_CartPage {

    private final WebDriver driver;
    private final By pricesLocator = By.xpath(
            "//button[contains(@class,'btn_secondary') and contains(translate(normalize-space(.),'remove','REMOVE'),'REMOVE')]" +
                    "/preceding-sibling::div[contains(@class,'inventory_item_price')]");

    private final By checkoutLink = By.id("checkout");

    public P03_CartPage(WebDriver driver) {
        this.driver = driver;
    }


    public String getTotalPrice() {
        try {

            By removeButtons = By.xpath("//button[contains(@class,'btn_secondary') and contains(translate(normalize-space(.),'remove','REMOVE'),'REMOVE')]");
            Utility.generalWait(driver).until(ExpectedConditions.visibilityOfElementLocated(removeButtons));

            By pricesLocator = By.xpath(
                    "//button[contains(@class,'btn_secondary') and contains(translate(normalize-space(.),'remove','REMOVE'),'REMOVE')]" +
                            "/preceding-sibling::div[contains(@class,'inventory_item_price')]"
            );

            List<WebElement> pricesOfSelectedProducts = driver.findElements(pricesLocator);
            LogsUtils.info("Number of Selected items " + pricesOfSelectedProducts.size());

            float total = 0f;
            for (WebElement el : pricesOfSelectedProducts) {
                String text = el.getText().trim().replace("$", "");
                total += Float.parseFloat(text);
            }
            String result = String.format("%.2f", total);
            LogsUtils.info("Total price of selected items = " + result);
            return result;
        } catch (Exception e) {
            LogsUtils.error("getTotalPriceOfSelectedProducts error: " + e.getMessage());
            return "0";
        }
    }

    public boolean comparePrices(String price) {
        return getTotalPrice().equals(price);
    }

    public P04_CheckoutPage clickOnCheckoutButton() {
        Utility.clickingOnElement(driver, checkoutLink);
        return new P04_CheckoutPage(driver);
    }


}
