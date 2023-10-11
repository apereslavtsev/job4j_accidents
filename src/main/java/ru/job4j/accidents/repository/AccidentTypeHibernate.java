package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Primary
@Repository
@AllArgsConstructor
public class AccidentTypeHibernate implements AccidentTypeRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<AccidentType> getAll() {
        return crudRepository.query("from AccidentType", AccidentType.class);
    }

    @Override
    public AccidentType create(AccidentType accidentType) {
        crudRepository.run(session -> session.save(accidentType));
        return accidentType;
    }

    @Override
    public boolean delete(AccidentType accidentType) {
        return crudRepository.run("delete from AccidentType where id =:id",
                Map.of("id", accidentType.getId())) > 0;

    }

    @Override
    public Optional<AccidentType> read(int id) {
        return crudRepository.optional("from AccidentType as at where at.id = :fid",
                            AccidentType.class, Map.of("fid", id));
    }

    @Override
    public boolean update(AccidentType accidentType) {
        boolean rsl = false;
        rsl = crudRepository.tx(session -> {
            session.merge(accidentType);
            return true;
        });
        return rsl;
    }

}