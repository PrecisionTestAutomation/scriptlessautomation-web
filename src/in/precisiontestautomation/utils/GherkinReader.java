package in.precisiontestautomation.utils;

import in.precisiontestautomation.enums.CsvFileHeader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author PTA-dev
 */
public class GherkinReader {

    private static final ThreadLocal<GherkinReader> THREAD_LOCAL_INSTANCE = ThreadLocal.withInitial(GherkinReader::new);

    private GherkinReader(){
    }

    public static GherkinReader getInstance(){
        return THREAD_LOCAL_INSTANCE.get();
    }

    public List<Map<String, String>> readGherkinSteps(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));

        List<Map<String, String>> stepsList = new ArrayList<>();
        String headers = CsvFileHeader.PAGE_NAME.name()+"|"+CsvFileHeader.WAIT_TYPE.name()+"|"+
                CsvFileHeader.SCROLL_TYPE.name()+"|"+ CsvFileHeader.ELEMENT_NAME.name()+"|"+ CsvFileHeader.ACTIONS.name()+"|"+
                CsvFileHeader.SEND_KEYS.name()+"|"+ CsvFileHeader.VALIDATION.name()+"|"+ CsvFileHeader.VALIDATION_TYPE.name();
        Pattern pattern = Pattern.compile(
                "\\b("+headers+")\\b: \"([^\"]*)\"");

        String currentPageName = "NONE"; // Initialize current PAGE_NAME as NONE

        for (String line : lines) {
            if (line.trim().startsWith("Given") || line.trim().startsWith("When") || line.trim().startsWith("Then")) {
                Map<String, String> stepMap = new HashMap<>();
                Matcher matcher = pattern.matcher(line);

                String[] keys = {CsvFileHeader.ELEMENT_NAME.name(), CsvFileHeader.WAIT_TYPE.name(),
                        CsvFileHeader.SCROLL_TYPE.name(), CsvFileHeader.ELEMENT_NAME.name(), CsvFileHeader.ACTIONS.name(),
                        CsvFileHeader.SEND_KEYS.name(), CsvFileHeader.VALIDATION.name(), CsvFileHeader.VALIDATION_TYPE.name()};

                // Initialize all keys with NONE
                for (String key : keys) {
                    stepMap.put(key, "NONE");
                }

                // Check if the current step is defining a new PAGE_NAME
                if (line.contains("Given the user is on PAGE_NAME")) {
                    if (matcher.find()) {
                        currentPageName = matcher.group(2);
                        stepMap.put("PAGE_NAME", currentPageName);
                    }
                } else {
                    stepMap.put("PAGE_NAME", currentPageName);
                    while (matcher.find()) {
                        String key = matcher.group(1);
                        String value = matcher.group(2);
                        stepMap.put(key, value);
                    }
                }

                stepsList.add(stepMap);
            }
        }
        return stepsList;
    }
}
