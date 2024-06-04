package in.precisiontestautomation.utils;


import in.precisiontestautomation.elementgenerator.GenerateWebElement;
import in.precisiontestautomation.enums.ActionTypes;
import in.precisiontestautomation.enums.CsvFileHeader;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebElement;

import java.util.Map;

/**
 * <p>PerformActions class.</p>
 *
 * @author PTA-dev
 * @version 1.0
 * @since 2024-05-20
 */
@Getter
public class PerformActions {
    @Setter
    private String actions;
    @Setter
    private String sendKeys;
    @Setter
    private String waitType;
    @Setter
    private String pageName;
    @Setter
    private String elementName;
    @Setter
    private String scrollType;


    private PerformActions(Map<CsvFileHeader, Object> csvHeaderElementMapper) {
        setActions(csvHeaderElementMapper.get(CsvFileHeader.ACTIONS).toString());
        setSendKeys(csvHeaderElementMapper.get(CsvFileHeader.SEND_KEYS).toString());
        setWaitType(csvHeaderElementMapper.get(CsvFileHeader.WAIT_TYPE).toString());
        setPageName(csvHeaderElementMapper.get(CsvFileHeader.PAGE_NAME).toString());
        setElementName(csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString());
        setScrollType(csvHeaderElementMapper.get(CsvFileHeader.SCROLL_TYPE).toString());
    }

    /**
     * <p>getInstance.</p>
     *
     * @param csvHeaderElementMapper a {@link java.util.Map} object
     * @return a {@link in.precisiontestautomation.utils.PerformActions} object
     */
    public static PerformActions getInstance(Map<CsvFileHeader, Object> csvHeaderElementMapper) {
        return new PerformActions(csvHeaderElementMapper);
    }

    /**
     * <p>performAction.</p>
     *
     * @return a {@link org.openqa.selenium.WebElement} object
     */
    public WebElement performAction(boolean captureScreenshotOnPass) {
        WebElement element = GenerateWebElement.getInstance(this).get().getElement();
        return ActionClass.actionsToBePerformed(element,this,captureScreenshotOnPass).getOrDefault(ActionTypes.valueOf(actions), () -> {
            throw new IllegalArgumentException("Invalid action: " + actions);
        }).get();
    }
}
