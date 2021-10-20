package com.dxzc.tools;

import java.util.function.Function;

public class DelayLoader<T, E> {

    public DelayLoader(T t, Function<T, E> fun) {
        loaded = false;
        a = t;
        assert fun != null;
        this.fun = fun;
    }

    public DelayLoader(E e) {
        loaded = true;
        b = e;
        this.fun = null;
    }

    private boolean loaded;

    private T a;

    private E b;

    private final Function<T, E> fun;

    public E get() {
        if (!loaded) {
            synchronized (this) {
                if (!loaded) {
                    assert fun != null;
                    b = fun.apply(a);
                    a = null;
                    loaded = true;
                }
            }
        }
        return b;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public T getUnload() {
        if (loaded) {
            throw new RuntimeException();
        }
        return a;
    }

}
