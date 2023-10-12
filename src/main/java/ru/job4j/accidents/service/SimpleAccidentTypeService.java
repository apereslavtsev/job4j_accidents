package ru.job4j.accidents.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentTypeCrudRepository;

@AllArgsConstructor
@Service
public class SimpleAccidentTypeService implements AccidentTypeService {
    private final AccidentTypeCrudRepository accidentTypeRepository;

    @Override
    public AccidentType create(AccidentType t) {
        accidentTypeRepository.save(t);
        return t;
    }

    @Override
    public boolean delete(AccidentType accidentType) {
        accidentTypeRepository.delete(accidentType);
        return !accidentTypeRepository.existsById(accidentType.getId());
    }

    @Override
    public List<AccidentType> getAll() {
        return accidentTypeRepository.findAll();
    }

    @Override
    public Optional<AccidentType> read(int id) {        
        return accidentTypeRepository.findById(id);
    }

    @Override
    public boolean update(AccidentType accidentType) {
        boolean exist = accidentTypeRepository.existsById(accidentType.getId());
        if (exist) {
            accidentTypeRepository.save(accidentType);
        }
        return exist;
    }
}
