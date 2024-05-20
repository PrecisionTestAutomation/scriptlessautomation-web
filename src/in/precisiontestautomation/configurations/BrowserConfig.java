package in.precisiontestautomation.configurations;

import in.precisiontestautomation.scriptlessautomation.core.configurations.ConfigReader;
import in.precisiontestautomation.utils.WebFrameworkActions;

import java.io.File;
import java.util.Objects;

/**
 * <p>BrowserConfig class.</p>
 *
 * @author PTA-dev
 * @version 1.0
 * @since 2024-05-20
 */
public class BrowserConfig {
    private static final String CONFIGURATION_FILE_NAME = "browserConfiguration"+ File.separator+"chrome.properties";

    /** Constant <code>chromeProfileDefaultContent="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromeProfileDefaultContent = Objects.isNull(WebFrameworkActions.getProperty("chrome.prefs.profile.default_content_settings.popups")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.prefs.profile.default_content_settings.popups") : WebFrameworkActions.getProperty("chrome.prefs.profile.default_content_settings.popups");
    /** Constant <code>chromeProfileDefaultContentMedia="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromeProfileDefaultContentMedia = Objects.isNull(WebFrameworkActions.getProperty("chrome.prefs.profile.default_content_setting_values.media_stream_camera")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.prefs.profile.default_content_setting_values.media_stream_camera") : WebFrameworkActions.getProperty("chrome.prefs.profile.default_content_setting_values.media_stream_camera");
    /** Constant <code>chromePrefJavaScript="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromePrefJavaScript = Objects.isNull(WebFrameworkActions.getProperty("chrome.prefs.javascript.enable")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.prefs.javascript.enable") : WebFrameworkActions.getProperty("chrome.prefs.javascript.enable");
    /** Constant <code>chromePrefCredentialsEnableService="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromePrefCredentialsEnableService = Objects.isNull(WebFrameworkActions.getProperty("chrome.prefs.credentials_enable_service")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.prefs.credentials_enable_service") : WebFrameworkActions.getProperty("chrome.prefs.credentials_enable_service");
    /** Constant <code>chromePrefProfilePassword="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromePrefProfilePassword = Objects.isNull(WebFrameworkActions.getProperty("chrome.prefs.profile.password_manager_enabled")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.prefs.profile.password_manager_enabled") : WebFrameworkActions.getProperty("chrome.prefs.profile.password_manager_enabled");
    /** Constant <code>chromePrefDownloadFile="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromePrefDownloadFile = Objects.isNull(WebFrameworkActions.getProperty("chrome.prefs.profile.downloadPath")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.prefs.profile.downloadPath") : WebFrameworkActions.getProperty("chrome.prefs.profile.downloadPath");
    /** Constant <code>chromeOptionsExcludeSwitches="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromeOptionsExcludeSwitches = Objects.isNull(WebFrameworkActions.getProperty("chrome.options.excludeSwitches")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.options.excludeSwitches") : WebFrameworkActions.getProperty("chrome.options.excludeSwitches");
    /** Constant <code>chromeOptionsNoSandbox="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromeOptionsNoSandbox = Objects.isNull(WebFrameworkActions.getProperty("chrome.options.no-sandbox")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.options.no-sandbox") : WebFrameworkActions.getProperty("chrome.options.no-sandbox");

    /** Constant <code>chromeOptionsDisableDevShmUsage="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromeOptionsDisableDevShmUsage = Objects.isNull(WebFrameworkActions.getProperty("chrome.options.disable-dev-shm-usage")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.options.disable-dev-shm-usage") : WebFrameworkActions.getProperty("chrome.options.disable-dev-shm-usage");

    /** Constant <code>chromeOptionsHeadless="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromeOptionsHeadless = Objects.isNull(WebFrameworkActions.getProperty("chrome.options.headless")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.options.headless") : WebFrameworkActions.getProperty("chrome.options.headless");

    /** Constant <code>chromeOptionsWindowSize="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromeOptionsWindowSize = Objects.isNull(WebFrameworkActions.getProperty("chrome.options.window-size")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.options.window-size") : WebFrameworkActions.getProperty("chrome.options.window-size");

    /** Constant <code>chromeOptionsStartMaximized="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromeOptionsStartMaximized = Objects.isNull(WebFrameworkActions.getProperty("chrome.options.start-maximized")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.options.start-maximized") : WebFrameworkActions.getProperty("chrome.options.start-maximized");

    /** Constant <code>chromeOptionsMockWebCamPath="Objects.isNull(WebFrameworkActions.getP"{trunked}</code> */
    public static final String chromeOptionsMockWebCamPath = Objects.isNull(WebFrameworkActions.getProperty("chrome.options.mockWebCamPath")) ?
            ConfigReader.getConfigValue(CONFIGURATION_FILE_NAME,"chrome.options.mockWebCamPath") : WebFrameworkActions.getProperty("chrome.options.mockWebCamPath");
}
