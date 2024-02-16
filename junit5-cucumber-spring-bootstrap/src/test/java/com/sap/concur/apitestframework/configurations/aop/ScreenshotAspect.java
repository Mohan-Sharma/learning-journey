package com.sap.concur.apitestframework.configurations.aop;

import com.sap.concur.apitestframework.configurations.annotations.LazyPrototypeComponent;
import com.sap.concur.apitestframework.configurations.annotations.TakeScreenshot;
import com.sap.concur.apitestframework.utils.ScreenshotUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Mohan Sharma
 */
@Aspect
@LazyPrototypeComponent
@RequiredArgsConstructor
public class ScreenshotAspect {

    private ScreenshotUtil screenshotUtil;

    @After("@annotation(takeScreenshot)")
    public void after(JoinPoint joinPoint, TakeScreenshot takeScreenshot) throws IOException {
        this.screenshotUtil.takeScreenShot(joinPoint.getSignature().getName());
    }
}
