package in.precisiontestautomation.runner;


import in.precisiontestautomation.scriptlessautomation.core.testng.xmlgenerator.GenerateTestNg;

/**
 * <p>ScriptlessApplication class.</p>
 *
 * @author PTA-dev
 * @version 1.0
 * @since 2024-05-20
 */
public class ScriptlessApplication {


    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects
     */
    public static void main(String[] args) {
        GenerateTestNg generateTestNg = new GenerateTestNg();
        generateTestNg.collectTestData(args,"WEB");
    }
}
