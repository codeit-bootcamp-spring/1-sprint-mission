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
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(this.data.get(userId));
    }

    public Map<UUID, User> findAll() {
        return new HashMap<>(data);
    }
}
