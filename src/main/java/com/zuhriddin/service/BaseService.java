package com.zuhriddin.service;

import java.util.*;

public interface BaseService<T, R> {

    T add(T t);

    T get(R id);

    List<T> list();

    void delete(R id);

    T update(T t);
}
