package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFUserRepository {
    private final List<User> users;

    public JCFUserRepository(){
        this.users = new ArrayList<>();
    }

    public void save(User user){
        users.add(user);
    }

    public void delete(User user){
        users.remove(user);
    }

    public List<User> load(){
        return users;
    }

    public Optional<User> findById(UUID userId) {
        return users.stream()
                .filter(user-> user.getId().equals(userId))
                .findFirst();
    }
}
