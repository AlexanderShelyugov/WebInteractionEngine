package ru.gobetter.webinteraction.selenium.executor;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.openqa.selenium.WebDriver;
import ru.gobetter.webinteraction.api.WebCommand;
import ru.gobetter.webinteraction.selenium.pool.Pool;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
final class WebTask<R> implements Callable<R> {
    private final Pool<WebDriver> driverPool;
    private final WebCommand<R> command;

    @Override
    public R call() {
        val driverHandle = driverPool.obtain();
        R result;
        try {
            val driver = driverHandle.get();
//            driver.getKeyboard().sendKeys(Keys.CONTROL, "T");
            result = command.execute(driver);
//            driver.close();
        } finally {
            driverHandle.release();
        }
        return result;
    }
}
