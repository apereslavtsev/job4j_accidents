package ru.job4j.accidents.service;

import java.util.List;
import java.util.Optional;

import ru.job4j.accidents.model.Accident;

public interface AccidentService {

    Accident create(Accident accident, String[] ruleIds);

    Accident create(Accident accident);

    Optional<Accident> read(int id);

    boolean update(Accident accident);

    boolean delete(Accident accident);

    List<Accident> getAll();
    
}