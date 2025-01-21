package discodeit.jcf;

import discodeit.entity.User;
import discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        data = new HashMap<>();
    }

    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        data.put(user.getId(), user);

        return user;
    }

    @Override
    public User find(UUID userId) {
        User userNullable = data.get(userId);

        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public User update(UUID userId, String username, String email, String password) {
        User userNullable = data.get(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        user.update(username, email, password);

        return user;
    }

    @Override
    public void delete(UUID id) {
        if(!data.containsKey(id)) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        data.remove(id);
    }
}
