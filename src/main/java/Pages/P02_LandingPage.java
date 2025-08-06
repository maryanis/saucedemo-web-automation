package Pages;

import Utilities.LogsUtils;
import Utilities.Utility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class P02_LandingPage {
    private static List<WebElement> allProducts;
    private static List<WebElement> selectedProducts;
    private final WebDriver driver;
    private final By addToCartButtonForAllProducts = By.xpath("//button[@class]");
    private final By numberOfProductsOnCartIcon = By.xpath("//span[contains(@class,'shopping_cart_badge')]");
    private final By numberOfSelectedProducts = By.xpath("//button[contains(@class,'btn_secondary')]");
    private final By cartIcon = By.xpath("//a[contains(@class,'shopping_cart_link')]");
    private final By sortIcon = By.className("product_sort_container");
    private final By productName = By.className("inventory_item_name");
    private final By productPrice = By.className("inventory_item_price");


    public P02_LandingPage(WebDriver driver) {
        this.driver = driver;
    }

    public P02_LandingPage selectRandomProducts(int numberOfProductsNeeded, int totalNumberOfProducts) {
        Set<Integer> randomNumbers = Utility.generateUniqueNumber(numberOfProductsNeeded, totalNumberOfProducts);
        for (int random : randomNumbers) {
            By addToCartButtonForAllProducts = By.xpath("(//button[@class])[" + random + "]");
            Utility.clickingOnElement(driver, addToCartButtonForAllProducts);
            LogsUtils.info("Random Number " + random);
        }
        return this;
    }

    public P02_LandingPage addAllProductsToCart() {
        allProducts = driver.findElements(addToCartButtonForAllProducts);
        LogsUtils.info("number of all products " + allProducts.size());
        for (int i = 1; i <= allProducts.size(); i++) {
            By addToCartButtonForAllProducts = By.xpath("(//button[@class])[" + i + "]");
            Utility.clickingOnElement(driver, addToCartButtonForAllProducts);
        }
        return this;
    }

    public String getNumberOfProductsOnCartIcon() {
        try {
            LogsUtils.info("number of products on cart " + Utility.getText(driver, numberOfProductsOnCartIcon));
            return Utility.getText(driver, numberOfProductsOnCartIcon);
        } catch (Exception e) {
            LogsUtils.error(e.getMessage());
            return "0";
        }
    }

    public String getNumberOfSelectedProducts() {
        try {
            selectedProducts = driver.findElements(numberOfSelectedProducts);
            LogsUtils.info("number of selected products " + selectedProducts.size());
            return String.valueOf(selectedProducts.size());
        } catch (Exception e) {
            LogsUtils.error(e.getMessage());
            return "0";
        }
    }

    public boolean comparingNumberOfProductsWithSelected() {
        return getNumberOfProductsOnCartIcon().equals(getNumberOfSelectedProducts());
    }

    public P03_CartPage clickOnCartIcon() {
        Utility.clickingOnElement(driver, cartIcon);
        return new P03_CartPage(driver);
    }


    public String getTotalPriceOfSelectedProducts() {
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

    public P02_LandingPage sortProductsAlphabeticallyAtoZ() {
        Select dropdown = new Select(driver.findElement(sortIcon));
        dropdown.selectByValue("az");
        LogsUtils.info("Sorted A to Z");
        return this;
    }

    public boolean checkSortingFromAtoZ() {
        List<WebElement> productNameElements = driver.findElements(productName);
        List<String> actualProductNames = new ArrayList<>();
        for (WebElement element : productNameElements) {
            actualProductNames.add(element.getText());
        }

        List<String> expectedSortedNames = new ArrayList<>(actualProductNames);
        Collections.sort(expectedSortedNames);

        LogsUtils.info("Actual: " + actualProductNames);
        LogsUtils.info("Expected: " + expectedSortedNames);

        return actualProductNames.equals(expectedSortedNames);
    }

    public P02_LandingPage sortProductsAlphabeticallyZtoA() {
        Select dropdown = new Select(driver.findElement(sortIcon));
        dropdown.selectByValue("za");
        LogsUtils.info("Sorted Z to A");
        return this;
    }

    public boolean checkSortingFromZtoA() {
        List<WebElement> productNameElements = driver.findElements(productName);
        List<String> actualProductNames = new ArrayList<>();
        for (WebElement element : productNameElements) {
            actualProductNames.add(element.getText());
        }

        List<String> expectedSortedNames = new ArrayList<>(actualProductNames);
        expectedSortedNames.sort(Collections.reverseOrder());


        LogsUtils.info("Actual: " + actualProductNames);
        LogsUtils.info("Expected: " + expectedSortedNames);

        return actualProductNames.equals(expectedSortedNames);
    }

    public P02_LandingPage sortProductsByPriceFromLowToHigh() {
        Select dropdown = new Select(driver.findElement(sortIcon));
        dropdown.selectByValue("lohi");
        LogsUtils.info("Sorted by price from low to high");
        return this;
    }

    public boolean checkSortingByPricesFromLowToHigh() {
        List<WebElement> priceElements = driver.findElements(By.className("inventory_item_price"));
        List<Double> actualPrices = new ArrayList<>();

        for (WebElement element : priceElements) {
            String priceText = element.getText().replace("$", "");
            actualPrices.add(Double.parseDouble(priceText));
        }
        List<Double> expectedSortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedSortedPrices);
        LogsUtils.info("Actual: " + actualPrices);
        LogsUtils.info("Expected: " + expectedSortedPrices);

        return actualPrices.equals(expectedSortedPrices);

    }

    public P02_LandingPage sortProductsByPriceFromHighToLow() {
        Select dropdown = new Select(driver.findElement(sortIcon));
        dropdown.selectByValue("hilo");
        LogsUtils.info("Sorted by price from high to low");
        return this;
    }

    public boolean checkSortingByPricesFromHighToLow() {
        List<WebElement> priceElements = driver.findElements(By.className("inventory_item_price"));
        List<Double> actualPrices = new ArrayList<>();

        for (WebElement element : priceElements) {
            String priceText = element.getText().replace("$", "");
            actualPrices.add(Double.parseDouble(priceText));
        }
        List<Double> expectedSortedPrices = new ArrayList<>(actualPrices);
        expectedSortedPrices.sort(Collections.reverseOrder());

        LogsUtils.info("Actual: " + actualPrices);
        LogsUtils.info("Expected: " + expectedSortedPrices);

        return actualPrices.equals(expectedSortedPrices);

    }


}


