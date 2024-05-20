package in.precisiontestautomation.utils;


import in.precisiontestautomation.enums.CsvFileHeader;
import in.precisiontestautomation.scriptlessautomation.core.exceptionhandling.PrecisionTestException;
import in.precisiontestautomation.scriptlessautomation.core.utils.AutomationAsserts;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class ExtractTestDataFromCsv {

    private final String STEP = "step";
    private final String filePath;
    Hashtable<String, Map<CsvFileHeader, Object>> table = new Hashtable<>();

    private ExtractTestDataFromCsv(String filePath){
        this.filePath = filePath;
    }

    public static ExtractTestDataFromCsv getInstance(String filePath) {
        return ThreadLocal.withInitial(() -> new ExtractTestDataFromCsv(filePath)).get();
    }

    public ExtractTestDataFromCsv collectAllSteps() {
        try {
            final int[] count = {0};
            List<Map<String, String>> allElements = CsvReader.readCSVFile(filePath);
            allElements.stream().iterator()
                    .forEachRemaining(elementMap -> {
                        Map<CsvFileHeader, Object> csvHeaderElementMapper = new HashMap<>();
                        elementMap.entrySet().stream().iterator()
                                .forEachRemaining(element -> {
                                    csvHeaderElementMapper.put(CsvFileHeader.valueOf(element.getKey().toUpperCase()), element.getValue());
                                });
                        table.put(STEP + count[0], csvHeaderElementMapper);
                        count[0]++;
                    });
        } catch (Exception e){
            throw new PrecisionTestException("Error while reading the file "+filePath);
        }
        return this;
    }

    public ExtractTestDataFromCsv executeSteps(WebAutomationAsserts automationAsserts, Boolean condition) {
        Map<CsvFileHeader, Object> csvHeaderElementMapper;
        if (!table.isEmpty()) {
            for (int i = 0; i < table.size(); i++) {
                csvHeaderElementMapper = table.get(STEP + i);
                System.out.println("Step executing -> StepNo:"+STEP + (i+2)+" | "+csvHeaderElementMapper);
                if(csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString().equalsIgnoreCase("break")){
                    automationAsserts.info("Added Breakpoint hear to stop the automation");
                    continue;
                }

                csvHeaderElementMapper.put(CsvFileHeader.WEB_ELEMENT, PerformActions.getInstance(csvHeaderElementMapper).performAction());

                if(condition) {
                    Validations.getInstance(csvHeaderElementMapper).get().performValidation(automationAsserts);
                }
                System.gc();
            }
        } else {
            throw new PrecisionTestException("No Steps are present to execute");
        }
        return this;
    }
}
