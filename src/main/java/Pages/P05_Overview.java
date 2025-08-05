package Pages;

import Utilities.LogsUtils;
import Utilities.Utility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class P05_Overview {
    private final WebDriver driver;

    private final By subtotal = By.className("summary_subtotal_label");
    private final By tax = By.className("summary_tax_label");
    private final By total = By.className("summary_total_label");
    private final By finishButton = By.id("finish");


    public P05_Overview(WebDriver driver) {
        this.driver = driver;
    }

    public Float getSubtotal() {
        return Float.parseFloat(Utility.getText(driver, subtotal).replace("Item total: $", ""));
    }

    public Float getTax() {
        return Float.parseFloat(Utility.getText(driver, tax).replace("Tax: $", ""));
    }

    public Float getTotal() {
        LogsUtils.info("total price " + Utility.getText(driver, total).replace("Total: $", ""));
        return Float.parseFloat(Utility.getText(driver, total).replace("Total: $", ""));
    }

    public String calculateTotalPrice() {
        double total = getSubtotal() + getTax();
        LogsUtils.info("price + taxes " + String.format("%.2f", total));
        return String.format("%.2f", total);
    }

    public boolean comparingPrices() {

        return calculateTotalPrice().equals(String.valueOf(getTotal()));
    }

    public P06_FinishingOrderPage clickOnFinishButton() {
        Utility.clickingOnElement(driver, finishButton);
        return new P06_FinishingOrderPage(driver);
    }
}
