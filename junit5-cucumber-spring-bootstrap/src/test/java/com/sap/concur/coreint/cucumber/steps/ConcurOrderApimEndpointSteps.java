package com.sap.concur.coreint.cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sap.concur.coreint.api.ConcurOrderApimEndpointApi;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mohan Sharma
 */
@Slf4j
@Getter
@AllArgsConstructor
public class ConcurOrderApimEndpointSteps extends AbstractEndpointSteps {

    private ConcurOrderApimEndpointApi concurOrderApimEndpointApi;

    @Given("I am an unauthorized sender")
    public void iAmAnUnauthorizedSender() {}

    @When("I hit apim endpoint with empty authentication")
    public void iHitApimEndpointWithEmptyAuthentication() {
        getConcurOrderApimEndpointApi().hitOrderApiEndpointWithEmptyAuthentication();
    }

    @And("I can see error message {string}")
    public void iCanSeeErrorMessage(final String errorMsg) throws JsonProcessingException {
        getConcurOrderApimEndpointApi().forEmptyAuthenticationVerifyErrorMessage(errorMsg);
    }

    @When("I hit apim endpoint with incorrect credentials")
    public void iHitApimEndpointWithIncorrectCredentials() {
        getConcurOrderApimEndpointApi().hitOrderApiEndpointWithIncorrectCredentials();
    }

    @Given("I am an authorized sender")
    public void iAmAnAuthorizedSender() {
    }

    @When("I hit apim endpoint with valid credentials")
    public void iHitApimEndpointWithValidCredentials() {
        getConcurOrderApimEndpointApi().hitOrderApiEndpointWithValidCredentialsEmptyBody();
    }

    @Then("I can see status code {string}")
    public void iCanSeeStatusCode(final String status) {
        getConcurOrderApimEndpointApi().verifyStatusCodeFromResponse(Integer.parseInt(status));
    }

    @Given("I am an authenticated sender")
    public void iAmAnAuthenticatedSender() {
    }

    @When("I hit apim endpoint with invalid order xml")
    public void iHitApimEndpointWithInvalidOrderXml() {
        getConcurOrderApimEndpointApi().hitOrderApiEndpointWithValidCredentialsInvalidXmlBody();
    }

    @And("validation error message")
    public void validationErrorMessage(final DataTable dataTable) throws JsonProcessingException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);
        getConcurOrderApimEndpointApi().validateApimErrorMessage(data.get("errorMsg"));
    }

    @When("I hit apim endpoint with valid order xml")
    public void iHitApimEndpointWithValidOrderXml() throws IOException {
        getConcurOrderApimEndpointApi().hitOrderApiEndpointWithValidCredentialsValidXmlBody();
    }
}
