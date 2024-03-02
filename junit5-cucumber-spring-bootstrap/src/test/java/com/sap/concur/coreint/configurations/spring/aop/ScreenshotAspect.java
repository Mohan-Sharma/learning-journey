package com.sap.concur.coreint.configurations.spring.aop;

import com.sap.concur.coreint.configurations.spring.annotations.LazyAutowired;
import com.sap.concur.coreint.configurations.spring.annotations.LazyComponent;
import com.sap.concur.coreint.configurations.spring.annotations.TakeScreenshot;
import com.sap.concur.coreint.utils.ScreenshotUtil;
import java.io.IOException;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Mohan Sharma
 */
@Aspect
@LazyComponent
@Getter
public class ScreenshotAspect {

    @LazyAutowired
    private ScreenshotUtil screenshotUtil;

    @After("@annotation(takeScreenshot)")
    public void after(JoinPoint joinPoint, TakeScreenshot takeScreenshot) throws IOException {
        getScreenshotUtil().takeScreenShot(joinPoint.getSignature().getName());
    }
}
