package com.zuhriddin.service;

import java.util.List;

public interface BaseService<T, R> {

    T add(T t);

    T get(R id);

    List<T> list();

    void delete(R id);
}
