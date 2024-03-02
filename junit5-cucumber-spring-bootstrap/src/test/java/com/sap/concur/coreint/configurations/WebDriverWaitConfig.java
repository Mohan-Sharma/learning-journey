package com.sap.concur.coreint.configurations;

import com.sap.concur.coreint.configurations.spring.annotations.LazyConfiguration;
import java.time.Duration;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * @author Mohan Sharma
 */
@LazyConfiguration
@Getter
public class WebDriverWaitConfig {

    @Value("${default.timeout:30}")
    private int timeout;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebDriverWait webdriverWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofMillis(getTimeout()));
    }

    @Bean(name = "fluentWait")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FluentWait<WebDriver> webdriverFluentWait(WebDriver driver) {
        return new FluentWait<>(driver);
    }
}
