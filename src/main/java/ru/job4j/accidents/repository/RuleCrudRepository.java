package ru.job4j.accidents.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.Rule;

public interface RuleCrudRepository extends CrudRepository<Rule, Integer> {

    List<Rule> findAll();

}
