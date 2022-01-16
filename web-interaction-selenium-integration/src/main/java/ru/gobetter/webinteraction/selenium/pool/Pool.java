package ru.gobetter.webinteraction.selenium.pool;

public interface Pool<T> {
    ObjectHandle<T> obtain();
}
