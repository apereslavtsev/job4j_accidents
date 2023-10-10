package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.utils.HibernateUtils;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@AllArgsConstructor
public class AccidentHibernate implements AccidentRepository {

    private final SessionFactory sf;

    @Override
    public List<Accident> getAll() {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            return session
                    .createQuery("from Accident", Accident.class)
                    .list();

        });
    }

    @Override
    public Accident create(Accident accident) {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            session.save(accident);
            return accident;
        });
    }

    @Override
    public boolean delete(Accident accident) {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            return session.createQuery("delete from Accident where id =:id")
                    .setParameter("id", accident.getId()).executeUpdate() > 0;
        });

    }

    @Override
    public Optional<Accident> read(int id) {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            return session
                    .createQuery("select distinct accident from Accident as accident left join fetch accident.type "
                            + "left join fetch accident.rules where accident.id = :id", Accident.class)
                    .setParameter("id", id).uniqueResultOptional();
        });

    }

    @Override
    public boolean update(Accident accident) {
        boolean rsl = false;
        rsl =  HibernateUtils.executeCommandInTransaction(sf, session -> {
            session.merge(accident);
            return true;
        });
        return rsl;
    }

}