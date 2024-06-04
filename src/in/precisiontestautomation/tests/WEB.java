package in.precisiontestautomation.tests;


import in.precisiontestautomation.drivers.DriverManager;
import in.precisiontestautomation.scriptlessautomation.core.configurations.ExtentReportConfig;
import in.precisiontestautomation.scriptlessautomation.core.configurations.TestNgConfig;
import in.precisiontestautomation.scriptlessautomation.core.exceptionhandling.PrecisionTestException;
import in.precisiontestautomation.scriptlessautomation.core.testng.xmlgenerator.DataProviderUtil;
import in.precisiontestautomation.scriptlessautomation.core.utils.CoreKeyInitializers;
import in.precisiontestautomation.setup.WebBaseTest;
import in.precisiontestautomation.utils.ExtractTestDataFromCsv;
import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Optional;

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
    private final ThreadLocal<Boolean> validationCondition = new ThreadLocal<>();
    private final ThreadLocal<String> categoryName = new ThreadLocal<>();
    private boolean captureScreenshotOnPass = Boolean.parseBoolean(ExtentReportConfig.Report_captureScreenshotOnPass);

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
        this.validationCondition.set(condition);
        final String testCaseName = new File(filePath).getName().split("_")[0];
        categoryName.set(StringUtils.capitalize(new File(filePath).getParentFile().getName()));
        System.out.println("----------------------------------" + testCaseName + " Started----------------------------------");
        try {
            extractTestData.set(ExtractTestDataFromCsv.getInstance(filePath)
                    .collectAllSteps()
                    .executeSteps(CoreKeyInitializers.getCustomSoftAssert().get(), this.validationCondition.get(),captureScreenshotOnPass));
        } catch (Exception e) {
            throw new PrecisionTestException("Failed While running test case " + testCaseName + ", " + e.getLocalizedMessage());
        } finally {
            Reporter.getCurrentTestResult().setAttribute("testRailId", new File(filePath).getName().split("_")[0]);
            Reporter.getCurrentTestResult().setAttribute("suiteName", new File(filePath).getParentFile().getName());
        }
        System.out.println("----------------------------------" + testCaseName + " Ended----------------------------------");
    }

    /**
     * Cleans up thread-local variables used in the tests execution to free up resources and prevent memory leaks.
     * This method ensures that after each tests method execution, all thread-local storages are cleared properly.
     * This is crucial for preventing cross-thread data leakage in a multi-threaded testing environment.
     */
    @AfterMethod(alwaysRun = true)
    public void cleanUpThreadLocals() {
        validationCondition.remove();
        Optional.ofNullable(extractTestData.get()).ifPresent(ExtractTestDataFromCsv::webGlobalVariableClear);
        extractTestData.remove();

    }
}
