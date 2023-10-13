package ru.job4j.accidents.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.job4j.accidents.model.Accident;

public interface AccidentCrudRepository extends CrudRepository<Accident, Integer> {

    List<Accident> findAll();
}
