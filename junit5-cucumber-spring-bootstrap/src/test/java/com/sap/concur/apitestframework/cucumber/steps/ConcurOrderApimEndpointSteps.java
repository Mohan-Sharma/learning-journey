package com.sap.concur.apitestframework.cucumber.steps;

import com.sap.concur.apitestframework.api.ConcurOrderApimEndpointApi;
import com.sap.concur.apitestframework.configurations.annotations.LazyComponent;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mohan Sharma
 */
@Slf4j
@Getter
@AllArgsConstructor
public class ConcurOrderApimEndpointSteps {

    private ConcurOrderApimEndpointApi concurOrderApimEndpointApi;

    @Given("I am an unauthorized sender")
    public void iAmAnUnauthorizedSender() {
    }

    @When("I hit apim endpoint with empty authentication")
    public void iHitApimEndpointWithEmptyAuthentication() {
        getConcurOrderApimEndpointApi().hitOrderAPIEndpointWithEmptyAuthentication();
    }

    @Then("I can see error code")
    public void iCanSeeErrorCode(final DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);
        getConcurOrderApimEndpointApi().forEmptyAuthenticationVerifyErrorCode(data.get("errorCode"));
    }

    @And("I can see error message")
    public void iCanSeeErrorMessage(final DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);
        getConcurOrderApimEndpointApi().forEmptyAuthenticationVerifyErrorMessage(data.get("errorMsg"));
    }

}
