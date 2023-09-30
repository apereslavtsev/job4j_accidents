package ru.job4j.accidents.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.RuleRepository;

@AllArgsConstructor
@Service
public class SimpleRuleService implements RuleService {
    private final RuleRepository ruleRepository;

    @Override
    public Rule create(Rule t) {
        ruleRepository.create(t);
        return t;
    }

    @Override
    public boolean delete(Rule t) {
        return ruleRepository.delete(t);
    }

    @Override
    public List<Rule> getAll() {
        return ruleRepository.getAll();
    }

    @Override
    public Optional<Rule> read(int id) {        
        return ruleRepository.read(id);
    }

    @Override
    public boolean update(Rule t) {
        return ruleRepository.update(t);
    }
}
