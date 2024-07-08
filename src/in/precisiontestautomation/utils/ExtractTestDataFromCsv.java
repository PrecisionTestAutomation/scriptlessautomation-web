package in.precisiontestautomation.utils;


import in.precisiontestautomation.apifactory.ApiRequester;
import in.precisiontestautomation.enums.CsvFileHeader;
import in.precisiontestautomation.scriptlessautomation.core.exceptionhandling.PrecisionTestException;
import in.precisiontestautomation.scriptlessautomation.core.utils.AutomationAsserts;

import java.util.*;


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
            List<Map<String, String>> allElements = null;
            if(filePath.toLowerCase().endsWith(".csv")){
                allElements = CsvReader.readCSVFile(filePath);
            } else if(filePath.toLowerCase().endsWith(".feature")){
                allElements = GherkinReader.getInstance().readGherkinSteps(filePath);
            } else {
                throw new PrecisionTestException("Automation doesn't support the extension "+filePath.split("\\.")[1]+ " : file path -> "+filePath);
            }

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

    public ExtractTestDataFromCsv executeSteps(AutomationAsserts automationAsserts, Boolean condition,Boolean captureScreenshotOnPass) {
        Map<CsvFileHeader, Object> csvHeaderElementMapper;
        if (!table.isEmpty()) {
            for (int i = 0; i < table.size(); i++) {
                csvHeaderElementMapper = table.get(STEP + i);
                System.out.println("Step executing -> StepNo:"+STEP + (i+2)+" | "+csvHeaderElementMapper);
                if(csvHeaderElementMapper.get(CsvFileHeader.ELEMENT_NAME).toString().equalsIgnoreCase("break")){
                    automationAsserts.info("Added Breakpoint hear to stop the automation");
                    continue;
                }

                csvHeaderElementMapper.put(CsvFileHeader.WEB_ELEMENT, PerformActions.getInstance(csvHeaderElementMapper).performAction(captureScreenshotOnPass));

                if(condition) {
                    Validations.getInstance(csvHeaderElementMapper).get().performValidation(automationAsserts,captureScreenshotOnPass);
                }
                System.gc();
            }
        } else {
            throw new PrecisionTestException("No Steps are present to execute");
        }
        return this;
    }

    public ExtractTestDataFromCsv webGlobalVariableClear() {
        try {
            if (!Objects.isNull(WebKeyInitializers.getGlobalVariables().get().isEmpty())) {
                WebKeyInitializers.getGlobalVariables().get().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
