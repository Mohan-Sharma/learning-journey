package com.sap.concur.coreint.configurations;

import com.sap.concur.coreint.configurations.spring.annotations.LazyConfiguration;
import com.sap.concur.coreint.configurations.spring.scope.ConcurThreadScope;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * @author Mohan Sharma
 */
@LazyConfiguration
@Slf4j
public class ConcurBeanConfigurations {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TestRestTemplate restTemplate() {
        var testRestTemplate =  new TestRestTemplate();
        testRestTemplate.getRestTemplate().setRequestFactory(clientHttpRequestFactory());
        return testRestTemplate;
    }

    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
        var configurer = new CustomScopeConfigurer();
        configurer.addScope("concurThreadScope", new ConcurThreadScope());
        return configurer;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JavascriptExecutor javascriptExecutor(WebDriver driver) {
        return (JavascriptExecutor) driver;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Actions getActions(WebDriver driver) {
        return new Actions(driver);
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        var timeout = 10000L;
        var connConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(timeout))
                .setSocketTimeout(Timeout.ofMilliseconds(timeout))
                .build();

        var requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(timeout))
                .build();

        var cm = new BasicHttpClientConnectionManager();
        cm.setConnectionConfig(connConfig);

        var client = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(cm)
                .build();
        return new HttpComponentsClientHttpRequestFactory(client);
    }
}