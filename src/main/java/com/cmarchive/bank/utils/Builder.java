package com.cmarchive.bank.utils;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Builder<T> {

    private static final Logger LOG = LoggerFactory.getLogger(Builder.class);
    
    private T instance;

    public Builder(Class<T> clazz) {
        try {
            instance = clazz.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            LOG.error("Erro occurs during builder instanciation.", e);
        }
    };
    
    public static <T> Builder<T> build(Class<T> clazz) {
        return new Builder<>(clazz);
    }
    
    public Builder<T> with(Consumer<T> consumer) {
        consumer.accept(instance);
        return this;
    }
    
    public T get() {
        return instance;
    }
    
}
