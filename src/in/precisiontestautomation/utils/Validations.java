package in.precisiontestautomation.utils;

import in.precisiontestautomation.enums.CsvFileHeader;
import in.precisiontestautomation.enums.ValidationTypes;
import in.precisiontestautomation.scriptlessautomation.core.exceptionhandling.PrecisionTestException;
import in.precisiontestautomation.scriptlessautomation.core.utils.AutomationAsserts;
import org.openqa.selenium.WebElement;

import java.util.Map;
import java.util.Objects;

public class Validations {

    Map<CsvFileHeader, Object> csvHeaderElementMapper;

    public Validations(Map<CsvFileHeader, Object> csvHeaderElementMapper) {
        this.csvHeaderElementMapper = csvHeaderElementMapper;
    }

    public static ThreadLocal<Validations> getInstance(Map<CsvFileHeader, Object> csvHeaderElementMapper) {
        return ThreadLocal.withInitial(() -> new Validations(csvHeaderElementMapper));
    }

    public void performValidation(WebAutomationAsserts automationAsserts) {
        WebElement element = (WebElement) csvHeaderElementMapper.get(CsvFileHeader.WEB_ELEMENT);
        if (!Objects.isNull(element)) {
            ValidationTypes validationType = ValidationTypes.valueOf(csvHeaderElementMapper.get(CsvFileHeader.VALIDATION_TYPE).toString());
            switch (validationType) {
                case EQUAL -> automationAsserts.assertEquals(element,csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString(), element.getText(), WebFrameworkActions.fetchPreFlow(WebFrameworkActions.integrateString(csvHeaderElementMapper.get(CsvFileHeader.VALIDATION).toString())));
                case EQUAL_IGNORE -> automationAsserts.assertEqualsIgnore(element,csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString(), element.getText(), WebFrameworkActions.fetchPreFlow(WebFrameworkActions.integrateString(csvHeaderElementMapper.get(CsvFileHeader.VALIDATION).toString())));
                case IS_DISPLAY -> automationAsserts.assertTrue(element,csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString(), element.isDisplayed(), csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME) + " is displayed", csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME) + " is not displayed");
                case IS_BUTTON_ENABLE -> automationAsserts.assertTrue(element,csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString(), element.isEnabled(), csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME) + " is Enable", csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME) + " is not Enable");
                case IS_BUTTON_DISABLE -> automationAsserts.assertTrue(element,csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString(), !element.isEnabled(), csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME) + " is not Enable", csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME) + " is Enable");
                case ATTRIBUTE_VALUE_PRESENT -> automationAsserts.assertTrue(element,csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString(), element.getAttribute("value").length() > 1, csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME) + " element value present", csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME) + " element value not present");
                case ATTRIBUTE_VALUE_NOT_PRESENT -> automationAsserts.assertTrue(element,csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString(), !(element.getAttribute("value").length() > 1), csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME) + " element value should present", csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME) + " element value not present");
                case ATTRIBUTE_EQUAL_IGNORE -> {
                    String[] attributeArray = csvHeaderElementMapper.get(CsvFileHeader.VALIDATION).toString().split("\\|");
                    if(attributeArray.length == 2) {
                        String expectedAttribute = attributeArray[0];
                        String attributeProperty = attributeArray[1];
                        automationAsserts.assertEqualsIgnore(element, csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString(), element.getAttribute(attributeProperty), WebFrameworkActions.fetchPreFlow(WebFrameworkActions.integrateString(expectedAttribute)));
                    } else {
                        throw new PrecisionTestException("ATTRIBUTE_EQUAL_IGNORE syntax should be attributeExpected|attributeProperty");
                    }
                }
                case ATTRIBUTE_EQUAL -> {
                    String[] attributeArray = csvHeaderElementMapper.get(CsvFileHeader.VALIDATION).toString().split("\\|");
                    if(attributeArray.length == 2) {
                        String expectedAttribute = attributeArray[0];
                        String attributeProperty = attributeArray[1];
                        automationAsserts.assertEquals(element, csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString(), element.getAttribute(attributeProperty), WebFrameworkActions.fetchPreFlow(WebFrameworkActions.integrateString(expectedAttribute)));
                    } else {
                        throw new PrecisionTestException("ATTRIBUTE_EQUAL syntax should be attributeExpected|attributeProperty");
                    }
                }
                case ATTRIBUTE_CONTAINS -> {
                    String[] attributeArray = csvHeaderElementMapper.get(CsvFileHeader.VALIDATION).toString().split("\\|");
                    if(attributeArray.length == 2) {
                        String expectedAttribute = attributeArray[0];
                        String attributeProperty = attributeArray[1];
                        automationAsserts.assertContains(element, csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString(), element.getAttribute(attributeProperty), WebFrameworkActions.fetchPreFlow(WebFrameworkActions.integrateString(expectedAttribute)));
                    } else {
                        throw new PrecisionTestException("ATTRIBUTE_CONTAINS syntax should be attributeExpected|attributeProperty");
                    }
                }
                case CUSTOM -> {
                    String[] customClassMethod = csvHeaderElementMapper.get(CsvFileHeader.VALIDATION).toString().split(":");
                    WebFrameworkActions.invokeCustomClassMethods(customClassMethod[0], customClassMethod[1]);
                }
                case CSS_PROPERTY -> {
                    String cssPropertyName = csvHeaderElementMapper.get(CsvFileHeader.VALIDATION).toString().split("\\|")[1];
                    String cssExpectedValue = csvHeaderElementMapper.get(CsvFileHeader.VALIDATION).toString().split("\\|")[0];
                    String actualValue = element.getCssValue(cssPropertyName);
                    String cssReportName = csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString() + " cssProperty of " + cssPropertyName;
                    automationAsserts.assertEquals(element,cssReportName,actualValue,cssExpectedValue);
                }
                case NONE -> automationAsserts.info(csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString() + " has No validation point");
                default -> throw new PrecisionTestException("Invalid validation type");
            }
        }
    }

}

