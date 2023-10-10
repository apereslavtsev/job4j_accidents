package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.job4j.accidents.model.AccidentType;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccidentTypeJdbcTemplate implements AccidentTypeRepository {

    private final JdbcTemplate jdbc;

    @Override
    public AccidentType create(AccidentType accidentType) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into accident_types (name) values (?)",
                    new String[] {"id"});
            ps.setString(1, accidentType.getName());
            return ps;
        }, keyHolder);
        accidentType.setId(keyHolder.getKeyAs(Integer.class));
        return accidentType;
    }

    public List<AccidentType> getAll() {
        return jdbc.query("select id, name from accident_types",
                (rs, row) -> {
                    AccidentType accidentType = new AccidentType();
                    accidentType.setId(rs.getInt("id"));
                    accidentType.setName(rs.getString("name"));
                    return accidentType;
                });
    }

    @Override
    public boolean delete(AccidentType accidentType) {
        return jdbc.update(
                "delete from accident_types WHERE id = ?",
                accidentType.getId()) > 0;
    }

    @Override
    public Optional<AccidentType> read(int id) {
        AccidentType rsl = null;
        try {
            rsl = jdbc.queryForObject(
                    "select id, name from accident_types where id = ?",
                    (resultSet, rowNum) -> {
                        AccidentType accidentType = new AccidentType();
                        accidentType.setId(resultSet.getInt("id"));
                        accidentType.setName(resultSet.getString("name"));
                        return accidentType;
                    }, id);
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(rsl);
    }

    @Override
    public boolean update(AccidentType accidentType) {
        boolean rsl = jdbc.update(
                "update accident_types set name = ? where id = ?",
                accidentType.getName(), accidentType.getId()) > 0;
        return rsl;
    }
}