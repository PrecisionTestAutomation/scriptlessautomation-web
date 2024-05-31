# PrecisionTestAutomation

![Java CI with Maven](https://img.shields.io/badge/Java%20CI%20with%20Maven-blue.svg)
![Maven Central](https://img.shields.io/maven-central/v/in.precisiontestautomation.scriptlessautomation/scriptlessautomation-web.svg)
![Version](https://img.shields.io/badge/version-4.4.0-blue.svg)
![Javadoc](https://img.shields.io/badge/javadoc-4.4.0-brightgreen.svg)
![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)

## Table of Contents
- [Introduction](#Introduction)
- [Prerequisites](#Prerequisites)
- [Project_Setup](#Project Setup)
- [Configuration](#Configuration)
- [Installation](#installation)
- [IntelliJ_IDEA_Setup](#IntelliJ IDEA Setup)
- [Conclusion](#Conclusion)
- [Next_Steps](#Next Steps)

## Introduction
This guide provides a step-by-step process to set up and run a Maven project with Scriptless Automation for API testing. It includes adding dependencies, configuring plugins, and setting up the necessary configurations for API automation.

## Prerequisites
Before you begin, ensure you have the following installed on your system:
* Java Development Kit (JDK)
* Apache Maven
* IntelliJ IDEA (or any preferred IDE)

## Project Setup
### 1. **Create a Maven Project**
   Create a new Maven project in your IDE or via the command line.
### 2. **Add Dependencies**
   Add the Scriptless Automation dependency for API testing to your pom.xml file:
    
```
    <dependencies>
      <dependency>
        <groupId>in.precisiontestautomation.scriptlessautomation</groupId>
        <artifactId>scriptlessautomation-api</artifactId>
        <version>LATEST</version>
      </dependency>
    </dependencies>
```
### 3. **Add Plugins**
   Add the following plugins to your pom.xml file to configure the execution of the main class and testing framework:

```
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>exec-maven-plugin</artifactId>
        <version>3.1.0</version>
        <executions>
          <execution>
            <goals>
              <goal>java</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <mainClass>in.precisiontestautomation.runner.ScriptlessApplication</mainClass>
          <arguments>
            <argument>true</argument>
          </arguments>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.0.0-M4</version>
        <configuration>
          <argLine>-XX:+ExplicitGInvokesConcurrent</argLine>
          <argLine>--add-opens java.base/java.lang.reflect=ALL-UNNAMED</argLine>
          <testFailureIgnore>false</testFailureIgnore>
          <suiteXmlFiles>
            <suiteXmlFile>target/testngenerator.xml</suiteXmlFile>
          </suiteXmlFiles>
        </configuration>
      </plugin>
    </plugins>
```

## Configuration
### 4. **Add Scriptless Configuration**
   To execute Scriptless Automation for API testing, add the following configuration files in your project:
    
```
    project
         └── config
             ├── browserConfiguration
             │   └── chrome.properties
             ├── reportConfiguration
             │   └── extentReportConfiguration.properties
             └── testNgConfiguration.properties
```

**4.1 browserConfiguration/chrome.properties**  
    This file contains the configuration settings for the Chrome browser.
```
        # Chrome Browser Configuration
        # Chrome prefs
        chrome.prefs.profile.default_content_settings.popups=0
        chrome.prefs.profile.default_content_setting_values.media_stream_camera=1
        chrome.prefs.javascript.enable=true
        chrome.prefs.credentials_enable_service=false
        chrome.prefs.profile.password_manager_enabled=false
        chrome.prefs.profile.downloadPath=target
        
        # Chrome options
        chrome.options.excludeSwitches=enable-automation
        chrome.options.no-sandbox=true
        chrome.options.disable-dev-shm-usage=true
        chrome.options.mockWebCamPath=path/to/mock/webcam
        chrome.options.headless=false
        chrome.options.window-size=1920,1080
        chrome.options.start-maximized=true
```

**4.2 reportConfiguration/extentReportConfiguration.properties**
   This file contains the configuration settings for the Extent Reports.

    # Extent Report Configuration
    ReportName = AutomationReport
    
    #DARK , STANDARD
    Report_Theme = DARK
    Report_JS = for (let e of document.querySelectorAll (".details-col")) { e.innerHTML ='Steps' };
    Report_CSS = .header .vheader .nav-logo>a .logo {min-height: 52px;min-width: 52px;}
    Report_logo = "/config/logo.jpg"
    Report_captureScreenshotOnPass = true

   **4.3 testNgConfiguration.properties**
    This file contains the TestNG configuration settings.
    
```
    # TestNG Configuration
    ReportName = AutomationReport
    Env = sandbox
    ThreadCount= 2
    
    FAILED_RETRY_COUNT=0
    SET_TEST_SUITE_NAME = Scriptless
    SET_TEST_NAME = Regression Testing
    
    TEST_DATA_SECTIONS=All
    TEST_IDS=All
    DISABLE_TEST_IDS=
    
    GROUPS=All
```

### 5. Add Test Data
Add the necessary test data to your project. This involves creating a directory structure to store test data files and ensuring they are accessible to your test scripts. Use the following directory structure:
```
    project
       └── test_data
           └── web
               ├── page_object_locators
               │   └── locators.properties
               ├── test_case_flows
               │   └── TestDirectory
               │       └── TestID_GroupName.csv
               ├── dynamic_strings
               │   ├── qa
               │   │   └── test.properties
               │   └── sandbox
               │       └── test.properties
               └── page_screenshot       
```   
       
   **5.1 test_data/web/page_object_locators/locators.properties**    
   TThis file contains all the locators used in your web pages.
```
    # Example locators
    username= ID => username
    password= ID => password
    submit= ID => submit
```
   
   **5.2 test_data/web/test_case_flows/TestDirectory/TestID_GroupName.csv**  
   This file contains test case flows and steps for each test. Each directory under test_case_flows represents a feature.
```
# Example test case flow
PAGE_NAME,WAIT_TYPE,SCROLL_TYPE,ELEMENT_NAME,ACTIONS,SEND_KEYS,VALIDATION,VALIDATION_TYPE
NONE,NONE,NONE,NONE,LOAD_URL,https://www.gmail.com/,NONE,NONE
```  

   **5.3 test_data/web/dynamic_strings**
    This file contains environment-specific dynamic strings
```
# Example dynamic strings for QA
baseUrl=https://qa.example.com
```

   **5.4 test_data/web/page_screenshot**
    This directory stores different page screenshots to do UI testing.

## IntelliJ IDEA Setup
### 6. Add IntelliJ Run Configuration
   Create a run configuration in IntelliJ IDEA to run your Maven project. Follow these steps:
1.    Go to Run > Edit Configurations.![](https://files.gitbook.com/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FiGaaGnfeM1ej74weWKLs%2Fuploads%2Fov27CJI8g209RJEE2kzj%2FScreenshot%202024-06-01%20at%201.32.26%20AM.png?alt=media&token=d2a9f970-7be7-4590-8027-95d7e4602ce1)
2.    Click on the + icon and select Application.![](https://files.gitbook.com/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FiGaaGnfeM1ej74weWKLs%2Fuploads%2F9I0zNOwOgjx8myBigtYb%2FScreenshot%202024-06-01%20at%201.33.27%20AM.png?alt=media&token=0c1267c0-25e1-46e0-8b23-9af2678d04ef)
3.    Set the name for your configuration (e.g., runner). ![](https://files.gitbook.com/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FiGaaGnfeM1ej74weWKLs%2Fuploads%2FU2SL6vg25OZtPlztqmTr%2FScreenshot%202024-06-01%20at%201.35.44%20AM.png?alt=media&token=baf687fd-df81-4f93-9f88-5e56a19b801d) 
4.    Click on Apply
### 7. Run CLI Command
   To execute the project, use the following CLI command:
```
mvn clean exec:java test
```
   
   This command will run the main class defined in the `exec-maven-plugin` configuration.

## Conclusion
   By following this guide, you have set up a Maven project with Scriptless Automation for web testing. You configured the necessary dependencies, plugins, and run configurations to execute your web automation tests.
   
## Next Steps
*    Customize your test scripts according to your web application requirements.
*    Explore more features of Scriptless Automation to enhance your test coverage.
*    Integrate additional tools and frameworks as needed for comprehensive test automation.