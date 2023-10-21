package ru.job4j.accidents.service;

import ru.job4j.accidents.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String name);

    boolean save(User user);

}
