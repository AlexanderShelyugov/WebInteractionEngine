package ru.gobetter.webinteraction.selenium.pool;

import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class LimitedPoolImpl<T> implements Pool<T> {
    private final List<T> objects;
    private final List<T> lockedObjects;
    private final int capacity;
    private final Object lock;

    LimitedPoolImpl(Supplier<T> creationMethod,
                    int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be more than zero");
        }
        this.objects = new LinkedList<>();
        this.lockedObjects = new LinkedList<>();
        this.capacity = capacity;
        for (int i = 0; i < capacity; i++) {
            objects.add(creationMethod.get());
        }
        this.lock = new Object();
    }

    public static <T> Pool<T> createPool(Supplier<T> creationMethod,
                                         int capacity) {
        return new LimitedPoolImpl<>(creationMethod, capacity);
    }

    public ObjectHandle<T> obtain() {
        if (objects.size() > capacity) {
            throw new IllegalStateException("Pool is bigger than given capacity " + capacity);
        }
        while (objects.isEmpty()) {
            // All objects are used. Waiting
            Thread.yield();
        }
        T object;
        synchronized (lock) {
            object = objects.get(0);
            objects.remove(0);
            lockedObjects.add(object);
        }

        return new ObjectHandleImpl(object);
    }

    @RequiredArgsConstructor
    private class ObjectHandleImpl implements ObjectHandle<T> {
        private final T value;

        @Override
        public T get() {
            return value;
        }

        @Override
        public void release() {
            synchronized (lock) {
                lockedObjects.remove(value);
                objects.add(0, value);
            }
        }
    }
}
