package ru.job4j.accidents.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.AccidentRepository;

@AllArgsConstructor
@Service
public class SimpleAccidentService implements AccidentService {
    private final AccidentRepository accidentRepository;

    @Override
    public void create(Accident t) {
        accidentRepository.create(t);
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
