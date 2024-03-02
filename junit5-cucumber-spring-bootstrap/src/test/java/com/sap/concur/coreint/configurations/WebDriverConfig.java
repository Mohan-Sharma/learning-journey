package com.sap.concur.coreint.configurations;

import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.BROWSER;
import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.CHROME;
import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.EDGE;
import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.FIREFOX;
import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.NON_REMOTE_PROFILE;
import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.SAFARI;

import com.sap.concur.coreint.configurations.spring.annotations.ConcurThreadScoped;
import com.sap.concur.coreint.configurations.spring.annotations.LazyConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * @author Mohan Sharma
 */
@Profile(NON_REMOTE_PROFILE)
@LazyConfiguration
public class WebDriverConfig {

    @ConcurThreadScoped
    @ConditionalOnProperty(name = BROWSER, havingValue = FIREFOX)
    @Primary
    public WebDriver firefoxDriver() {
        return new FirefoxDriver();
    }

    @ConcurThreadScoped
    @ConditionalOnProperty(name = BROWSER, havingValue = EDGE)
    @Primary
    public WebDriver edgeDriver() {
        return new EdgeDriver();
    }

    @ConcurThreadScoped
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = BROWSER, havingValue = CHROME)
    @Primary
    public WebDriver chromeDriver() {
        return new ChromeDriver();
    }

    @ConcurThreadScoped
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = BROWSER, havingValue = SAFARI)
    @Primary
    public WebDriver safariDriver() {
        return new SafariDriver();
    }
}
