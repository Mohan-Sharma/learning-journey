package com.sap.concur.coreint.cucumber.steps;

import com.sap.concur.coreint.configurations.spring.annotations.ElapsedTime;
import com.sap.concur.coreint.configurations.spring.annotations.LazyAutowired;
import com.sap.concur.coreint.pages.GooglePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Mohan Sharma
 */
@Slf4j
@Getter
public class GooglePageSteps {

    @LazyAutowired
    private GooglePage googlePage;

    @Given("a web browser")
    public void aWebBrowser() {
        log.info("Using {} browser", ((RemoteWebDriver) getGooglePage().getDriver()).getCapabilities().getBrowserName());
    }

    @When("I hit google url")
    public void iHitGoogleUrl() {
        getGooglePage()
                .hitsStoreURL();
    }

    @Then("validate google homepage title {string}")
    public void validateGoogleHomePageTitle(final String title) {
        getGooglePage()
                .validPageTitle(title);
    }

    @And("page contains a search bar with action {string}")
    @ElapsedTime
    public void pageContainsASearchBar(final String expectedAction) {
        getGooglePage()
                .validSearchBar(expectedAction);
    }

    @And("page contains google brand image with alt {string}")
    @ElapsedTime
    public void pageContainsGoogleBrandImage(final String expectedAltText) {
        getGooglePage()
                .validBrandImage(expectedAltText);
    }
}
