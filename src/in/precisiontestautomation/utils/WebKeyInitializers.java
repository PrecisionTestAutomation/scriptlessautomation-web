package in.precisiontestautomation.utils;

import lombok.Getter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;

public class WebKeyInitializers extends ApiKeyInitializers{

    @Getter
    private static final ThreadLocal<String> browserName = new ThreadLocal<>();

    @Getter
    private static final ThreadLocal<String> getMainWindow = new ThreadLocal<>();

    @Getter
    private static final ThreadLocal<HashMap<Integer, Object>> devToolsListener = ThreadLocal.withInitial(HashMap::new);

    @Getter
    private static final ThreadLocal<String> firstName = new ThreadLocal<>();

    @Getter
    private static final ThreadLocal<String> lastName = new ThreadLocal<>();

    @Getter
    private static final ThreadLocal<String> phoneNumber = new ThreadLocal<>();

    @Getter
    private static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
}
