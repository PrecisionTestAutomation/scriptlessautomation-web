package in.precisiontestautomation.drivers;

import in.precisiontestautomation.utils.WebKeyInitializers;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Objects;

/**
 * <p>DriverManager class.</p>
 *
 * @author PTA-dev
 * @version 1.0
 * @since 2024-05-20
 */
public class DriverManager {

    private DriverManager(){
    }

    // Initialize ThreadLocal of RemoteWebDriver at declaration


    /**
     * <p>initializeDriver.</p>
     *
     * @param platform a {@link java.lang.String} object
     * @param browser a {@link java.lang.String} object
     */
    public static void initializeDriver(String platform,String browser) {

        DriverTypes driverType = DriverTypes.valueOf(platform.toUpperCase());

        switch (driverType) {
            case ANDROID -> {
                //local android setup
            }
            case IOS -> {
                //local IOS setup
            }
            case WEB -> {
                // set the ThreadLocal instance to the thread local variable
                WebKeyInitializers.getDriver().set(WebDriverClass.getInstance().setDriver(browser).get());
                WebKeyInitializers.getGetMainWindow().set(WebKeyInitializers.getDriver().get().getWindowHandle());
            }
            case CLOUD -> {
                //cloud configuration
            }
        }
    }

    /**
     * <p>getDriver.</p>
     *
     * @return a {@link org.openqa.selenium.remote.RemoteWebDriver} object
     */
    public static RemoteWebDriver getDriver(){
        return WebKeyInitializers.getDriver().get();
    }

    /**
     * <p>quit.</p>
     */
    public static void quit(){
        if(!Objects.isNull(WebKeyInitializers.getDriver().get())){
            if(!Objects.isNull(WebKeyInitializers.getDriver().get().getSessionId())){
                WebKeyInitializers.getDriver().get().close();
                WebKeyInitializers.getDriver().get().quit();
                // remove the driver from threadLocal instance to prevent memory leaks
                WebKeyInitializers.getDriver().remove();
            }
        }
    }

}

