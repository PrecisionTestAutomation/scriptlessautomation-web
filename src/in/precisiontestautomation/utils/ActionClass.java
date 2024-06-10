package in.precisiontestautomation.utils;

import com.aventstack.extentreports.Status;
import in.precisiontestautomation.drivers.DriverManager;
import in.precisiontestautomation.elementgenerator.GenerateWebElement;
import in.precisiontestautomation.enums.ActionTypes;
import in.precisiontestautomation.scriptlessautomation.core.exceptionhandling.PrecisionTestException;
import in.precisiontestautomation.scriptlessautomation.core.reports.ReportManagerRunner;
import in.precisiontestautomation.scriptlessautomation.core.utils.CoreKeyInitializers;
import in.precisiontestautomation.tests.API;
import in.precisiontestautomation.tests.WEB;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static in.precisiontestautomation.enums.ActionTypes.*;

/***
 * Actions class is web Action class to perform actions on the web
 * according to the user requirement
 * @author PTA-dev
 * @version 1.0
 * @since 2024-05-20
 */
public class ActionClass {

    public static Map<ActionTypes, Supplier<WebElement>> actionsToBePerformed(WebElement element, PerformActions performActions,boolean captureScreenshotOnPass) {
        return switch (ActionTypes.valueOf(performActions.getActions())) {
            case CLICK -> Map.of(ActionTypes.CLICK, () -> {
                if (element != null) {
                    element.click();
                }
                return element;
            });
            case SEND_KEYS -> Map.of(ActionTypes.SEND_KEYS, () -> {
                if (element != null) {
                    element.clear();
                    element.sendKeys(WebFrameworkActions.fetchPreFlow(performActions.getSendKeys()));
                }
                return element;
            });
            case WAIT_INVISIBILITY -> Map.of(ActionTypes.WAIT_INVISIBILITY, () -> {
                long waitSeconds;
                try {
                    waitSeconds = performActions.getSendKeys().length() > 1 ? Long.parseLong(performActions.getSendKeys()) : 10;
                } catch (NumberFormatException e) {
                    waitSeconds = 10; // Default value if parsing fails
                }

                new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(waitSeconds))
                        .until(ExpectedConditions.invisibilityOfElementLocated(
                                GenerateWebElement.getInstance(performActions).get().extractLocator()));

                return element;
            });
            case CREATE_NEW_TAB -> Map.of(CREATE_NEW_TAB, () -> {
                DriverManager.getDriver().switchTo().newWindow(WindowType.TAB);
                return null;
            });
            case LOAD_URL -> Map.of(LOAD_URL, () -> {
                DriverManager.getDriver().get(WebFrameworkActions.fetchPreFlow(performActions.getSendKeys()));
                return null;
            });
            case GENERATE_RANDOM_EMAIL_ADDRESS -> Map.of(GENERATE_RANDOM_EMAIL_ADDRESS, () -> {
                WebFrameworkActions.invokeClassMethods("MOCK", "generateRandomEmailAddress");
                return null;
            });
            case FETCH_RANDOM_EMAIL_BODY -> Map.of(FETCH_RANDOM_EMAIL_BODY, () -> {
                WebFrameworkActions.invokeClassMethods("MOCK", "getRandomEmailBody", performActions.getSendKeys());
                return null;
            });
            case DEPENDANT_TEST_CASE -> Map.of(DEPENDANT_TEST_CASE, () -> {
                WEB.getInstance().testRunner(WebFrameworkActions.getFileWithStartName(performActions.getSendKeys()), false);
                return null;
            });
            case KEYBOARD_DOWN_ARROW ->
                    Map.of(KEYBOARD_DOWN_ARROW, () -> WebFrameworkActions.clickDownArrayOnElement(element));
            case KEYBOARD_TAB -> Map.of(KEYBOARD_TAB, () -> WebFrameworkActions.clickTabOnElement(element));
            case SWITCH_FRAME_WEB_ELEMENT -> Map.of(SWITCH_FRAME_WEB_ELEMENT, () -> {
                performActions.setPageName(performActions.getSendKeys().split(":")[0]);
                performActions.setElementName(performActions.getSendKeys().split(":")[1]);
                DriverManager.getDriver().switchTo().frame(GenerateWebElement.getInstance(performActions).get().getElement());
                return element;
            });
            case SWITCH_FRAME_PARENT -> Map.of(SWITCH_FRAME_PARENT, () -> {
                DriverManager.getDriver().switchTo().parentFrame();
                return element;
            });
            case DRIVER_QUIT -> Map.of(DRIVER_QUIT, () -> {
                DriverManager.getDriver().quit();
                return null;
            });
            case CHROME_BROWSER -> Map.of(CHROME_BROWSER, () -> {
                DriverManager.initializeDriver("web", "chrome");
                return null;
            });
            case HARD_WAIT -> Map.of(HARD_WAIT, () -> {
                try {
                    Thread.sleep(Integer.parseInt(performActions.getSendKeys()) * 1000L);
                } catch (Exception ignored) {
                }
                return null;
            });
            case API_EXECUTOR -> Map.of(API_EXECUTOR, () -> {
                try {
                    API.getInstance().testRunner(WebFrameworkActions.getFileWithStartName(performActions.getSendKeys()), false);
                } catch (Exception e) {
                    throw new PrecisionTestException("Fail while executing API test case | " + " | " + performActions.getSendKeys() + " | " + e.getLocalizedMessage());
                }
                return null;
            });
            case REFRESH -> Map.of(REFRESH, () -> {
                try {
                    DriverManager.getDriver().navigate().refresh();
                } catch (Exception e) {
                    throw new PrecisionTestException("Fail during refreshing the browser");
                }
                return null;
            });
            case CUSTOM -> Map.of(CUSTOM, () -> {
                String[] customClassMethod = performActions.getSendKeys().split(":");
                WebElement object = WebFrameworkActions.invokeCustomClassMethods(customClassMethod[0], customClassMethod[1]);
                return Objects.isNull(object) ? element : object;
            });
            case EXPLICIT_WAIT_TEXT_PRESENT -> Map.of(EXPLICIT_WAIT_TEXT_PRESENT, () -> {
                long waitSeconds;
                String text = null;
                try {
                    String[] splitValue = performActions.getSendKeys().split("&&");
                    waitSeconds = splitValue.length > 1 ? Long.parseLong(splitValue[1]) : 10;
                    text = splitValue[0];
                } catch (NumberFormatException e) {
                    waitSeconds = 10;
                }

                boolean isTextPresent = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(waitSeconds))
                        .until(ExpectedConditions.textToBePresentInElement(element, WebFrameworkActions.integrateString(text)));
                String imageBase64 = WebFrameworkActions.takeScreenShotInBase64(element);
                CoreKeyInitializers.getCustomSoftAssert().get().assertTrue(performActions.getElementName(), isTextPresent , "Element consist expected text","Element doesn't consist expected text",captureScreenshotOnPass,imageBase64);

                return element;
            });
            case SAVE -> Map.of(SAVE, () -> {
                String elementText = element.getText().trim();
                WebKeyInitializers.getGlobalVariables().get().put(performActions.getSendKeys(), elementText);
                return null;
            });
            case CLEAR -> Map.of(CLEAR, () -> {
                if (Objects.isNull(element)) {
                    String elementValue = element.getAttribute("value").trim();
                    for (int i = 0; i < elementValue.length(); i++) {
                        element.sendKeys(Keys.BACK_SPACE);
                    }
                }
                return null;
            });
            case SELECT_BY_VALUE -> Map.of(SELECT_BY_VALUE, () -> {
                WebFrameworkActions.selectByValue(element, performActions.getSendKeys());
                return element;
            });
            case SELECT_BY_INDEX -> Map.of(SELECT_BY_INDEX, () -> {
                WebFrameworkActions.selectByIndex(element, performActions.getSendKeys());
                return element;
            });
            case SELECT_BY_TEXT -> Map.of(SELECT_BY_TEXT, () -> {
                WebFrameworkActions.selectByText(element, performActions.getSendKeys());
                return element;
            });
            case DESELECT_BY_VALUE -> Map.of(DESELECT_BY_VALUE, () -> {
                WebFrameworkActions.deSelectByValue(element, performActions.getSendKeys());
                return element;
            });
            case DESELECT_BY_INDEX -> Map.of(DESELECT_BY_INDEX, () -> {
                WebFrameworkActions.deSelectByIndex(element, performActions.getSendKeys());
                return element;
            });
            case DESELECT_BY_TEXT -> Map.of(DESELECT_BY_TEXT, () -> {
                WebFrameworkActions.deSelectByText(element, performActions.getSendKeys());
                return element;
            });
            case CLICK_BY_JS -> Map.of(CLICK, () -> {
                if (element != null) {
                    WebFrameworkActions.clickByJavaScript(element);
                }
                return element;
            });
            case DOUBLE_CLICK -> Map.of(DOUBLE_CLICK,() ->{
                if (element != null) {
                    WebFrameworkActions.doubleClick(element);
                }
                return element;
            });
            case CLEAR_SEND_KEYS -> Map.of(CLEAR_SEND_KEYS,()->{
                if(element != null){
                    WebFrameworkActions.clearAndSendTextElementField(element, performActions.getSendKeys());
                }
                return element;
            });
            default -> Map.of(NONE, () -> {
                ReportManagerRunner.getTest().log(Status.INFO, performActions.getElementName() + " has no action to perform");
                return element;
            });
        };
    }
}
