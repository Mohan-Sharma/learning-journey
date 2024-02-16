package com.sap.concur.apitestframework.cucumber;

import com.sap.concur.apitestframework.configurations.spring.SharedTestContext;
import com.sap.concur.apitestframework.utils.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;

/**
 * @author Mohan Sharma
 */
@Slf4j
@RequiredArgsConstructor
public class ApplicationHooks {

    private final ScreenshotUtil screenshotUtil;
    private final ApplicationContext applicationContext;

    @AfterStep
    public void afterStep(Scenario scenario){
        if(scenario.isFailed()){
            captureScreenshot(scenario);
        }
    }

    @AfterStep("@screenshot")
    public void afterStepScreenshot(Scenario scenario){
        captureScreenshot(scenario);
    }

    private void captureScreenshot(Scenario scenario) {
        scenario.attach(this.screenshotUtil.getScreenshot(), "image/png", scenario.getName());
    }

    @Before
    public void setUp() {
        log.info("------------- TEST CONTEXT SETUP -------------");
    }

    @After
    public void tearDown() {
        log.info("------------- TEST CONTEXT TEAR DOWN -------------");
        SharedTestContext.CONTEXT.reset();
    }

    private WebDriver getWebDriver() {
        return this.applicationContext.getBean(WebDriver.class);
    }
}
