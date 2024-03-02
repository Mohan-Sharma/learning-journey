package com.sap.concur.coreint.pages;

import com.sap.concur.coreint.configurations.spring.annotations.LazyAutowired;
import com.sap.concur.coreint.utils.LogUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Mohan Sharma
 */
@Getter
public abstract class AbstractCoreIntegrationBasePage {

    @Autowired
    protected WebDriver driver;
    @LazyAutowired
    protected WebDriverWait wait;
    @LazyAutowired
    protected JavascriptExecutor javascriptExecutor;
    @LazyAutowired
    protected LogUtil logUtil;
    @LazyAutowired
    protected Actions actions;
    @LazyAutowired
    protected FluentWait<WebDriver> fluentWait;

    @PostConstruct
    private void init() {
        PageFactory.initElements(getDriver(), this);
    }

    //Click Method by using JAVA Generics (You can use both By or Web element)
    public <T> void click(T elementAttr) {
        final var waitedElement = getWaitedWebElement(elementAttr);
        waitedElement.click();
    }

    public void jsClick(By by) {
        getJavascriptExecutor().executeScript("arguments[0].click();", getWait().until(ExpectedConditions.visibilityOfElementLocated(by)));
    }

    //Write Text by using JAVA Generics (You can use both By or WebElement)
    public <T> void writeText(T elementAttr, String text) {
        waitElements(elementAttr);
        final var waitedElement = getWaitedWebElement(elementAttr);
        waitedElement.sendKeys(text);
    }

    //Read Text by using JAVA Generics (You can use both By or WebElement)
    public <T> String readText(T elementAttr) {
        return getWaitedWebElement(elementAttr).getText().trim();
    }

    @SneakyThrows
    public <T> String readTextErrorMessage(T elementAttr) {
        Thread.sleep(2000); //This needs to be improved.
        return getDriver()
            .findElement((By) elementAttr)
            .getText();
    }

    public <T> String getAttributeValue(T elementAttr, final String attribute) {
        final var waitedElement = getWaitedWebElement(elementAttr);
        return waitedElement.getAttribute(attribute);
    }

    public <T> Boolean isVisibleInViewport(T element) {
        final String script = "var elem = arguments[0],   " +
                "  box = elem.getBoundingClientRect(),    " +
                "  cx = box.left + box.width / 2,         " +
                "  cy = box.top + box.height / 2,         " +
                "  e = document.elementFromPoint(cx, cy); " +
                "for (; e; e = e.parentElement) {         " +
                "  if (e === elem)                        " +
                "    return true;                         " +
                "}                                        " +
                "return false;                            ";
        return executeScript(element, script);
    }

    // Can return: One of Boolean, Long, Double, String, List, Map or WebElement. Or null.
    public <T> T executeScript(final String script, Class<T> type) {
        return type.cast(getJavascriptExecutor().executeScript(script));
    }

    public <T> Boolean executeScript(final T element, final String script) {
        final var webElement = getWaitedWebElement(element);
        return (Boolean)getJavascriptExecutor().executeScript(script, webElement);
    }

    private <T> boolean isBy(final T element) {
        return element
                .getClass()
                .getName()
                .contains("By");
    }

    public <T> Boolean isDisplayed(T elementAttr) {
        return getWaitedWebElement(elementAttr).isDisplayed();
    }

    public <T> WebElement getWaitedWebElement(T elementAttr) {
        if (isBy(elementAttr)) {
            return getWait().until(ExpectedConditions.presenceOfElementLocated((By) elementAttr)).findElement((By) elementAttr);
        } else {
            return getWait().until(ExpectedConditions.visibilityOf((WebElement) elementAttr));
        }
    }

    public <T> void waitElements(T elementAttr) {
        if (isBy(elementAttr)) {
            getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy((By) elementAttr));
        } else {
            getWait().until(ExpectedConditions.visibilityOfAllElements((WebElement) elementAttr));
        }
    }
}
