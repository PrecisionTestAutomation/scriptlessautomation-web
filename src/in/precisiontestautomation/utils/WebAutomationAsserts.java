package in.precisiontestautomation.utils;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import in.precisiontestautomation.scriptlessautomation.core.configurations.ExtentReportConfig;
import in.precisiontestautomation.scriptlessautomation.core.utils.AutomationAsserts;
import in.precisiontestautomation.scriptlessautomation.core.utils.CoreKeyInitializers;
import org.openqa.selenium.WebElement;

import java.util.Objects;

/**
 * @author PTA-dev
 * @version 1.0
 * @since 2024-05-20
 */
public class WebAutomationAsserts {
    private final AutomationAsserts automationAssert;
    private boolean captureScreenshotOnPass = Boolean.parseBoolean(ExtentReportConfig.Report_captureScreenshotOnPass);

    private final static ThreadLocal<WebAutomationAsserts> THREAD_LOCAL_INSTANCE = ThreadLocal.withInitial(WebAutomationAsserts::new);

    private WebAutomationAsserts() {
        this.automationAssert = CoreKeyInitializers.getCustomSoftAssert().get();
    }

    public static WebAutomationAsserts getInstance(){
        return THREAD_LOCAL_INSTANCE.get();
    }

    public void info(String message){
        automationAssert.info(message);
    }
    public void assertEquals(WebElement element, String elementName, String actual, String expected) {
        boolean stringsMatch = actual.replaceAll("\n", " ").equals(expected);
        automationAssert.test.log(stringsMatch ? Status.PASS : Status.FAIL,
                stringsMatch ? elementName + " -> Actual:<b><i>" + actual + "</i></b> match with Expected:<b><i>" + expected + "</i></b>" : elementName + " -> Actual:<i><b>" + actual + "</i></b> doesn't match with Expected:<i><b>" + expected + "</i></b>",
                shouldCaptureScreenshot(stringsMatch) ? MediaEntityBuilder.createScreenCaptureFromBase64String(Objects.requireNonNull(WebFrameworkActions.takeScreenShotInBase64(element))).build() : null);
    }

    public void assertTrue(WebElement element,String elementName, boolean trueCondition, String successMessage, String failureMessage) {
        automationAssert.test.log(trueCondition ? Status.PASS : Status.FAIL,
                trueCondition ? elementName + " -> " + successMessage : elementName + " -> " + failureMessage,
                shouldCaptureScreenshot(trueCondition) ? MediaEntityBuilder.createScreenCaptureFromBase64String(Objects.requireNonNull(WebFrameworkActions.takeScreenShotInBase64(element))).build() : null);
    }

    public void assertNotEquals(WebElement element,String elementName,String actual, String expected) {
        boolean stringsMatch = !actual.replaceAll("\n", " ").equals(expected);
        automationAssert.test.log(stringsMatch ? Status.PASS : Status.FAIL,
                stringsMatch ? elementName + " -> Actual:<b><i>" + actual + "</i></b> match with Expected:<b><i>" + expected + "</i></b>" : elementName + " -> Actual:<i><b>" + actual + "</i></b> doesn't match with Expected:<i><b>" + expected + "</i></b>",
                shouldCaptureScreenshot(stringsMatch) ? MediaEntityBuilder.createScreenCaptureFromBase64String(Objects.requireNonNull(WebFrameworkActions.takeScreenShotInBase64(element))).build() : null);
    }

    public void assertEqualsIgnore(WebElement element,String elementName,String actual, String expected) {
        boolean stringsMatch = actual.replaceAll("\n", " ").equalsIgnoreCase(expected);
        automationAssert.test.log(stringsMatch ? Status.PASS : Status.FAIL,
                stringsMatch ? elementName + " -> Actual:<b><i>" + actual + "</i></b> match with Expected:<b><i>" + expected + "</i></b>" : elementName + " -> Actual:<i><b>" + actual + "</i></b> doesn't match with Expected:<i><b>" + expected + "</i></b>",
                shouldCaptureScreenshot(stringsMatch) ? MediaEntityBuilder.createScreenCaptureFromBase64String(Objects.requireNonNull(WebFrameworkActions.takeScreenShotInBase64(element))).build() : null);
    }

    public void assertContains(WebElement element,String elementName, String actual, String expected) {
        boolean stringsMatch = actual.replaceAll("\n", " ").contains(expected);
        automationAssert.test.log(stringsMatch ? Status.PASS : Status.FAIL,
                stringsMatch ? elementName + " -> Actual:<b><i>" + actual + "</i></b> contains with Expected:<b><i>" + expected + "</i></b>" : elementName + " -> Actual:<i><b>" + actual + "</i></b> doesn't contains with Expected:<i><b>" + expected + "</i></b>",
                shouldCaptureScreenshot(stringsMatch) ? MediaEntityBuilder.createScreenCaptureFromBase64String(Objects.requireNonNull(WebFrameworkActions.takeScreenShotInBase64(element))).build() : null);
    }

    private boolean shouldCaptureScreenshot(boolean result) {
        return (!result || (result && captureScreenshotOnPass));
    }

}
