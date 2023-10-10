package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.utils.HibernateUtils;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@AllArgsConstructor
public class AccidentTypeHibernate implements AccidentTypeRepository {

    private final SessionFactory sf;

    @Override
    public List<AccidentType> getAll() {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            return session
                    .createQuery("from AccidentType", AccidentType.class)
                    .list();

        });
    }

    @Override
    public AccidentType create(AccidentType accidentType) {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            session.save(accidentType);
            return accidentType;
        });
    }

    @Override
    public boolean delete(AccidentType accidentType) {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            return session.createQuery("delete from AccidentType as at where at.id = :fid")
                    .setParameter("fid", accidentType.getId()).executeUpdate() > 0;
        });

    }

    @Override
    public Optional<AccidentType> read(int id) {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            return session
                    .createQuery("from AccidentType as at where at.id = :fid",
                            AccidentType.class)
                    .setParameter("fid", id).uniqueResultOptional();
        });

    }

    @Override
    public boolean update(AccidentType accidentType) {
        boolean rsl = false;
        rsl = HibernateUtils.executeCommandInTransaction(sf, session -> {
            session.merge(accidentType);
            return true;
        });
        return rsl;
    }

}