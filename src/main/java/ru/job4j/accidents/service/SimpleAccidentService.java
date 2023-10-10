package ru.job4j.accidents.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.AccidentCrudRepository;
import ru.job4j.accidents.repository.RuleCrudRepository;

@AllArgsConstructor
@Service
public class SimpleAccidentService implements AccidentService {

    private final AccidentCrudRepository accidentRepository;
    
    private final RuleCrudRepository ruleRepository;

    @Override
    public Accident create(Accident accident, String[] ruleIds) {
        if (ruleIds != null && ruleIds.length > 0) {
            List<String> rIds = List.of(ruleIds);
            accident.setRules(rIds.stream()
                    .map(id -> ruleRepository.findById(Integer.parseInt(id)).get())
                    .collect(Collectors.toSet())
                );
        }
        accidentRepository.save(accident);
        return accident;
    }

    @Override
    public Accident create(Accident accident) {
        accidentRepository.save(accident);
        return accident;
    }

    @Override
    public boolean delete(Accident accident) {
        accidentRepository.delete(accident);
        return !accidentRepository.existsById(accident.getId());
    }

    @Override
    public List<Accident> getAll() {
        return Lists.newArrayList(accidentRepository.findAll());
    }

    @Override
    public Optional<Accident> read(int id) {        
        return accidentRepository.findById(id);
    }

    @Override
    public boolean update(Accident t) {
        accidentRepository.save(t);
        return true;
    }
}
