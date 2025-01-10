package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCF_user implements UserService {

    private final List<User> userSet;

    public JCF_user() {
        userSet = new ArrayList<>();
    }
    @Override
    public void creat(User user) {
        boolean duplication = userSet.stream().anyMatch(user1 -> user1.getName().equals(user.getName()));
        if (duplication) {
            System.out.println("name duplication!");
        } else {
            userSet.add(user);
        }
    }

    @Override
    public void delete(User user) {
        userSet.remove(user);

    }

    @Override
    public void update(User user, String name) {
        boolean duplication = userSet.stream().anyMatch(user1 -> user1.getName().equals(name));
        userSet.forEach(users -> {
            if (users.getId().equals(user.getId()) && !duplication) {
                users.updateName(name);
            }
        });
//
    }

    @Override
    public User write(String name) {
        Optional<User> user = userSet.stream().filter(user1 -> user1.getName().equals(name)).findFirst();
        if (user.isPresent()) {
            return user.get();
        }
        else {
            System.out.println("Name not found");
            return null;
        }
    }

    @Override
    public List<User> allWrite() {
        return userSet;
    }
}
