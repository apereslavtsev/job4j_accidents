package ru.job4j.accidents.repository;

import java.util.List;
import java.util.Optional;

public interface CrudInterface<T> {
    
    T create(T t);

    Optional<T> read(int id);

    boolean update(T t);

    boolean delete(T t);

    List<T> getAll();

}
