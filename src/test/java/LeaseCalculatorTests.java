import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.testng.Assert.*;

public class LeaseCalculatorTests {

    @AfterMethod
    public void closeWindowAfterEachTest(){
        closeWindow();
    }

    @Test
    public void TestDirectLinkOpensAndJumpsToCalculator() throws InterruptedException {
        // Open lease calculator url
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
        if ($(By.id("acceptPirukas")).isDisplayed()) {
            $(By.id("acceptPirukas")).click();
        }

        // Verify that screen has jumped to the calculator
        WebElement calculatorElement = ($(By.id("kalkulaator")).toWebElement());
        boolean isVisible = TestUtil.isVisibleInViewport(calculatorElement);
        assertTrue(isVisible, "Browser did not jump to the calculator section!");
    }

    @Test
    public void TestAllLabelsAreDisplayedOnSampleMonthlyInstalmentTab() {

        // Open url, accept cookies and give browser time to load
        TestUtil.openUrlAndAcceptCookies("https://www.lhv.ee/et/liising#monthly-payment");

        // Create an array with all visible label names
        String[] labelNames = new String[]{"Soovin liisingut", "Liisingu tüüp", "Sõiduki hind", "Sissemakse", "Liisingu periood", "Intress", "Jääkväärtus", "eraisikuna", "juriidilise isikuna", "kapitalirent", "kasutusrent", "Hind sisaldab käibemaksu"};

        // Create an array with corresponding label types
        String[] labelTypes = new String[]{"account_type", "lease_type", "price", "initial_percentage", "period", "interest_rate", "reminder_percentage", "account_type-0", "account_type-1", "kap_rent", "kas_rent", "vat_included"};

        // Verify that all those elements are displayed
        for (int i = 0; i < labelNames.length; i++) {
            String name = labelNames[i];
            String type = labelTypes[i];
            assertTrue($(By.xpath("//label[contains(@for, '" + type + "') and text() = '" + name + "']")).isDisplayed(), "Label '" + name + "' is missing!");
        }
    }

    @Test
    public void TestAllLabelsAreDisplayedOnMaxMonthlyInstalmentTab() {

        // Open url, accept cookies and give browser time to load
        TestUtil.openUrlAndAcceptCookies("https://www.lhv.ee/et/liising#max-payment");

        // Create an array with all visible label names
        String[] labelName = new String[]{"Liisingut soovin taotleda", "Perekonnaseis", "Ülalpeetavate arv", "Netosissetulek", "üksi", "koos kaastaotlejaga", "abielus või vabaabielus"};

        // Create an array with corresponding label types
        String[] labelType = new String[]{"ownership", "marital-status", "dependent-persons", "monthly-income", "ownership-0", "ownership-1", "marital-status-married"};

        // Verify that all those elements are displayed
        for (int i = 0; i < labelName.length; i++) {
            String name = labelName[i];
            String type = labelType[i];
            assertTrue($(By.xpath("//label[contains(@for, '" + type + "') and text() = '" + name + "']")).isDisplayed(), "Label '" + name + "' is missing!");
        }
    }

    @Test
    public void TestDisplayHiddenLabelOnSampleMonthlyInstalmentTab() {

        // Open url, accept cookies and give browser time to load
        TestUtil.openUrlAndAcceptCookies("https://www.lhv.ee/et/liising#monthly-payment");

        // Verify that hidden element's label is not displayed
        String hiddenElement = "Käibemaksu tasumine";
        assertFalse($(By.xpath("//label[contains(@class, 'control-label col-xs-12 col-sm-5') and text() = '" + hiddenElement + "']")).isDisplayed(), "'" + hiddenElement + "' should not be visible!");

        //Select 'juriidilise isikuna'
        $(By.xpath("//label[contains(@for, 'account_type-1')]")).click();

        // Verify that hidden element's label is displayed now
        assertTrue($(By.xpath("//label[contains(@class, 'control-label col-xs-12 col-sm-5') and text() = '" + hiddenElement + "']")).isDisplayed(), "Label '" + hiddenElement + "' is missing!");
    }

    @Test
    public void TestDisplayHiddenLabelsOnMaxMonthlyInstalmentTab() throws InterruptedException {

        // Open url, accept cookies and give browser time to load
        TestUtil.openUrlAndAcceptCookies("https://www.lhv.ee/et/liising#max-payment");


        // Verify that hidden section title and one hidden input field are not displayed
        assertFalse($(By.xpath("//label[contains(@class, 'col-xs-11') and text() = 'Kaastaotleja']")).isDisplayed(), "Section 'Kaastaotleja' should not be visible!");
        assertFalse($(By.xpath("//label[contains(@for, 'guarantor-monthly-income') and text() = 'Netosissetulek']")).isDisplayed(), "Co-applicant field(s) should not be visible!");

        // Select 'koos kaastaotlejaga' and give browser time to load
        $(By.xpath("//label[contains(@for, 'ownership-1')]")).click();
        Thread.sleep(1000);

        // Verify that section titles are displayed now
        String[] sectionTitle = new String[]{"Põhitaotleja", "Kaastaotleja"};

        for (String title : sectionTitle) {
            assertTrue($(By.xpath("//label[contains(@class, 'col-xs-11') and text() = '" + title + "']")).isDisplayed(), "Label '" + title + "' is missing!");
        }

        // Verify that hidden input fields are displayed now
        String[] labelName = new String[]{"Perekonnaseis", "Ülalpeetavate arv", "Netosissetulek", "Perekonnaseis", "Ülalpeetavate arv", "Netosissetulek", "abielus või vabaabielus"};
        String[] labelType = new String[]{"marital-status", "dependent-persons", "monthly-income", "guarantor-marital-status", "guarantor-dependent-persons", "guarantor-monthly-income", "guarantor-marital-status-married"};

        for (int i = 0; i < labelName.length; i++) {
            String name = labelName[i];
            String type = labelType[i];
            assertTrue($(By.xpath("//label[contains(@for, '" + type + "') and text() = '" + name + "']")).isDisplayed(), "Label '" + name + "' is missing!");
        }
    }

    @Test
    public void TestCalculatorTitleAndDisclaimer() {

        // Open url, accept cookies and give browser time to load
        TestUtil.openUrlAndAcceptCookies("https://www.lhv.ee/et/liising#kalkulaator");

        // Verify that calculator title is displayed
        String firstPart = "Arvuta";
        String secondPart = "kuumakse";
        assertTrue($(By.xpath("//h2[contains(text(), '" + firstPart + "')]")).isDisplayed(), "Problem with lease calculator title!");
        assertTrue($(By.xpath("//strong[contains(text(), '" + secondPart + "')]")).isDisplayed(), "Problem with lease calculator title!");

        // Verify that disclaimer is displayed
        String disclaimerText = "Tulemus on ligikaudne ja võib erineda sulle pakutavatest tingimustest";
        assertTrue($(By.xpath("//p[contains(text(), '" + disclaimerText + "')]")).isDisplayed(), "Problem with lease calculator disclaimer!");
    }

    @Test
    public void TestDefaultValuesOnBothTabs() {

        // Open url, accept cookies and give browser time to load
        TestUtil.openUrlAndAcceptCookies("https://www.lhv.ee/et/liising#max-payment");

        // Verify that all checkbox and radio button default values are as expected
        String[] fieldIds = new String[]{"account_type-0", "kap_rent", "vat_included", "ownership-0", "marital-status-married"};
        for (String id : fieldIds) {
            assertTrue($(By.xpath("//input[@id='" + id + "']")).isSelected(), id + " checkbox/radio button is not selected by default!");
        }

        // Verify that all default dropdown values are as expected
        String[] dropdownNames = new String[]{"years", "months", "dependent-persons"};
        String[] expectedDropdownValues = new String[]{"72", "0", "1"};

        for (int i = 0; i < dropdownNames.length; i++) {
            String actualDropdownValue = $(By.xpath("//select[(@Name='" + dropdownNames[i] + "')]")).getValue();
            assertEquals(actualDropdownValue, expectedDropdownValues[i]);
        }

        // Verify that all default input values are as expected
        String[] inputFieldIds = new String[]{"price", "initial_percentage", "initial", "interest_rate", "reminder_percentage", "reminder", "monthly-income"};
        String[] expectedInputValues = new String[]{"15 000", "10", "1500", "4", "10", "1500", "900"};

        for (int i = 0; i < inputFieldIds.length; i++) {
            String actualInputValue = $(By.xpath("//input[(@id='" + inputFieldIds[i] + "')]")).getValue();
            assertEquals(actualInputValue, expectedInputValues[i]);
        }
    }

    @Test
    public void TestMonthlyIncomeTooltip() throws InterruptedException {

        // Open url, accept cookies and give browser time to load
        TestUtil.openUrlAndAcceptCookies("https://www.lhv.ee/et/liising#max-payment");

        // Get field with tooltip and convert to WebElement
        WebElement inputFieldElement = ($(By.xpath("//input[contains(@name, 'monthly-income')]")).toWebElement());

        // Make screen jump to the calculator and give time to load
        $(By.xpath("//a[contains(@href, '#kalkulaator')]")).click();
        Thread.sleep(1000);

        // Hover over 'Netosissetulek' field
        WebDriver driver = getWebDriver();
        Actions action = new Actions(driver);
        action.moveToElement(inputFieldElement).perform();

        // Verify the tooltip has become visible
        WebElement monthlyIncomeTooltip = ($(By.xpath("//div[contains(@class, 'tooltip fade right in')]")).toWebElement());
        boolean isVisible = TestUtil.isVisibleInViewport(monthlyIncomeTooltip);
        assertTrue(isVisible, "Tooltip for Netosissetulek is missing!");
    }

    @Test
    public void TestFinalResultOnSampleMonthlyInstalmentTab() {

        // Open url, accept cookies and give browser time to load
        TestUtil.openUrlAndAcceptCookies("https://www.lhv.ee/et/liising#monthly-payment");

        // Change values for all fields
        // Select 'juriidilise isikuna' radio button
        $(By.xpath("//label[@for='account_type-1']")).click();
        // Select 'kasutusrent' radiobutton
        $(By.xpath("//label[@for='kas_rent']")).click();
        // Remove checkmark from VAT checkbox
        $(By.xpath("//label[@for='vat_included']")).click();
        // Click on years dropdown, then select value 48 (=4 years)
        $(By.xpath("//select[@name='years']")).click();
        $(By.xpath("//option[@value='48']")).click();
        // Click on months dropdown, then select value 10
        $(By.xpath("//select[@name='months']")).click();
        $(By.xpath("//option[@value='10']")).click();

        // For fields, which need new input values, create an array with field id-s
        String[] inputFieldId = new String[]{"price", "initial", "interest_rate", "reminder_percentage"};

        // Create array with new values for those fields
        String[] fieldNewValue = new String[]{"42000", "12000", "4.76", "20"};

        // Clear existing values and send new values
        for (int i = 0; i < inputFieldId.length; i++) {
            String fieldId = inputFieldId[i];
            String newValue = fieldNewValue[i];
            $(By.xpath("//input[@id='" + fieldId + "']")).clear();
            $(By.xpath("//input[@id='" + fieldId + "']")).sendKeys(newValue);
        }

        // Verify that the final result (monthly instalment) is as expected
        String expectedResult = "525.17";
        String actualResult = $(By.xpath("//div[(@class='payment')]")).getText();
        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void TestNetIncomeTooSmallMessageOnMaxMonthlyInstalmentTab() {

        // Open url, accept cookies and give browser time to load
        TestUtil.openUrlAndAcceptCookies("https://www.lhv.ee/et/liising#max-payment");

        // Clear net income field and insert value below minimum
        $(By.xpath("//input[@id='monthly-income']")).clear();
        $(By.xpath("//input[@id='monthly-income']")).sendKeys("799.9");

        // Verify that message about too small income is displayed
        assertTrue($(By.xpath("//div[@class='col-xs-12 col-sm-4 col-md-3 col-sm-offset-1 small calculator-error']")).isDisplayed(), "Net income too small message is missing!");
    }

    @Test
    public void TestApplyHereOpensExpectedPage() {

        // Open url, accept cookies and give browser time to load
        TestUtil.openUrlAndAcceptCookies("https://www.lhv.ee/et/liising#max-payment");

        // Click on the 'Taotle siin' button
        $(By.xpath("//a[contains(@class, 'btn btn-dark') and text() = 'Taotle siin']")).click();

        // Verify url
        String url = WebDriverRunner.url();
        assertEquals(url, "https://www.lhv.ee/et/liising/taotlus");

        // Verify page title
        String title = title();
        assertEquals(title, "Liisingutaotlus · LHV");
    }

    @Test
    public void TestSwitchingBetweenMonthlyIncomeTabs() {

        // Open url, accept cookies and give browser time to load
        TestUtil.openUrlAndAcceptCookies("https://www.lhv.ee/et/liising#monthly-payment");

        // Navigate to maximum monthly instalment tab
        $(By.xpath("//a[contains(@data-toggle, 'tab') and text() = 'Maksimaalne kuumakse']")).click();

        // Verify that maximum monthly instalment tab is now active
        assertTrue($(By.xpath("//a[contains(@aria-expanded, 'true') and text() = 'Maksimaalne kuumakse']")).isDisplayed(), "Problem in switching tabs!");

        // Navigate to sample monthly instalment tab
        $(By.xpath("//a[contains(@data-toggle, 'tab') and text() = 'Näidiskuumakse']")).click();

        // Verify that maximum monthly instalment tab is not active anymore
        assertTrue($(By.xpath("//a[contains(@aria-expanded, 'false') and text() = 'Maksimaalne kuumakse']")).isDisplayed(), "Problem in switching tabs!");
    }
}