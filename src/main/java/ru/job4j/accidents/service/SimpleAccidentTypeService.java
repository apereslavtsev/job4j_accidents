package ru.job4j.accidents.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentTypeRepository;

@AllArgsConstructor
@Service
public class SimpleAccidentTypeService implements AccidentTypeService {
    private final AccidentTypeRepository accidentTypeRepository;

    @Override
    public AccidentType create(AccidentType t) {
        accidentTypeRepository.create(t);
        return t;
    }

    @Override
    public boolean delete(AccidentType t) {
        return accidentTypeRepository.delete(t);
    }

    @Override
    public List<AccidentType> getAll() {
        return accidentTypeRepository.getAll();
    }

    @Override
    public Optional<AccidentType> read(int id) {        
        return accidentTypeRepository.read(id);
    }

    @Override
    public boolean update(AccidentType t) {
        return accidentTypeRepository.update(t);
    }
}
