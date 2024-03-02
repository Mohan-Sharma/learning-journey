package com.sap.concur.coreint.utils;

import com.sap.concur.coreint.configurations.spring.annotations.LazyComponent;
import java.io.IOException;
import java.nio.file.Path;
import lombok.Getter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.util.FileCopyUtils;

/**
 * @author Mohan Sharma
 */
@LazyComponent
@Getter
public class ScreenshotUtil {

    @Autowired
    private ApplicationContext ctx;

    @Value("${screenshot.path}")
    private Path path;

    public void takeScreenShot(String testName) throws IOException {
        var sourceFile = getCtx().getBean(TakesScreenshot.class).getScreenshotAs(OutputType.FILE);
        FileCopyUtils.copy(sourceFile, getPath().resolve( testName + ".png").toFile());
    }

    public byte[] getScreenshot(){
        return getCtx().getBean(TakesScreenshot.class).getScreenshotAs(OutputType.BYTES);
    }

}
