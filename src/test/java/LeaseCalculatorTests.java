import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.testng.Assert.*;

public class LeaseCalculatorTests {

    @Test
    public void TestDirectLinkOpensAndJumpsToCalculator() throws InterruptedException {
        open("https://www.lhv.ee/et/liising#kalkulaator");

        // Assert the url matches
        String url = WebDriverRunner.url();
        assertEquals(url, "https://www.lhv.ee/et/liising#kalkulaator");

        // Assert the title matches
        String title = title();
        assertEquals(title, "Liising · LHV");

        // Give some time for browser to load
        Thread.sleep(3000);

        // Accept cookies
        $(By.id("acceptPirukas")).click();

        // Verify that screen has jumped to the calculator
        WebElement calculatorElement = ($(By.id("kalkulaator")).toWebElement());
        boolean isVisible = TestUtil.isVisibleInViewport(calculatorElement);
        assertTrue(isVisible, "Browser did not jump to the calculator section!");

        closeWindow();
    }

    @Test
    public void TestAllLabelsAreDisplayedOnSampleMonthlyInstalmentTab() {

        // Open url and accept cookies
        open("https://www.lhv.ee/et/liising#kalkulaator");
        $(By.id("acceptPirukas")).click();

        // Create array with all visible labels
        String[] labelNames = new String[]{"Soovin liisingut", "Liisingu tüüp", "Sõiduki hind", "Sissemakse", "Liisingu periood", "Intress", "Jääkväärtus", "eraisikuna", "juriidilise isikuna", "kapitalirent", "kasutusrent", "Hind sisaldab käibemaksu"};

        // Create array with responding types
        String[] labelTypes = new String[]{"account_type", "lease_type", "price", "initial_percentage", "period", "interest_rate", "reminder_percentage", "account_type-0", "account_type-1", "kap_rent", "kas_rent", "vat_included"};

        // Verify that all array elements are displayed
        for (int i = 0; i < labelNames.length; i++) {
            String label = labelNames[i];
            String type = labelTypes[i];
            assertTrue($(By.xpath("//label[contains(@for, '" + type + "') and text() = '" + label + "']")).isDisplayed(), "Label '" + label + "' is missing!");
        }

        // Verify that hidden label is not displayed
        String hiddenElement = "Käibemaksu tasumine";
        assertFalse($(By.xpath("//label[contains(@class, 'control-label col-xs-12 col-sm-5') and text() = '" + hiddenElement + "']")).isDisplayed(), "Label '" + hiddenElement + "' should not be displayed!");

        closeWindow();
    }

    @Test
    public void TestAllLabelsAreDisplayedOnMaxMonthlyInstalmentTab() {

        // Open url, accept cookies
        open("https://www.lhv.ee/et/liising#max-payment");
        $(By.id("acceptPirukas")).click();

        // Create array with all visible labels
        String[] labelName = new String[]{"Liisingut soovin taotleda", "Perekonnaseis", "Ülalpeetavate arv", "Netosissetulek", "üksi", "koos kaastaotlejaga", "abielus või vabaabielus"};

        // Create array with corresponding types
        String[] labelType = new String[]{"ownership", "marital-status", "dependent-persons", "monthly-income", "ownership-0", "ownership-1", "marital-status-married"};

        // Verify that all array elements are displayed
        for (int i = 0; i < labelName.length; i++) {
            String name = labelName[i];
            String type = labelType[i];
            assertTrue($(By.xpath("//label[contains(@for, '" + type + "') and text() = '" + name + "']")).isDisplayed(), "Label '" + name + "' is missing!");
        }

        closeWindow();
    }

    @Test
    public void TestAllHiddenLabelsAreDisplayedOnSampleMonthlyInstalmentTab() {

        // Open url and accept cookies
        open("https://www.lhv.ee/et/liising#kalkulaator");
        $(By.id("acceptPirukas")).click();

        //Select 'juriidilise isikuna'
        $(By.xpath("//label[contains(@for, 'account_type-1')]")).click();

        // Verify that initially hidden element is now displayed
        String text = "Käibemaksu tasumine";
        assertTrue($(By.xpath("//label[contains(@class, 'control-label col-xs-12 col-sm-5') and text() = '" + text + "']")).isDisplayed(), "Label '" + text + "' is missing!");

        closeWindow();
    }

    @Test
    public void TestAllHiddenLabelsAreDisplayedOnMaxMonthlyInstalmentTab() throws InterruptedException {

        // Open url and accept cookies
        open("https://www.lhv.ee/et/liising#kalkulaator");
        $(By.id("acceptPirukas")).click();

        // Navigate to Maximum Monthly Instalment tab and give browser time to load
        $(By.xpath("//a[contains(@data-toggle, 'tab') and text() = 'Maksimaalne kuumakse']")).click();
        Thread.sleep(1000);

        // Select 'koos kaastaotlejaga' and give browser time to load
        $(By.xpath("//label[contains(@for, 'ownership-1')]")).click();
        Thread.sleep(1000);

        // Create arrays with initially hidden labels
        String[] labelName1 = new String[]{"Põhitaotleja", "Kaastaotleja"};
        String[] labelName2 = new String[]{"Perekonnaseis", "Ülalpeetavate arv", "Netosissetulek", "Perekonnaseis", "Ülalpeetavate arv", "Netosissetulek", "abielus või vabaabielus"};

        // Create array with types for verifying identical labels
        String[] labelType = new String[]{"marital-status", "dependent-persons", "monthly-income", "guarantor-marital-status", "guarantor-dependent-persons", "guarantor-monthly-income", "guarantor-marital-status-married"};

        // Verify that all initially hidden elements are now displayed
        for (String s : labelName1) {
            assertTrue($(By.xpath("//label[contains(@class, 'col-xs-11') and text() = '" + s + "']")).isDisplayed(), "Label '" + s + "' is missing!");
        }

        for (int i = 0; i < labelName2.length; i++) {
            String name = labelName2[i];
            String type = labelType[i];
            assertTrue($(By.xpath("//label[contains(@for, '" + type + "') and text() = '" + name + "']")).isDisplayed(), "Label '" + name + "' is missing!");
        }

        closeWindow();
    }

    @Test
    public void TestCalculatorTitleAndDisclaimer() {

        // Open url and accept cookies
        open("https://www.lhv.ee/et/liising#kalkulaator");
        $(By.id("acceptPirukas")).click();

        // Verify that calculator title is displayed
        String firstPart = "Arvuta";
        String secondPart = "kuumakse";
        assertTrue($(By.xpath("//h2[contains(text(), '" + firstPart + "')]")).isDisplayed(), "Problem with lease calculator title!");
        assertTrue($(By.xpath("//strong[contains(text(), '" + secondPart + "')]")).isDisplayed(), "Problem with lease calculator title!");

        // Verify that disclaimer is displayed
        String disclaimerText = "Tulemus on ligikaudne ja võib erineda sulle pakutavatest tingimustest";
        assertTrue($(By.xpath("//p[contains(text(), '" + disclaimerText + "')]")).isDisplayed(), "Problem with lease calculator disclaimer!");

        closeWindow();
    }

    @Test
    public void TestDefaultValuesOnBothTabs() {

        // Open url and accept cookies
        open("https://www.lhv.ee/et/liising#max-payment");
        $(By.id("acceptPirukas")).click();

        // Verify all checkbox and radio button default values are as expected
        String[] selectedFieldId = new String[]{"account_type-0", "kap_rent", "vat_included", "ownership-0", "marital-status-married"};
        for (String s : selectedFieldId) {
            assertTrue($(By.xpath("//input[@id='" + s + "']")).isSelected(), s + " checkbox/radio button is not selected by default!");
        }

        // Verify all default dropdown values are as expected
        String[] dropdownName = new String[]{"years", "months", "dependent-persons"};
        String[] expectedDropdownValue = new String[]{"72", "0", "1"};

        for (int i = 0; i < dropdownName.length; i++) {
            String actualDropdownValue = $(By.xpath("//select[(@Name='" + dropdownName[i] + "')]")).getValue();
            assertEquals(actualDropdownValue, expectedDropdownValue[i]);
        }

        // Verify all default input values are as expected
        String[] expectedInputValue = new String[]{"15 000", "10", "1500", "4", "10", "1500", "900"};
        String[] inputFieldId = new String[]{"price", "initial_percentage", "initial", "interest_rate", "reminder_percentage", "reminder", "monthly-income"};

        for (int i = 0; i < inputFieldId.length; i++) {
            String actualInputValue = $(By.xpath("//input[(@id='" + inputFieldId[i] + "')]")).getValue();
            assertEquals(actualInputValue, expectedInputValue[i]);
        }

        closeWindow();
    }

    @Test
    public void TestMonthlyIncomeTooltip() throws InterruptedException {

        // Open url and accept cookies
        open("https://www.lhv.ee/et/liising#max-payment");
        $(By.id("acceptPirukas")).click();

        // Get field with tooltip and convert it to WebElement
        WebElement inputFieldElement = ($(By.xpath("//input[contains(@name, 'monthly-income')]")).toWebElement());

        // Make screen jump to the calculator and give time for the necessary field to become visible
        $(By.xpath("//a[contains(@href, '#kalkulaator')]")).click();
        Thread.sleep(1000);

        // Hover over 'Netosissetulek' field
        WebDriver driver = getWebDriver();
        Actions action = new Actions(driver);
        action.moveToElement(inputFieldElement).perform();

        // Verify the tooltip is visible
        WebElement monthlyIncomeTooltip = ($(By.xpath("//div[contains(@class, 'tooltip fade right in')]")).toWebElement());
        boolean isVisible = TestUtil.isVisibleInViewport(monthlyIncomeTooltip);
        assertTrue(isVisible, "Tooltip for Netosissetulek is missing!");

        closeWindow();
    }

    @Test
    public void TestResultOnSampleMonthlyInstalmentTab ()  {

        // Open url and accept cookies
        open("https://www.lhv.ee/et/liising#kalkulaator");
        $(By.id("acceptPirukas")).click();

        // Change values for clickable fields
        $(By.xpath("//label[@for='account_type-1']")).click();
        $(By.xpath("//label[@for='kas_rent']")).click();
        $(By.xpath("//label[@for='vat_included']")).click();
        $(By.xpath("//select[@name='years']")).click();
        $(By.xpath("//option[@value='48']")).click();
        $(By.xpath("//select[@name='months']")).click();
        $(By.xpath("//option[@value='10']")).click();

        // Create array with field id-s, which will be changed by sending new values
        String[] inputFieldId = new String[]{"price", "initial", "interest_rate", "reminder_percentage"};

        // Create array with new values
        String[] fieldNewValue = new String[]{"42000", "12000", "4.76", "20"};

        // Clear existing values and send new values
        for (int i = 0; i < inputFieldId.length; i++) {
            String fieldId = inputFieldId[i];
            String newValue = fieldNewValue[i];
            $(By.xpath("//input[@id='" + fieldId + "']")).clear();
            $(By.xpath("//input[@id='" + fieldId + "']")).sendKeys(newValue);
        }

        // Verify that the result is as expected
        String expectedResult = "525.17";
        String actualResult = $(By.xpath("//div[(@class='payment')]")).getText();
        assertEquals(actualResult, expectedResult);

        closeWindow();
    }

    @Test
    public void TestNetIncomeTooSmallMessageOnMaxMonthlyInstalmentTab() {

        // Open url, accept cookies
        open("https://www.lhv.ee/et/liising#max-payment");
        $(By.id("acceptPirukas")).click();

        // Insert net income below minimum
        $(By.xpath("//input[@id='monthly-income']")).clear();
        $(By.xpath("//input[@id='monthly-income']")).sendKeys("799.9");

        // Verify that message about too small income is displayed
        assertTrue($(By.xpath("//div[@class='col-xs-12 col-sm-4 col-md-3 col-sm-offset-1 small calculator-error']")).isDisplayed(), "Net income too small message is missing!");

        closeWindow();
    }

    @Test
    public void TestApplyHereOpensExpectedPage() {

        // Open url, accept cookies
        open("https://www.lhv.ee/et/liising#max-payment");
        $(By.id("acceptPirukas")).click();

        // Click on the Apply here button
        $(By.xpath("//a[contains(@class, 'btn btn-dark') and text() = 'Taotle siin']")).click();

        // Assert the url is as expected
        String url = WebDriverRunner.url();
        assertEquals(url, "https://www.lhv.ee/et/liising/taotlus");

        // Assert the title is as expected
        String title = title();
        assertEquals(title, "Liisingutaotlus · LHV");

        closeWindow();
    }

    @Test
    public void TestSwitchingBetweenMonthlyIncomeTabs() {

        // Open url, accept cookies
        open("https://www.lhv.ee/et/liising#kalkulaator");
        $(By.id("acceptPirukas")).click();

        // Navigate to maximum monthly instalment tab
        $(By.xpath("//a[contains(@data-toggle, 'tab') and text() = 'Maksimaalne kuumakse']")).click();

        // Verify that maximum monthly instalment tab is now active
        assertTrue($(By.xpath("//a[contains(@aria-expanded, 'true') and text() = 'Maksimaalne kuumakse']")).isDisplayed(), "Problem in switching tabs!");

        // Navigate to sample monthly instalment tab
        $(By.xpath("//a[contains(@data-toggle, 'tab') and text() = 'Näidiskuumakse']")).click();

        // Verify that maximum monthly instalment tab is not active anymore
        assertTrue($(By.xpath("//a[contains(@aria-expanded, 'false') and text() = 'Maksimaalne kuumakse']")).isDisplayed(), "Problem in switching tabs!");

        closeWindow();
    }
}