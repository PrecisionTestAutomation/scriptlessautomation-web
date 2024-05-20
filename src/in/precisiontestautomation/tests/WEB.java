package in.precisiontestautomation.tests;


import in.precisiontestautomation.drivers.DriverManager;
import in.precisiontestautomation.scriptlessautomation.core.configurations.TestNgConfig;
import in.precisiontestautomation.scriptlessautomation.core.exceptionhandling.PrecisionTestException;
import in.precisiontestautomation.scriptlessautomation.core.testng.xmlgenerator.DataProviderUtil;
import in.precisiontestautomation.setup.WebBaseTest;
import in.precisiontestautomation.utils.ExtractTestDataFromCsv;
import in.precisiontestautomation.utils.WebAutomationAsserts;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;

/**
 * <p>WEB class.</p>
 *
 * @author PTA-dev
 * @version 1.0
 * @since 2024-05-20
 */
public class WEB extends WebBaseTest {

    private static WEB instance = null;
    private final ThreadLocal<ExtractTestDataFromCsv> extractTestData = new ThreadLocal<>();

    private WEB(){
    }

    /**
     * <p>Getter for the field <code>instance</code>.</p>
     *
     * @return a {@link in.precisiontestautomation.tests.WEB} object
     */
    public static WEB getInstance() {
        if(instance == null) {
            instance = new WEB();
        }
        return instance;
    }

    /**
     * <p>beforeMethod.</p>
     *
     * @param platform a {@link java.lang.String} object
     */
    @Parameters({"Platform"})
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(String platform) {
        DriverManager.initializeDriver(platform, TestNgConfig.BROWSER);
    }

    /**
     * <p>testRunner.</p>
     *
     * @param filePath a {@link java.lang.String} object
     * @param condition a {@link java.lang.Boolean} object
     */
    @Test(dataProviderClass = DataProviderUtil.class, dataProvider = "dataProvide")
    public void testRunner(String filePath, Boolean condition) {
        final String testCaseName = new File(filePath).getName().split("_")[0];
        System.out.println("----------------------------------" + testCaseName + " Started----------------------------------");
        try {
            extractTestData.set(ExtractTestDataFromCsv.getInstance(filePath)
                    .collectAllSteps()
                    .executeSteps(WebAutomationAsserts.getInstance(), condition));
        } catch (Exception e) {
            throw new PrecisionTestException("Failed While running test case " + testCaseName + ", " + e.getLocalizedMessage());
        } finally {
            Reporter.getCurrentTestResult().setAttribute("testRailId", new File(filePath).getName().split("_")[0]);
            Reporter.getCurrentTestResult().setAttribute("suiteName", new File(filePath).getParentFile().getName());
        }
        System.out.println("----------------------------------" + testCaseName + " Ended----------------------------------");
    }
}
