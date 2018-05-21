package com.cmarchive.bank.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class SpringBeanUtils implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        doSet(applicationContext);
    }

    private static synchronized void doSet(final ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static <T> T getBean(final Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static <T> T getValue(final String property, final Class<T> beanClass) {
        return context.getEnvironment().getProperty(property, beanClass);
    }
}
