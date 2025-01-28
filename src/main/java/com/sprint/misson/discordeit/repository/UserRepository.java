package com.sprint.misson.discordeit.repository;

import com.sprint.misson.discordeit.entity.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    boolean delete(User user);

    User findById(String id);

    List<User> findAll();

}
