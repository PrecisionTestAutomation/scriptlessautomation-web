package in.precisiontestautomation.elementgenerator;

import in.precisiontestautomation.scriptlessautomation.core.configurations.TestNgConfig;
import in.precisiontestautomation.scriptlessautomation.core.exceptionhandling.PrecisionTestException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class GetLocators {
    private static final String LOCATOR_DIRECTORY = System.getProperty("user.dir")+File.separator+"test_data"+File.separator+"%s"+File.separator+"page_object_locators";

    private final Properties prop = new Properties();

    private Properties extractLocator(String pageName) {
        try(FileInputStream fis = new FileInputStream(String.format(LOCATOR_DIRECTORY, TestNgConfig.PLATFORM.toLowerCase())+ File.separator+pageName+".properties")){
            prop.load(fis);
        } catch (FileNotFoundException e) {
            throw new PrecisionTestException("config File not found");
        } catch (IOException e) {
            throw new PrecisionTestException("Failed on parsing the File "+ Arrays.toString(e.getStackTrace()));
        }

        return prop;
    }

    public static String getLocatorValue(String pageName,String key){
        if(!pageName.equalsIgnoreCase("NONE")) {
            return new GetLocators().extractLocator(pageName).getProperty(key);
        } else {
            return "NONE";
        }
    }
}
