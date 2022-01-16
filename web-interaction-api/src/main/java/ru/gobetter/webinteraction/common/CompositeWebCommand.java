package ru.gobetter.webinteraction.common;

import lombok.val;
import org.openqa.selenium.WebDriver;
import ru.gobetter.webinteraction.api.WebCommand;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;

public final class CompositeWebCommand<T> implements WebCommand<T> {

    private Collection<WebCommand<T>> commands;

    @SafeVarargs
    public CompositeWebCommand(WebCommand<T>... commands) {
        this(asList(commands));
    }

    public CompositeWebCommand(Collection<WebCommand<T>> commands) {
        this.commands = unmodifiableCollection(commands);
    }

    @Override
    public T execute(WebDriver driver) {
        val t = new AtomicReference<T>();
        commands.forEach(command -> t.set(command.execute(driver)));
        return t.get();
    }
}
