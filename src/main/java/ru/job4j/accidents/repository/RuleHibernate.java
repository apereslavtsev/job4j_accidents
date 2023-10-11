package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import ru.job4j.accidents.model.Rule;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Primary
@Repository
@AllArgsConstructor
public class RuleHibernate implements RuleRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<Rule> getAll() {
        return crudRepository.query("from Rule", Rule.class);
    }

    @Override
    public Rule create(Rule rule) {
        crudRepository.run(session -> session.save(rule));
        return rule;
    }

    @Override
    public boolean delete(Rule rule) {
        return crudRepository.run("delete from Rule where id =:id",
                Map.of("id", rule.getId())) > 0;

    }

    @Override
    public Optional<Rule> read(int id) {
        return crudRepository.optional("from Rule as r where r.id = :fid",
                            Rule.class, Map.of("fid", id));
    }

    @Override
    public boolean update(Rule rule) {
        boolean rsl = false;
        rsl = crudRepository.tx(session -> {
            session.merge(rule);
            return true;
        });
        return rsl;
    }

}