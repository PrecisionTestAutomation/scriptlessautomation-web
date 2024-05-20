package in.precisiontestautomation.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import in.precisiontestautomation.annotations.ReadFile;
import in.precisiontestautomation.drivers.DriverManager;
import in.precisiontestautomation.enums.FileTypes;
import in.precisiontestautomation.scriptlessautomation.core.exceptionhandling.PrecisionTestException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class WebFrameworkActions extends ApiFrameworkActions{

    public static WebElement clearAndSendTextElementField(WebElement element, String text) {
        int fieldValue = element.getAttribute("value").length();
        element.click();
        for (int i = 0; i < fieldValue; i++) {
            element.sendKeys(Keys.BACK_SPACE);
        }
        element.sendKeys(text);
        return element;
    }

    public static void doubleClick(WebElement element) {
        Actions actions = new Actions(DriverManager.getDriver());
        actions.doubleClick(element).perform();
    }

    public static WebElement scrollToElement(WebElement element) {
        JavascriptExecutor js = DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView();", element);
        return element;
    }

    public static void clickByJavaScript(WebElement element) {
        JavascriptExecutor js = DriverManager.getDriver();
        js.executeScript("arguments[0].click();", element);
    }

    public static WebElement waitUntilElementTextLengthGreaterThanOne(WebDriverWait wait, WebElement element) {
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return (element.getText().length() > 1);
            }
        });
        return element;
    }

    public static void findBrokenLinks(List<WebElement> links, String[] strings, SoftAssert softAssert) {
        for (WebElement element : links) {
            String url = element.getAttribute("href");
            softAssert.assertEquals(element.getText(), strings[links.indexOf(element)]);
            if (url == null || url.isEmpty()) {
                softAssert.assertFalse(false, strings[links.indexOf(element)] + " is broken");
                continue;
            }
            try {
                HttpURLConnection huc = (HttpURLConnection) (new URL(url).openConnection());
                huc.setRequestMethod("HEAD");
                huc.connect();
                int respCode = huc.getResponseCode();
                softAssert.assertFalse(respCode >= 400, strings[links.indexOf(element)] + " is a valid link");
            } catch (IOException e) {
                throw new PrecisionTestException("Exception while validating link " + url);
            }
        }
    }

    private static void switchToWithPredict(BiPredicate<String, String> predicate, String string) {
        WebDriver driver = DriverManager.getDriver();
        String currentWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        Stream<String> windowHandleStream = allWindowHandles.stream();
        String newWindowHandle = windowHandleStream.filter(handle -> !handle.equals(currentWindowHandle))
                .filter(handle -> predicate.test(handle, string))
                .findFirst()
                .orElseThrow(() -> new PrecisionTestException("New window not found"));
        driver.switchTo().window(newWindowHandle);
    }

    private static BiPredicate<String, String> predicateWindowSwitchOnTitle() {
        return (handle, str) -> DriverManager.getDriver().switchTo().window(handle).getTitle().contains(str);
    }

    private static BiPredicate<String, String> predicateWindowSwitchOnUrl() {
        return (handle, url) -> DriverManager.getDriver().switchTo().window(handle).getCurrentUrl().contains(url);
    }

    public static void switchWindowByTitle(String title) {
        switchToWithPredict(predicateWindowSwitchOnTitle(), title);
    }

    public static void switchWindowByUrl(String url) {
        switchToWithPredict(predicateWindowSwitchOnUrl(), url);
    }


    public static String takeScreenShotInBase64(WebElement elementToHighlight) {
        try {
            if(Objects.nonNull(elementToHighlight)) {
                DriverManager.getDriver().executeScript("arguments[0].style.border='3px solid red'", elementToHighlight);
            }
            String screenShot =  Optional.ofNullable(DriverManager.getDriver())
                    .map(driver -> ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64))
                    .orElse(null);
            if(Objects.nonNull(elementToHighlight)) {
                DriverManager.getDriver().executeScript("arguments[0].style.border=''", elementToHighlight);
            }
            return screenShot;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeClassMethods(String className, String methodName) {
        try {
            Class<?> cls = Class.forName(WebFrameworkActions.class.getPackageName() + "." + className);
            Method method = cls.getMethod(methodName);
            Object obj = cls.getDeclaredConstructor().newInstance();
            return (T) method.invoke(obj);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMainWindow() {
        return DriverManager.getDriver().getWindowHandle();
    }

    public static void switchNewBrowserTab() {
        DriverManager.getDriver().switchTo().newWindow(WindowType.TAB);
    }


    public static String getFileWithStartName(String fileStartWithName) {
        return searchFiles(fileStartWithName, System.getProperty("user.dir") + "/test_data/");
    }

    public static WebElement clickDownArrayOnElement(WebElement element) {
        element.sendKeys(Keys.DOWN);
        return element;
    }

    public static WebElement clickTabOnElement(WebElement element) {
        element.sendKeys(Keys.TAB);
        return element;
    }


    private static Object[] prepareParametersForMethod(Method method,String methodName) {
        if (method.isAnnotationPresent(ReadFile.class)) {
            ReadFile readFile = method.getAnnotation(ReadFile.class);
            // Validate method parameters
            Class<?>[] paramTypes = method.getParameterTypes();


            Hashtable<String, List<String>> table = null;
            Set<String> headers = null;
            if (readFile.fileType() == FileTypes.CSV) {
                if (paramTypes.length != 2 ||
                        !paramTypes[0].isAssignableFrom(Hashtable.class) ||
                        !paramTypes[1].isAssignableFrom(Set.class)) {
                    throw new IllegalArgumentException("\n "+methodName+" Method parameters do not match expected types. Please use 'Hashtable<String, List<String>> table, Set<String> csvHeader' as parameters in the same sequence.");
                }
                table = readCsvFile(readFile.directoryPath(),readFile.fileNameStartsWith(),readFile.fileType(),readFile.timeOut());
                headers = table.keySet();
            } else if (readFile.fileType() == FileTypes.TXT) {
                if (paramTypes.length != 1 ||
                !paramTypes[0].isAssignableFrom(List.class)){
                    throw new IllegalArgumentException("\n "+methodName+" Method parameter do not match expected type. Please use List<String> as parameter.");
                }
                List<String> lines = readTxtFile(readFile.directoryPath(),readFile.fileNameStartsWith(),readFile.fileType(),readFile.timeOut());
                return new Object[]{lines};
            }

            // Invoke the method with the table and headers
            return new Object[]{table, headers};
        } else {
            // If no annotation, invoke the method normally
            return new Object[method.getParameterCount()];
        }
    }

    public static WebElement selectByValue(WebElement element,String sendKeys){
        Select select = new Select(element);
        select.selectByValue(sendKeys);
        return element;
    }

    public static WebElement selectByIndex(WebElement element,String sendKeys){
        Select select = new Select(element);
        select.selectByIndex(Integer.parseInt(sendKeys));
        return element;
    }

    public static WebElement selectByText(WebElement element,String sendKeys){
        Select select = new Select(element);
        select.selectByVisibleText(sendKeys);
        return element;
    }

    public static WebElement deSelectByValue(WebElement element,String sendKeys){
        Select select = new Select(element);
        select.deselectByValue(sendKeys);
        return element;
    }

    public static WebElement deSelectByIndex(WebElement element,String sendKeys){
        Select select = new Select(element);
        select.deselectByIndex(Integer.parseInt(sendKeys));
        return element;
    }

    public static WebElement deSelectByText(WebElement element,String sendKeys){
        Select select = new Select(element);
        select.deselectByVisibleText(sendKeys);
        return element;
    }

    private static Hashtable<String, List<String>> readCsvFile(String searchDirectory,String fileNameStartsWith,FileTypes fileTypes,int timeout) {
        Hashtable<String, List<String>> table = new Hashtable<>();
        String filepath = isFileDownloaded(System.getProperty("user.dir")+File.separator+searchDirectory,fileNameStartsWith,fileTypes.name(),timeout);

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filepath)).build()) {
            List<String[]> allData = reader.readAll();

            // Check if the CSV file is empty
            if (allData.isEmpty()) {
                return table;
            }

            // Extract headers (first row)
            String[] headers = allData.get(0);

            // Initialize lists for each header
            for (String header : headers) {
                table.put(header, new ArrayList<>());
            }

            // Populate the table with data
            for (int rowIndex = 1; rowIndex < allData.size(); rowIndex++) {
                String[] row = allData.get(rowIndex);
                for (int colIndex = 0; colIndex < headers.length; colIndex++) {
                    String header = headers[colIndex];
                    String value = colIndex < row.length ? row[colIndex] : ""; // Handle missing values
                    table.get(header).add(value);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return table;
    }

    private static List<String> readTxtFile(String searchDirectory,String fileNameStartsWith,FileTypes fileTypes,int timeout) {
        List<String> lines = new ArrayList<>();
        String filepath = isFileDownloaded(System.getProperty("user.dir")+File.separator+searchDirectory,fileNameStartsWith,fileTypes.name(),timeout);
        try {
            lines = Files.readAllLines(Path.of(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static String isFileDownloaded(String directory,String fileText, String fileExtension, int waitTillSeconds) {
        File[] listOfFiles;
        boolean fileDownloaded = false;
        String filePath = null;

        long waitTillTime = Instant.now().getEpochSecond() + waitTillSeconds;
        while (Instant.now().getEpochSecond() < waitTillTime) {
            listOfFiles = new File(directory).listFiles();
            for (File file : listOfFiles) {
                String fileName = file.getName().toLowerCase();
                if (fileName.contains(fileText.toLowerCase()) && fileName.contains(fileExtension.toLowerCase())) {
                    fileDownloaded = true;
                    filePath = file.getAbsolutePath();
                    break;
                }
            }
            if (fileDownloaded) {
                break;
            }
        }
        return filePath;
    }
}
