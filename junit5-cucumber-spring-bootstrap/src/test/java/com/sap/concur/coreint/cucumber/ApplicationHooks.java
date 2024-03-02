package com.sap.concur.coreint.cucumber;

import com.sap.concur.coreint.configurations.spring.annotations.LazyAutowired;
import com.sap.concur.coreint.utils.ScreenshotUtil;
import com.sap.concur.coreint.utils.SharedTestContext;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Mohan Sharma
 */
@Slf4j
@Getter
public class ApplicationHooks {

    @LazyAutowired
    private ScreenshotUtil screenshotUtil;
    @LazyAutowired
    private ApplicationContext applicationContext;

    @Before(value = "@ui")
    public void deleteCookies() {
        getWebDriver().manage().deleteAllCookies();
        getWebDriver().manage().window().maximize();
    }

    @After
    public void afterAPI(){
        SharedTestContext.CONTEXT.reset();
    }

    @After(value = "@ui")
    public void afterUIScenario(){
        getWebDriver().quit();
    }

    @AfterStep(value = "@ui")
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
        scenario.attach(getScreenshotUtil().getScreenshot(), "image/png", scenario.getName());
    }

    private WebDriver getWebDriver() {
        return getApplicationContext().getBean(WebDriver.class);
    }
}
