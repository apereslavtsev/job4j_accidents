package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.utils.HibernateUtils;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@AllArgsConstructor
public class RuleHibernate implements RuleRepository {

    private final SessionFactory sf;

    @Override
    public List<Rule> getAll() {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            return session
                    .createQuery("from Rule", Rule.class)
                    .list();

        });
    }

    @Override
    public Rule create(Rule rule) {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            session.save(rule);
            return rule;
        });
    }

    @Override
    public boolean delete(Rule rule) {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            return session.createQuery("delete from Rule as r where r.id = :fid")
                    .setParameter("fid", rule.getId()).executeUpdate() > 0;
        });

    }

    @Override
    public Optional<Rule> read(int id) {
        return HibernateUtils.executeCommandInTransaction(sf, session -> {
            return session
                    .createQuery("from Rule as r where r.id = :fid",
                            Rule.class)
                    .setParameter("fid", id).uniqueResultOptional();
        });

    }

    @Override
    public boolean update(Rule rule) {
        boolean rsl = false;
        rsl = HibernateUtils.executeCommandInTransaction(sf, session -> {
            session.merge(rule);
            return true;
        });
        return rsl;
    }

}