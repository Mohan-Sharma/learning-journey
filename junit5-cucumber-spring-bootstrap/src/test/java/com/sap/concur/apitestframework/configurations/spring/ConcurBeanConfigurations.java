package com.sap.concur.apitestframework.configurations.spring;

import com.sap.concur.apitestframework.configurations.annotations.LazyTestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * @author Mohan Sharma
 */
@LazyTestConfiguration
@Slf4j
public class ConcurBeanConfigurations {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TestRestTemplate testRestTemplate() {
        return new TestRestTemplate();
    }

}