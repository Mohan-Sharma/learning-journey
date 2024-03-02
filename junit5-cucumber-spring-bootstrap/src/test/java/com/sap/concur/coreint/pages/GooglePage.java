package com.sap.concur.coreint.pages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.concur.coreint.configurations.spring.annotations.LazyComponent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Mohan Sharma
 */
@LazyComponent
@Slf4j
@Getter
public class GooglePage extends AbstractCoreIntegrationBasePage {

    @Value("${google.page.url}")
    private String googlePageURL;

    @FindBy(how = How.CSS, using = "img[data-atf='1']")
    private WebElement brandImage;

    @FindBy(how = How.CSS, using = "form[method='get']")
    private WebElement searchForm;


    public GooglePage hitsStoreURL() {
        getDriver().get(getGooglePageURL());
        return this;
    }

    public GooglePage validPageTitle(final String expectedTitle) {
        assertEquals(expectedTitle, getDriver().getTitle());
        return this;
    }

    public GooglePage validSearchBar(final String expectedAction) {
        assertTrue(getSearchForm().isDisplayed());
        assertEquals(expectedAction, getAttributeValue(getSearchForm(), "action"));
        return this;
    }

    public GooglePage validBrandImage(final String expectedAltText) {
        assertTrue(getBrandImage().isDisplayed());
        assertEquals(expectedAltText, getAttributeValue(getBrandImage(), "alt"));
        return this;
    }
}
