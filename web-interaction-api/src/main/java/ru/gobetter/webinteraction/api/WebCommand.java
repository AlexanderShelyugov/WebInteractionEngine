package ru.gobetter.webinteraction.api;

import org.openqa.selenium.WebDriver;

public interface WebCommand<R> {
    R execute(WebDriver driver);
}
