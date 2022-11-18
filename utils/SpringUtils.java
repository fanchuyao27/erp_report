package com.sfp.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author xl
 * @version 1.0.0
 * @ClassName SpringUtils.java
 * @Description TODO
 * @createTime 2022年07月22日 14:21:00
 */
public class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    /**
     *
     * 配置ApplicationContext，在普通类中可以通过调用SpringUtils.getApplicationContext()来获取ApplicationContext对象
     * */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtils.applicationContext == null){
            SpringUtils.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    // 通过名字获取Bean
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    // 通过class获取Bean
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    // 通过name和class获取Bean对象
    public static <T> T getBean(String name, Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}
