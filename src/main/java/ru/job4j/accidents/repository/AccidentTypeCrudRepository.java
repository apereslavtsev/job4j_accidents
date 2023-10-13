package ru.job4j.accidents.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.AccidentType;

public interface AccidentTypeCrudRepository extends CrudRepository<AccidentType, Integer> {
 
    List<AccidentType> findAll();

}
