package ru.gobetter.webinteraction.selenium.pool;

public interface ObjectHandle<T> {
    T get();

    void release();
}
