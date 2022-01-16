package ru.gobetter.webinteraction.selenium.executor;

import lombok.NonNull;
import lombok.val;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.gobetter.webinteraction.api.WebCommand;
import ru.gobetter.webinteraction.api.WebInteractionExecutor;
import ru.gobetter.webinteraction.selenium.pool.Pool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class WebInteractionExecutorImpl implements WebInteractionExecutor {
    private final ExecutorService executorService;
    private final Pool<WebDriver> driverPool;

    public WebInteractionExecutorImpl(Pool<WebDriver> webDriverPool, int capacity) {
        this.executorService = newFixedThreadPool(capacity);
        this.driverPool = webDriverPool;
        WebDriverWait wait;
    }

    @Override
    public <R> CompletableFuture<R> execute(@NonNull WebCommand<R> command) {
        val task = new WebTask<>(driverPool, command);
        return CompletableFuture.supplyAsync(task::call, executorService);
    }
}
