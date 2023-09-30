package ru.job4j.accidents.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import lombok.NoArgsConstructor;
import ru.job4j.accidents.model.Rule;

@NoArgsConstructor
@Repository
public class RuleMem implements RuleRepository {

    private Map<Integer, Rule> store = new ConcurrentHashMap<>();

    private AtomicInteger id = new AtomicInteger(0);

    @PostConstruct
    public void initStore() {
        this.create(new Rule(1, "Статья. 1"));
        this.create(new Rule(2, "Статья. 2"));
        this.create(new Rule(3, "Статья. 3"));
    }

    @Override
    public Rule create(Rule t) {
        t.setId(id.getAndIncrement());
        store.put(t.getId(), t);
        return t;
    }

    @Override
    public boolean delete(Rule t) {
        return store.remove(t.getId(), t);
    }

    @Override
    public List<Rule> getAll() {
        return store.values().stream().toList();
    }

    @Override
    public Optional<Rule> read(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public boolean update(Rule t) {
        return store.computeIfPresent(t.getId(), (key, value) -> t) != null;
    }

}
