package discodeit.repository.jcf;

import discodeit.entity.User;
import discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> users;

    public JCFUserRepository() {
        this.users = new HashMap<>();
    }

    @Override
    public User save(String name, String email, String phoneNumber, String password) {
        User user = new User(name, email, phoneNumber, password);
        users.put(user.getId() ,user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public void update(UUID userId, String name, String email, String phoneNumber) {

    }

    @Override
    public void updatePassword(UUID userId, String originalPassword, String newPassword) {

    }

    @Override
    public void delete(UUID userId) {

    }
}
