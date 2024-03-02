package com.sap.concur.coreint.configurations;

import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.BROWSER;
import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.CHROME;
import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.EDGE;
import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.FIREFOX;
import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.REMOTE_PROFILE;
import static com.sap.concur.coreint.utils.ConcurCoreIntegrationConstants.SAFARI;

import com.sap.concur.coreint.configurations.spring.annotations.ConcurThreadScoped;
import com.sap.concur.coreint.configurations.spring.annotations.LazyConfiguration;
import java.net.URL;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * @author Mohan Sharma
 */
@Profile(REMOTE_PROFILE)
@LazyConfiguration
@Getter
public class RemoteWebDriverConfig {

    @Value("${selenium.grid.url}")
    private URL url;

    @ConcurThreadScoped
    @ConditionalOnProperty(name = BROWSER, havingValue = FIREFOX)
    @Primary
    public WebDriver remoteFirefoxDriver(){
        var firefoxOptions = new FirefoxOptions();
        return new RemoteWebDriver(getUrl(), firefoxOptions);
    }

    @ConcurThreadScoped
    @ConditionalOnProperty(name = BROWSER, havingValue = EDGE)
    @Primary
    public WebDriver remoteEdgeDriver(){
        var edgeOptions = new EdgeOptions();
        return new RemoteWebDriver(getUrl(), edgeOptions);
    }

    @ConcurThreadScoped
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = BROWSER, havingValue = CHROME)
    @Primary
    public WebDriver remoteChromeDriver(){
        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--no-sandbox");
        return new RemoteWebDriver(getUrl(), chromeOptions);
    }

    @ConcurThreadScoped
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = BROWSER, havingValue = SAFARI)
    @Primary
    public WebDriver remoteSafariDriver(){
        var safariOptions = new SafariOptions();
        return new RemoteWebDriver(getUrl(), safariOptions);
    }
}
