package discodeit.repository.jcf;

import discodeit.enity.User;
import discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;   // 모든 유저 데이터, key=id

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public void delete(UUID userId) {
        data.remove(userId);
    }

    @Override
    public User findById(UUID userId) {
        User user = this.data.get(userId);
        return Optional.ofNullable(user)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " not found"));
    }


    public Map<UUID, User> findAll() {
        return new HashMap<>(data);
    }
}
