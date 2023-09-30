package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate implements AccidentRepository {

    private final JdbcTemplate jdbc;

    @Override
    public Accident create(Accident accident) {
        jdbc.update("insert into accidents (name) values (?)",
                accident.getName());
        return accident;
    }

    public List<Accident> getAll() {
        return jdbc.query("select id, name from accidents",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("id"));
                    accident.setName(rs.getString("name"));
                    return accident;
                });
    }

    @Override
    public boolean delete(Accident accident) {
        return jdbc.update(
                "delete from accidents where id = ?",
                accident.getId()) > 0;
    }

    @Override
    public Optional<Accident> read(int id) {
        Accident rsl = jdbc.queryForObject(
                "select id, name from accidents where id = ?",
                (resultSet, rowNum) -> {
                    Accident accident = new Accident();
                    accident.setId(resultSet.getInt("id"));
                    accident.setName(resultSet.getString("name"));
                    return accident;
                }, id);
       return Optional.of(rsl);
    }

    @Override
    public boolean update(Accident accident) {
        return jdbc.update(
                "update accidents set name = ? where id = ?",
                accident.getName(), accident.getId()) > 0;
    }

}
