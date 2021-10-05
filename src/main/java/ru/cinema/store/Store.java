package ru.cinema.store;

import java.util.Collection;

public interface Store<T> {
    Collection<T> findAll();

    T findById(int id);

    T save(T t);

    T delete(int id);
}
