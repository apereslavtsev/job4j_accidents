package ru.job4j.accidents.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.AccidentRepository;

@AllArgsConstructor
@Service
public class SimpleAccidentService implements AccidentService {
    private final AccidentRepository accidentRepository;
    private final RuleService ruleService;

    @Override
    public Accident create(Accident accident, String[] ruleIds) {
        if (ruleIds != null && ruleIds.length > 0) {
            List<String> rIds = List.of(ruleIds);
            accident.setRules(rIds.stream()
                    .map(id -> ruleService.read(Integer.parseInt(id)).get())
                    .collect(Collectors.toSet())
                );
        }
        accidentRepository.create(accident);
        return accident;
    }

    @Override
    public Accident create(Accident accident) {
        accidentRepository.create(accident);
        return accident;
    }

    @Override
    public boolean delete(Accident t) {
        return accidentRepository.delete(t);
    }

    @Override
    public List<Accident> getAll() {
        return accidentRepository.getAll();
    }

    @Override
    public Optional<Accident> read(int id) {        
        return accidentRepository.read(id);
    }

    @Override
    public boolean update(Accident t) {
        return accidentRepository.update(t);
    }
}
