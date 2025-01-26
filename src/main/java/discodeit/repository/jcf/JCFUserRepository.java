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
    public void update(User user, String name, String email, String phoneNumber) {
        user.update(name, email, phoneNumber);
    }

    @Override
    public void updatePassword(User user, String originalPassword, String newPassword) {
        user.updatePassword(originalPassword, newPassword);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }
}
