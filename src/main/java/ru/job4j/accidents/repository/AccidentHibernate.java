package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccidentHibernate implements AccidentRepository {

    private final SessionFactory sf;

    @Override
    public List<Accident> getAll() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from Accident", Accident.class)
                    .list();
        }
    }

    @Override
    public Accident create(Accident accident) {
        try (Session session = sf.openSession()) {
            session.save(accident);
            return accident;
        }
    }

    @Override
    public boolean delete(Accident accident) {
        try (Session session = sf.openSession()) {
            return session.createQuery("delete from Accident where id =:id", Accident.class)
                    .setParameter("id", accident.getId()).executeUpdate() > 0;
        }

    }

    @Override
    public Optional<Accident> read(int id) {
        try (Session session = sf.openSession()) {
            return session.createQuery("select distinct Accident from Accident as acc left join fetch acc.type "
                    + "left join fetch acc.rules where acc.id = :id", Accident.class)
                    .setParameter("id", id).uniqueResultOptional();
        }
    }

    @Override
    public boolean update(Accident accident) {
        /*try (Session session = sf.openSession()) {            
        }*/
        return false;
    }

}