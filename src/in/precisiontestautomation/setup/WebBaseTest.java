package in.precisiontestautomation.setup;

import in.precisiontestautomation.drivers.DriverManager;
import in.precisiontestautomation.scriptlessautomation.core.configurations.TestNgConfig;
import in.precisiontestautomation.scriptlessautomation.core.testng.setup.BaseTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

/**
 * <p>WebBaseTest class.</p>
 *
 * @author PTA-dev
 * @version 1.0
 * @since 2024-05-20
 */
public class WebBaseTest extends BaseTest {

    /**
     * <p>beforeSuiteWeb.</p>
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuiteWeb(){
        TestNgConfig.PLATFORM = "WEB";
    }

    /**
     * <p>afterMethodWeb.</p>
     *
     * @param result a {@link org.testng.ITestResult} object
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethodWeb(ITestResult result){
        DriverManager.quit();
    }
}
