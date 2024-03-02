package com.sap.concur.coreint.api;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;

import com.sap.concur.coreint.configurations.spring.annotations.LazyAutowired;
import com.sap.concur.coreint.configurations.spring.annotations.LazyComponent;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author Mohan Sharma
 */
@Slf4j
@LazyComponent
@Getter
public abstract class AbstractCoreIntegrationEndpointApi {

    @LazyAutowired
    private Environment environment;
    @LazyAutowired
    private TestRestTemplate restTemplate;

    private HttpHeaders xmlHttpsHeaders() {
        final var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_XML);
        return headers;
    }

    private HttpHeaders jsonHttpsHeaders() {
        final var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }

    private HttpHeaders basicAuthXmlHttpHeaders(final String username, final String password) {
        final var headers = xmlHttpsHeaders();
        addAuthorizationHeader(username, password, headers);
        return headers;
    }

    private HttpHeaders basicAuthJsonHttpHeaders(final String username, final String password) {
        final var headers = jsonHttpsHeaders();
        addAuthorizationHeader(username, password, headers);
        return headers;
    }

    private void addAuthorizationHeader(String username, String password, HttpHeaders headers) {
        final var auth = username + ":" + password;
        final var encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        final var authHeader = "Basic " + new String( encodedAuth );
        headers.add(HttpHeaders.AUTHORIZATION, authHeader);
    }

    protected HttpEntity<String> basicAuthXmlHttpEntity(String request, final String username, final String password) {
        return new HttpEntity<>(request, basicAuthXmlHttpHeaders(username, password));
    }

    protected HttpEntity<String> basicAuthJsonHttpEntity(String request, final String username, final String password) {
        return new HttpEntity<>(request, basicAuthJsonHttpHeaders(username, password));
    }

    protected HttpEntity<String> authNoneHttpEntity(String request) {
        return new HttpEntity<>(request, jsonHttpsHeaders());
    }

    protected String valueForKey(final String key) {
        return environment.getProperty(key);
    }

    protected InputStream loadFile(final String path) {
        return AbstractCoreIntegrationEndpointApi.class.getClassLoader().getResourceAsStream(path);
    }
}
