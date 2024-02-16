package com.sap.concur.apitestframework.cucumber.steps;

import static com.sap.concur.apitestframework.utils.ConcurApiConstants.CUCUMBER_EXECUTION_PARALLEL_CONFIG_CUSTOM_PARALLELISM;
import static com.sap.concur.apitestframework.utils.ConcurApiConstants.CUSTOM_PARALLELISM;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.JUnitException;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

/**
 * @author Mohan Sharma
 */
@Slf4j
public class CustomConcurParallelExecutionStrategy implements ParallelExecutionConfigurationStrategy {

    @Override
    public ParallelExecutionConfiguration createConfiguration(ConfigurationParameters configurationParameters) {
        int parallelism =
                configurationParameters
                        .get(CUSTOM_PARALLELISM, Integer::valueOf)
                        .orElseThrow(() -> new JUnitException(String.format("Configuration parameter '%s' must be set", CUCUMBER_EXECUTION_PARALLEL_CONFIG_CUSTOM_PARALLELISM)));
        return new CustomConcurParallelExecutionConfiguration(parallelism, parallelism, parallelism, parallelism, 30);
    }

    static class CustomConcurParallelExecutionConfiguration implements ParallelExecutionConfiguration {

        private final int parallelism;
        private final int minimumRunnable;
        private final int maxPoolSize;
        private final int corePoolSize;
        private final int keepAliveSeconds;

        private CustomConcurParallelExecutionConfiguration(int parallelism, int minimumRunnable, int maxPoolSize, int corePoolSize,
                int keepAliveSeconds) {
            this.parallelism = parallelism;
            this.minimumRunnable = minimumRunnable;
            this.maxPoolSize = maxPoolSize;
            this.corePoolSize = corePoolSize;
            this.keepAliveSeconds = keepAliveSeconds;
        }

        @Override
        public int getParallelism() {
            return parallelism;
        }

        @Override
        public int getMinimumRunnable() {
            return minimumRunnable;
        }

        @Override
        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        @Override
        public int getCorePoolSize() {
            return corePoolSize;
        }

        @Override
        public int getKeepAliveSeconds() {
            return keepAliveSeconds;
        }

        @Override
        public Predicate<? super ForkJoinPool> getSaturatePredicate() {
            return (ForkJoinPool p) -> true;
        }
    }
}
