package in.precisiontestautomation.drivers;

import in.precisiontestautomation.configurations.BrowserConfig;
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
    private static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();

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
        driver = new ThreadLocal<>();

        switch (browserName) {
            case "chrome":
                chromeSetup();
                break;
        }

        return driver;
    }

    private void chromeSetup() {
        try {
            LoggingPreferences loggingPrefs = new LoggingPreferences();
            loggingPrefs.enable(LogType.BROWSER, Level.WARNING);

            WebDriverManager.chromedriver().setup();
            driver.set(new ChromeDriver(chromeOptions()));
        } catch (Exception e){
           throw new PrecisionTestException("Failed while Setting up or start chrome browser");
        }
    }

    private HashMap chromePrefs(){
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", Integer.parseInt(BrowserConfig.chromeProfileDefaultContent));
        chromePrefs.put("profile.default_content_setting_values.media_stream_camera", Integer.parseInt(BrowserConfig.chromeProfileDefaultContentMedia));
        chromePrefs.put("javascript.enable", Boolean.parseBoolean(BrowserConfig.chromePrefJavaScript));
        chromePrefs.put("credentials_enable_service", Boolean.parseBoolean(BrowserConfig.chromePrefCredentialsEnableService));
        chromePrefs.put("profile.password_manager_enabled", Boolean.parseBoolean(BrowserConfig.chromePrefProfilePassword));
        if(!BrowserConfig.chromePrefDownloadFile.toLowerCase().contains("external")) {
            chromePrefs.put("download.default_directory", checkCreateDirectory(System.getProperty("user.dir") + File.separator + BrowserConfig.chromePrefDownloadFile));
        } else {
            chromePrefs.put("download.default_directory", checkCreateDirectory(BrowserConfig.chromePrefDownloadFile.split("->")[1].trim()));
        }

        return chromePrefs;
    }

    private ChromeOptions chromeOptions(){
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs());

        options.setExperimentalOption("excludeSwitches", new String[]{BrowserConfig.chromeOptionsExcludeSwitches});

        if (Boolean.parseBoolean(BrowserConfig.chromeOptionsNoSandbox)) {
            options.addArguments("--no-sandbox");
        }

        if (Boolean.parseBoolean(BrowserConfig.chromeOptionsDisableDevShmUsage)) {
            options.addArguments("--disable-dev-shm-usage");
        }

        String mockWebCamPath = BrowserConfig.chromeOptionsMockWebCamPath;
        if (mockWebCamPath != null) {
            options.addArguments("--use-fake-ui-for-media-stream",
                    "--use-fake-device-for-media-stream",
                    "--remote-allow-origins=*",
                    "--use-file-for-fake-video-capture=" + System.getProperty("user.dir") + mockWebCamPath);
        }

        if (Boolean.parseBoolean(BrowserConfig.chromeOptionsHeadless)) {
            options.addArguments("--headless", "--window-size=" + BrowserConfig.chromeOptionsWindowSize, "--start-maximized");
        }

        if (Boolean.parseBoolean(BrowserConfig.chromeOptionsStartMaximized)) {
            options.addArguments("--start-maximized");
        } else {
            options.addArguments("--window-size=" + BrowserConfig.chromeOptionsWindowSize);
        }

        return options;
    }

    private String checkCreateDirectory(String directoryPath){
        File directory = new File(directoryPath);
        if(directory.exists()){
            return directoryPath;
        } else {
            if(directory.mkdirs()){
                System.out.println("directoryPath is created " + directoryPath);
            } else {
                System.out.println("fail to create directoryPath '" + directoryPath+"' file will be created in local chrome download location");
            }
            return directoryPath;
        }
    }
}
