package com.sap.concur.apitestframework.api;

import com.sap.concur.apitestframework.configurations.annotations.LazyComponent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;

/**
 * @author Mohan Sharma
 */
@LazyComponent
@Slf4j
@AllArgsConstructor
public class ConcurOrderApimEndpointApi {

    private final TestRestTemplate restTemplate;

    public ConcurOrderApimEndpointApi hitOrderAPIEndpointWithEmptyAuthentication() {
        return this;
    }

    public ConcurOrderApimEndpointApi forEmptyAuthenticationVerifyErrorCode(final String errorCode) {
        return this;
    }

    public ConcurOrderApimEndpointApi forEmptyAuthenticationVerifyErrorMessage(String errorMsg) {
        return this;
    }
}

