package ru.gobetter.webinteraction.api;

import java.util.concurrent.CompletableFuture;

public interface WebInteractionExecutor {
    <R> CompletableFuture<R> execute(WebCommand<R> command);
}
