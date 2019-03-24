package top.songm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;

/**
 * Logger
 * @author songm
 * @datetime 2018/12/23 16:30
 */
public class BaseLogger<T> {

    public Logger LOGGER;

    public BaseLogger() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class pClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        this.LOGGER = LoggerFactory.getLogger(pClass);
    }

}
