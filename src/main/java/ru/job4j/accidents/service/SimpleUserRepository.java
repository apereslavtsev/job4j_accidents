package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.UserRepository;

import java.util.Optional;


@Service
@AllArgsConstructor
public class SimpleUserRepository implements UserService {

    private final UserRepository userRepository;


    @Override
    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public boolean save(User user) {
        boolean rsl = true;
        try {
            userRepository.save(user);
        } catch (Exception e) {
            rsl = false;
        }
        return rsl;
    }

}
