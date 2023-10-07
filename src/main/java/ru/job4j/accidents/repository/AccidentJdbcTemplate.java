package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Primary
@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate implements AccidentRepository {

    private final JdbcTemplate jdbc;

    private final AccidentExtractor accidentExtractor = new AccidentExtractor();

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

    @Override
    public List<Accident> getAll() {
        String query = """
                               select accidents.id, accidents.name, accidents.text,
                accidents.address, accident_type_id, acc.name as accident_type_name, ar.rule_id as rule_id, rules.name as rule_name
                   from accidents as accidents
                   left join accident_types as acc on accidents.accident_type_id = acc.id
                   left join accident_rules as ar on accidents.id = ar.accident_id
                   left join rules as rules on ar.rule_id = rules.id                   
                           """;
        return jdbc.query(query, accidentExtractor);
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
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("fid", id);
        String query = """
                               select accidents.id, accidents.name, accidents.text,
                accidents.address, accident_type_id, acc.name as accident_type_name, ar.rule_id as rule_id, rules.name as rule_name
                   from accidents as accidents
                   left join accident_types as acc on accidents.accident_type_id = acc.id
                   left join accident_rules as ar on accidents.id = ar.accident_id
                   left join rules as rules on ar.rule_id = rules.id
                   where accidents.id = :fid
                           """;
        List<Accident> rsl = new NamedParameterJdbcTemplate(jdbc).query(query, parameters, accidentExtractor);
        return rsl.isEmpty() ? Optional.empty() : Optional.of(rsl.get(0));
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

    private final class AccidentExtractor implements ResultSetExtractor<List<Accident>> {

        @Override
        public List<Accident> extractData(ResultSet rs) throws SQLException, DataAccessException {

            Map<Integer, Accident> rslMap = new HashMap<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                Accident accident = null;
                if (rslMap.containsKey(id)) {
                    accident = rslMap.get(id);                  
                } else {
                    accident = new Accident();
                    accident.setId(id);
                    accident.setName(rs.getString("name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    accident.setType(new AccidentType(rs.getInt("accident_type_id"),
                            rs.getString("accident_type_id")));
                    rslMap.put(accident.getId(), accident);
                }
                Set<Rule> rules = accident.getRules();
                if (rules == null) {
                    rules = new HashSet<>();
                }
                rules.add(new Rule(rs.getInt("rule_id"), 
                    rs.getString("rule_name")));
                accident.setRules(rules);
            }
            return new ArrayList<>(rslMap.values());
        }

    }

}
