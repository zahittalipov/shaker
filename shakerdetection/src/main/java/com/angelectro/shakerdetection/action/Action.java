package com.angelectro.shakerdetection.action;

/**
 * Created by Загит Талипов on 03.05.2017.
 */

public interface Action<T> {
    void call(T t);
}
