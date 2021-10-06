package ru.cinema.store;

import java.sql.SQLException;
import java.util.Collection;

public interface Store<T> {
    Collection<T> findAll();

    T findById(int id);

    T save(T t) throws SQLException;

    T delete(int id);
}
