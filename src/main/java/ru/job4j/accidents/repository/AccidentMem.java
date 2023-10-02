package ru.job4j.accidents.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.NoArgsConstructor;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

@Primary
@NoArgsConstructor
@Repository
public class AccidentMem implements AccidentRepository {

    private Map<Integer, Accident> store = new ConcurrentHashMap<>();

    private AtomicInteger id = new AtomicInteger(0);
    
    @PostConstruct
    public void initStore() {
        this.create(
            new Accident(1, "car theft",
             "car theft in Moskow", "Moskow", 
             new AccidentType(1, "Две машины"), 
             Set.of(new Rule(1, "Статья. 1"), new Rule(2, "Статья. 2"))
             ));

        this.create(
            new Accident(2, "driving on red",
             "driving on red in Saint-Petersburg", "Saint-Petersburg", 
             new AccidentType(2, "Машина и человек"), 
             Set.of(new Rule(3, "Статья. 3"), new Rule(2, "Статья. 2"))
             ));

        this.create(
            new Accident(3, "drunk driving",
             "drunk driving in Saratov", "Saratov", 
             new AccidentType(3, "Машина и велосипед"), 
             Set.of(new Rule(3, "Статья. 3"))
             ));
    }

    @Override
    public Accident create(Accident t) {
        t.setId(id.getAndIncrement());
        store.put(t.getId(), t);
        return t;
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
        return store.computeIfPresent(t.getId(), (key, value) -> t) != null;        
    }

}
