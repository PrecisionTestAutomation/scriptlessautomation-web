package in.precisiontestautomation.utils;

import in.precisiontestautomation.drivers.DriverManager;
import in.precisiontestautomation.enums.ScrollTypes;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.Map;
import java.util.function.Consumer;


public class ScrollUtils {

    private ScrollUtils() {
    }

    public static ScrollUtils getInstance() {
        return new ScrollUtils();
    }

    public WebElement scrollAction(WebElement element, String scrollType) {
        var jsExecutor = (JavascriptExecutor) DriverManager.getDriver();
        Map<ScrollTypes, Consumer<JavascriptExecutor>> actions = Map.of(
                ScrollTypes.SCROLL_TO_ELEMENT_CENTER, e -> e.executeScript("arguments[0].scrollIntoView({block: 'center'})", element),
                ScrollTypes.SCROLL_TO_BOTTOM, e -> e.executeScript("window.scrollTo(0,document.body.scrollHeight)"),
                ScrollTypes.SCROLL_TO_TOP, e -> e.executeScript("arguments[0].scrollIntoView();", element),
                ScrollTypes.NONE, e -> e.executeScript("","")
        );
        actions.getOrDefault(ScrollTypes.valueOf(scrollType.toUpperCase()), e -> {
        }).accept(jsExecutor);

        return element;
    }
}
