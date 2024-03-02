package com.sap.concur.coreint.api;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.concur.coreint.configurations.spring.annotations.LazyComponent;
import com.sap.concur.coreint.utils.SharedTestContext;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.FileCopyUtils;

/**
 * @author Mohan Sharma
 */
@LazyComponent
@Slf4j
@Getter
public class ConcurOrderApimEndpointApi extends AbstractCoreIntegrationEndpointApi {

    public ConcurOrderApimEndpointApi hitOrderApiEndpointWithEmptyAuthentication() {
        return processOrderXmlWithCredentials(authNoneHttpEntity(StringUtils.EMPTY));
    }

    public ConcurOrderApimEndpointApi verifyStatusCodeFromResponse(final int statusCode) {
        var response = SharedTestContext.CONTEXT.getResponse();
        assertThat(response.getStatusCode().value(), is(statusCode));
        return this;
    }

    public ConcurOrderApimEndpointApi forEmptyAuthenticationVerifyErrorMessage(String errorMsg) throws JsonProcessingException {
        var response = SharedTestContext.CONTEXT.getResponse();
        var body = response.getBody().toString();
        var mapper = new ObjectMapper();
        var jsonNode = mapper.readTree(body);
        assertThat(jsonNode.get("message").textValue(), is(errorMsg));
        return this;
    }

    public ConcurOrderApimEndpointApi hitOrderApiEndpointWithIncorrectCredentials() {
        return processOrderXmlWithCredentials(basicAuthJsonHttpEntity(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY));
    }

    public ConcurOrderApimEndpointApi hitOrderApiEndpointWithValidCredentialsEmptyBody() {
        return processOrderXmlWithCredentials(basicAuthJsonHttpEntity(StringUtils.EMPTY, valueForKey("concur.order.dev.user"), valueForKey("concur.order.dev.password")));
    }

    public ConcurOrderApimEndpointApi hitOrderApiEndpointWithValidCredentialsInvalidXmlBody() {
        return processOrderXmlWithCredentials(basicAuthXmlHttpEntity("<invalid>empty</invalid>", valueForKey("concur.order.dev.user"), valueForKey("concur.order.dev.password")));
    }

    public ConcurOrderApimEndpointApi hitOrderApiEndpointWithValidCredentialsValidXmlBody() throws IOException {
        var orderXml = FileCopyUtils.copyToString(new InputStreamReader(loadFile("data/order.xml"), UTF_8));
        return processOrderXmlWithCredentials(basicAuthXmlHttpEntity(orderXml, valueForKey("concur.order.dev.user"), valueForKey("concur.order.dev.password")));
    }

    public ConcurOrderApimEndpointApi validateApimErrorMessage(final String errorMsg) throws JsonProcessingException {
        var response = SharedTestContext.CONTEXT.getResponse();
        var body = response.getBody().toString();
        var mapper = new ObjectMapper();
        var jsonNode = mapper.readTree(body);
        var faultNode = jsonNode.get("fault");
        var detailNode = faultNode.get("detail");
        assertThat(detailNode.get("errorcode").textValue(), is(errorMsg));
        return this;
    }

    private ConcurOrderApimEndpointApi processOrderXmlWithCredentials(HttpEntity<String> requestEntity) {
        var response = getRestTemplate().exchange(valueForKey("concur.order.apim.endpoint"), HttpMethod.POST, requestEntity, String.class);
        SharedTestContext.CONTEXT.setResponse(response);
        return this;
    }
}

