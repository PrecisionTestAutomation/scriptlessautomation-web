package in.precisiontestautomation.elementgenerator;


import in.precisiontestautomation.drivers.DriverManager;
import in.precisiontestautomation.enums.LocatorsTypes;
import in.precisiontestautomation.enums.WaitTypes;
import in.precisiontestautomation.scriptlessautomation.core.exceptionhandling.PrecisionTestException;
import in.precisiontestautomation.utils.PerformActions;
import in.precisiontestautomation.utils.ScrollUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class GenerateWebElement {

    private final PerformActions performActions;

    private GenerateWebElement(PerformActions performActions) {
        this.performActions = performActions;
    }

    public static ThreadLocal<GenerateWebElement> getInstance(PerformActions performActions) {
        return ThreadLocal.withInitial(() -> new GenerateWebElement(performActions));
    }

    public WebElement getElement() {
        try {
            if (!performActions.getElementName().equalsIgnoreCase("none")) {
                return scrollWaitGetElement(DriverManager.getDriver(), extractLocator(), performActions.getWaitType());
            } else {
                return null;
            }
        } catch (TimeoutException e) {
            throw PrecisionTestException.elementNotVisible(performActions.getElementName(), e);
        } catch (NullPointerException e) {
            throw PrecisionTestException.invalidElementName(performActions.getElementName(), performActions.getPageName(), e);
        }
    }

    public By extractLocator() {
        String[] locatorValues = GetLocators.getLocatorValue(performActions.getPageName(), performActions.getElementName()).split("->");
        String locatorType = locatorValues[0].trim();
        String value = ignoringExc(() -> locatorValues[1].trim());

        return switch (LocatorsTypes.valueOf(locatorType.toUpperCase())) {
            case ID -> By.id(value);
            case NAME -> By.name(value);
            case CLASS_NAME -> By.className(value);
            case TAG_NAME -> By.tagName(value);
            case LINK_TEXT -> By.linkText(value);
            case PARTIAL_LINK_TEXT -> By.partialLinkText(value);
            case XPATH -> By.xpath(generateReferenceLocator(value));
            case CSS -> By.cssSelector(generateReferenceLocator(value));
            case NONE -> null;
        };
    }

    private WebElement scrollWaitGetElement(WebDriver driver, By by, String waitType) {
        String[] waitArr = waitType.split(":");
        WebDriverWait wait = new WebDriverWait(driver, waitArr.length > 1 ? Duration.ofSeconds(Integer.parseInt(waitArr[1])) : Duration.ofSeconds(10));

        if(Objects.isNull(by)){
            waitArr[0] = "NONE";
        }

        var actions = Map.<WaitTypes, Supplier<WebElement>>of(
                WaitTypes.VISIBILITY, () -> {
                    ScrollUtils.getInstance().scrollAction(wait.until(ExpectedConditions.presenceOfElementLocated(by)), performActions.getScrollType());
                    return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                },
                WaitTypes.PRESENCE_OF_ELEMENT_ON_DOM, () -> ScrollUtils.getInstance().scrollAction(wait.until(ExpectedConditions.presenceOfElementLocated(by)), performActions.getScrollType()),
                WaitTypes.PRESENCE_OF_TEXT_GREATER_THAN_ONE, () -> {
                    WebElement finalElement = ScrollUtils.getInstance().scrollAction(wait.until(ExpectedConditions.presenceOfElementLocated(by)), performActions.getScrollType());
                    wait.until((ExpectedCondition<Boolean>) d -> (finalElement.getText().length() > 1));
                    return finalElement;
                },
                WaitTypes.PRESENCE_OF_ATTRIBUTE_VALUE_GREATER_THAN_ONE, () -> {
                    WebElement finalElement = ScrollUtils.getInstance().scrollAction(wait.until(ExpectedConditions.presenceOfElementLocated(by)), performActions.getScrollType());
                    wait.until((ExpectedCondition<Boolean>) d -> (finalElement.getAttribute("value").length() > 1));
                    return finalElement;
                },
                WaitTypes.ELEMENT_TO_BE_CLICKABLE, () -> ScrollUtils.getInstance().scrollAction(wait.until(ExpectedConditions.elementToBeClickable(by)), performActions.getScrollType()),
                WaitTypes.NONE, () -> ScrollUtils.getInstance().scrollAction(DriverManager.getDriver().findElement(by),performActions.getScrollType())
        );

        return actions.getOrDefault(WaitTypes.valueOf(waitArr[0].toUpperCase()), () -> DriverManager.getDriver().findElement(by)).get();
    }

    private String generateReferenceLocator(String value) {
        String[] valueArr = value.split("=>");
        if (valueArr.length > 1) {
            String[] locatorValues = GetLocators.getLocatorValue(performActions.getPageName(), valueArr[0].trim()).split("->");
            return locatorValues[1].trim() + valueArr[1];
        }
        return value;
    }

    public static String ignoringExc(CallableExc r) {
        try {
            return r.call();
        } catch (Exception e) {
            // Log or handle the exception in some way here
            return "NONE"; // Or return a default value
        }
    }

    @FunctionalInterface
    public interface CallableExc {
        String call() throws Exception;
    }
}
