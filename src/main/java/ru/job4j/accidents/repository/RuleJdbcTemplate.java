package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
@AllArgsConstructor
public class RuleJdbcTemplate implements RuleRepository {

    private final JdbcTemplate jdbc;

    @Override
    public Rule create(Rule rule) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into rules (name) values (?)",
                    new String[] {"id"});
            ps.setString(1, rule.getName());
            return ps;
        }, keyHolder);
        rule.setId(keyHolder.getKeyAs(Integer.class));
        return rule;
    }

    public List<Rule> getAll() {
        return jdbc.query("select id, name from rules",
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                });
    }

    @Override
    public boolean delete(Rule rule) {
        return jdbc.update(
                "delete from accident_rules WHERE rule_id = ?;"
                        + "delete from rules where id = ?",
                rule.getId()) > 0;
    }

    @Override
    public Optional<Rule> read(int id) {
        Rule rsl = null;
        try {
            rsl = jdbc.queryForObject(
                    "select id, name from rules where id = ?",
                    (resultSet, rowNum) -> {
                        Rule rule = new Rule();
                        rule.setId(resultSet.getInt("id"));
                        rule.setName(resultSet.getString("name"));
                        return rule;
                    }, id);
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(rsl);
    }

    @Override
    public boolean update(Rule rule) {
        boolean rsl = jdbc.update(
                "update accidents set name = ? where id = ?",
                rule.getName(), rule.getId()) > 0;
        return rsl;
    }

}
