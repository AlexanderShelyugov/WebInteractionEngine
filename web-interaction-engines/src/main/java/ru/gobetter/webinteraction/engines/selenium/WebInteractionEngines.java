package ru.gobetter.webinteraction.engines.selenium;

import ru.gobetter.webinteraction.api.WebInteractionExecutor;
import ru.gobetter.webinteraction.selenium.SeleniumDriverFactory;
import ru.gobetter.webinteraction.selenium.executor.WebInteractionExecutorImpl;
import ru.gobetter.webinteraction.selenium.pool.LimitedPoolImpl;

public class WebInteractionEngines {

    public static WebInteractionExecutor createSeleniumEngine(final String executablePath,
                                                              final int capacity,
                                                              boolean loadImages) {
        return new WebInteractionExecutorImpl(
            LimitedPoolImpl.createPool(
                () -> SeleniumDriverFactory.createNewFirefoxDriver(executablePath, loadImages),
                capacity),
            capacity
        );
    }

    public static WebInteractionExecutor createContainerEngine(int capacity) {
        return new WebInteractionExecutorImpl(
            LimitedPoolImpl.createPool(
                SeleniumDriverFactory::createNewContainer,
                capacity),
            capacity);
    }

    private WebInteractionEngines() {
    }
}
