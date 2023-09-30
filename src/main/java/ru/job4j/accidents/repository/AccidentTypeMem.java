package ru.job4j.accidents.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import lombok.NoArgsConstructor;
import ru.job4j.accidents.model.AccidentType;

@NoArgsConstructor
@Repository
public class AccidentTypeMem implements AccidentTypeRepository {

    private Map<Integer, AccidentType> store = new ConcurrentHashMap<>();

    private AtomicInteger id = new AtomicInteger(0);
    
    @PostConstruct
    public void initStore() {
        this.create(new AccidentType(1, "Две машины"));
        this.create(new AccidentType(2, "Машина и человек"));
        this.create(new AccidentType(3, "Машина и велосипед"));
    }

    @Override
    public AccidentType create(AccidentType t) {
        t.setId(id.getAndIncrement());
        store.put(t.getId(), t);
        return t;
    }

    @Override
    public boolean delete(AccidentType t) {
        return store.remove(t.getId(), t);
    }

    @Override
    public List<AccidentType> getAll() {
        return store.values().stream().toList();
    }

    @Override
    public Optional<AccidentType> read(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public boolean update(AccidentType t) {
        return store.computeIfPresent(t.getId(), (key, value) -> t) != null;        
    }

}
