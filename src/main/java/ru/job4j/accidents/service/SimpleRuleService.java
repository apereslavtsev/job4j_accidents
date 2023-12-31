package ru.job4j.accidents.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.RuleCrudRepository;

@AllArgsConstructor
@Service
public class SimpleRuleService implements RuleService {
    private final RuleCrudRepository ruleRepository;

    @Override
    public Rule create(Rule rule) {
        ruleRepository.save(rule);
        return rule;
    }

    @Override
    public boolean delete(Rule rule) {
        ruleRepository.delete(rule);
        return !ruleRepository.existsById(rule.getId());
    }

    @Override
    public List<Rule> getAll() {
        return ruleRepository.findAll();
    }

    @Override
    public Optional<Rule> read(int id) {        
        return ruleRepository.findById(id);
    }

    @Override
    public boolean update(Rule t) {
        boolean exist = ruleRepository.existsById(t.getId());
        if (exist) {
            ruleRepository.save(t);
        }
        return exist;
    }
}
