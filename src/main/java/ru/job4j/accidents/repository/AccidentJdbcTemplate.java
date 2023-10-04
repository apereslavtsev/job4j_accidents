package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Rule;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate implements AccidentRepository {

    private final JdbcTemplate jdbc;

    private final AccidentTypeRepository accidentTypeRepository;

    @Override
    public Accident create(Accident accident) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into accidents (name,text,address,accident_type_id) values (?,?,?,?)",
                    new String[] {"id"});
            ps.setString(1, accident.getName());
            ps.setString(2, accident.getText());
            ps.setString(3, accident.getAddress());
            ps.setInt(4, accident.getType().getId());
            return ps;
        }, keyHolder);
        accident.setId(keyHolder.getKeyAs(Integer.class));

        for (Rule rule : accident.getRules()) {
            jdbc.update(
                    "insert into accident_rules (accident_id, rule_id) values (?, ?)",
                    accident.getId(), rule.getId());
        }

        return accident;
    }

    public List<Accident> getAll() {
        return jdbc.query("select id, name, text, address from accidents",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("id"));
                    accident.setName(rs.getString("name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    return accident;
                });
    }

    @Override
    public boolean delete(Accident accident) {
        return jdbc.update(
                "delete from accident_rules WHERE accident_id = ?;"
                        + "delete from accidents where id = ?",
                accident.getId()) > 0;
    }

    @Override
    public Optional<Accident> read(int id) {
        Accident rsl = null;
        try {
            rsl = jdbc.queryForObject(
                    "select id, name, text, address, accident_type_id from accidents where id = ?",
                    (resultSet, rowNum) -> {
                        Accident accident = new Accident();
                        accident.setId(resultSet.getInt("id"));
                        accident.setName(resultSet.getString("name"));
                        accident.setText(resultSet.getString("text"));
                        accident.setAddress(resultSet.getString("address"));
                        accident.setType(accidentTypeRepository.read(
                                resultSet.getInt("accident_type_id")).get());
                        return accident;
                    }, id);

            List<Rule> rules = jdbc.query("select ar.rule_id as id, rules.name as name from accident_rules as ar"
                    + " left join rules on ar.rule_id = rules.id"
                    + " where ar.accident_id = ?",
                    (rs, row) -> {
                        Rule rule = new Rule(rs.getInt("id"),
                                rs.getString("name"));
                        return rule;
                    }, id);

            rsl.setRules(new HashSet<>(rules));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.of(rsl);
    }

    @Override
    public boolean update(Accident accident) {
        boolean rsl = jdbc.update(
                "update accidents set name = ?, text = ?, address = ?, accident_type_id = ?"
                        + " where id = ?",
                accident.getName(), accident.getText(), accident.getAddress(),
                accident.getType().getId(), accident.getId()) > 0;
        if (rsl) {
            jdbc.update("delete from accident_rules WHERE accident_id = ?",
                    accident.getId());
            if (accident.getRules() != null) {
                for (Rule rule : accident.getRules()) {
                    jdbc.update(
                            "insert into accident_rules (accident_id, rule_id) values (?, ?)",
                            accident.getId(), rule.getId());
                }
            }
        }
        return rsl;
    }

}
