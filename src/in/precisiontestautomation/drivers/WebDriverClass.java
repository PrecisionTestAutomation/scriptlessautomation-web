package in.precisiontestautomation.drivers;

import in.precisiontestautomation.drivers.browsers.Chrome;
import org.openqa.selenium.remote.RemoteWebDriver;

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
