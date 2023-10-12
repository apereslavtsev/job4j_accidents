package ru.job4j.accidents.repository;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.data.repository.CrudRepository;

import ru.job4j.accidents.model.Rule;

public interface RuleCrudRepository extends CrudRepository<Rule, Integer> {

    public default List<Rule> findAll() {
        return Lists.newArrayList(findAll());
    }

}
