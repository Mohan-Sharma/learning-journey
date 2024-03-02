package com.sap.concur.coreint.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * @author Mohan Sharma
 */
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class CucumberSpringContextConfig {}
