package ru.job4j.accidents.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import lombok.NoArgsConstructor;
import ru.job4j.accidents.model.Accident;

@NoArgsConstructor
@Repository
public class AccidentMem implements AccidentRepository {

    private Map<Integer, Accident> store = new ConcurrentHashMap<>();

    private int id = 0;
    
    @PostConstruct
    public void initStore() {
        this.create(
            new Accident(0, "car theft",
             "car theft in Moskow", "Moskow"));

        this.create(
            new Accident(0, "driving on red",
             "driving on red in Saint-Petersburg", "Saint-Petersburg"));

        this.create(
            new Accident(0, "drunk driving",
             "drunk driving in Saratov", "Saratov"));
    }

    @Override
    public void create(Accident t) {
        t.setId(id);
        store.put(id, t);
        id++;        
    }

    @Override
    public boolean delete(Accident t) {
        return store.remove(t.getId(), t);
    }

    @Override
    public List<Accident> getAll() {
        return store.values().stream().toList();
    }

    @Override
    public Optional<Accident> read(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public boolean update(Accident t) {
        return store.replace(t.getId(), t) != null;
    }

}
