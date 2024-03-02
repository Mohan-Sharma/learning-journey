package com.sap.concur.coreint.utils;

import com.sap.concur.coreint.configurations.spring.annotations.LazyComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;

/**
 * @author Mohan Sharma
 */
@LazyComponent
public class LogUtil {

    public static LogEntries getLogs(WebDriver driver) {
        return driver
            .manage()
            .logs()
            .get(LogType.BROWSER);
    }

}
