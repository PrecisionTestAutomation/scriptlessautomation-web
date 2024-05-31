package in.precisiontestautomation.drivers;

import in.precisiontestautomation.configurations.BrowserConfig;
import in.precisiontestautomation.drivers.browsers.Chrome;
import in.precisiontestautomation.scriptlessautomation.core.exceptionhandling.PrecisionTestException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * <p>WebDriverClass class.</p>
 *
 * @author PTA-dev
 * @version 1.0
 * @since 2024-05-20
 */
public class WebDriverClass {
    private static class Holder {
        static final WebDriverClass INSTANCE = new WebDriverClass();
    }

    /**
     * <p>getInstance.</p>
     *
     * @return a {@link in.precisiontestautomation.drivers.WebDriverClass} object
     */
    public static WebDriverClass getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * <p>Setter for the field <code>driver</code>.</p>
     *
     * @param browserName a {@link java.lang.String} object
     * @return a {@link java.lang.ThreadLocal} object
     */
    public ThreadLocal<RemoteWebDriver> setDriver(String browserName) {

        switch (browserName) {
            case "chrome":
                return new Chrome().chromeSetup();
        }
        return null;
    }
}
