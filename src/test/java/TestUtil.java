import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import static com.codeborne.selenide.Selenide.*;

public class TestUtil {

    // https://stackoverflow.com/questions/45243992/verification-of-element-in-viewport-in-selenium
    public static Boolean isVisibleInViewport(WebElement element) {
        WebDriver driver = ((RemoteWebElement)element).getWrappedDriver();

        return (Boolean)((JavascriptExecutor)driver).executeScript(
                "var elem = arguments[0],                 " +
                        "  box = elem.getBoundingClientRect(),    " +
                        "  cx = box.left + box.width / 2,         " +
                        "  cy = box.top + box.height / 2,         " +
                        "  e = document.elementFromPoint(cx, cy); " +
                        "for (; e; e = e.parentElement) {         " +
                        "  if (e === elem)                        " +
                        "    return true;                         " +
                        "}                                        " +
                        "return false;                            "
                , element);
    }

    public static void openUrlAndAcceptCookies(String url) {
        try{
            open(url);
            if ($(By.id("acceptPirukas")).isDisplayed()) {
                $(By.id("acceptPirukas")).click();
            }
            Thread.sleep(2000);
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
