package ru.job4j.accidents.repository;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.data.repository.CrudRepository;

import ru.job4j.accidents.model.AccidentType;

public interface AccidentTypeCrudRepository extends CrudRepository<AccidentType, Integer> {
 
    public default List<AccidentType> findAll() {
        return Lists.newArrayList(findAll());
    }

}
