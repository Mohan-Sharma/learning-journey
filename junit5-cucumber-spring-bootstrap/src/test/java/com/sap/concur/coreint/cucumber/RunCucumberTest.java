package com.sap.concur.coreint.cucumber;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mohan Sharma
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("cucumber/features")
public class RunCucumberTest {}