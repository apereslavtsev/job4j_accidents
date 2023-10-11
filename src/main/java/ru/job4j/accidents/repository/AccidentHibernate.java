package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Primary
@Repository
@AllArgsConstructor
public class AccidentHibernate implements AccidentRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<Accident> getAll() {
        return crudRepository.query("from Accident", Accident.class);
    }

    @Override
    public Accident create(Accident accident) {
        crudRepository.run(session -> session.save(accident));
        return accident;
    }

    @Override
    public boolean delete(Accident accident) {
        return crudRepository.run("delete from Accident where id =:id",
                Map.of("id", accident.getId())) > 0;
    }

    @Override
    public Optional<Accident> read(int id) {
        return crudRepository.optional("select distinct accident from Accident as accident "
                + "left join fetch accident.type left join fetch accident.rules where accident.id = :id",
                Accident.class, Map.of("id", id));

    }

    @Override
    public boolean update(Accident accident) {
        boolean rsl = false;
        rsl = crudRepository.tx(session -> {
            session.merge(accident);
            return true;
        });
        return rsl;
    }

}