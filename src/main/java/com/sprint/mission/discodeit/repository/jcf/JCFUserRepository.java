package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;



@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserRepository implements UserRepository {
    private static JCFUserRepository instance;
    private final Map<UUID, User> users;

    private JCFUserRepository(){
        this.users = new HashMap<>();
    }

    public static JCFUserRepository getInstance(){
        if(instance == null){
            synchronized (JCFUserRepository.class){
                if (instance == null){
                    instance = new JCFUserRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public User save(User user){
        this.users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(UUID id){
        this.users.remove(id);
    }

    @Override
    public List<User> findAll(){
        return this.users.values().stream()
                .toList();
    }

    @Override
    public boolean existsName(String userName) {
        return this.findAll().stream()
                .anyMatch(user -> user.getUserName().equals(userName));
    }

    @Override
    public boolean existsEmail(String email) {
        return this.users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsId(UUID id) {
        return this.users.containsKey(id);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(this.users.get(id));
    }

    @Override
    public Optional<User> findByName(String name) {
        return this.users.values().stream()
                .filter(user -> user.getUserName().equals(name))
                .findFirst();
    }

}
