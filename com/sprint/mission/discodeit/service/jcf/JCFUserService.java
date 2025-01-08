package discodeit.service.jcf;

import discodeit.entity.User;
import discodeit.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data; // assume that it is repository

    private JCFUserService() {
        data = new HashMap<>();
    }

    private static final class InstanceHolder {
        private final static JCFUserService INSTANCE = new JCFUserService();
    }

    public static JCFUserService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public User create() {
        User newUser = User.createUser();
        UUID userId = newUser.getCommon().getId();
        return data.putIfAbsent(userId, newUser);
    }

    @Override
    public User read(UUID key) {
        return data.compute(key, (id, user) -> {
            if (user == null) return User.createEmptyUser();

            return user;
        });
    }

    @Override
    public User update(UUID key, User userToUpdate) {
        if (data.containsKey(key))
        {
            UUID newKey = userToUpdate.getCommon().getId();
            data.put(newKey, userToUpdate);
            data.remove(key);
            return data.get(newKey);
        }

        return User.createEmptyUser();
    }

    @Override
    public User delete(UUID key) {
        if (data.containsKey(key))
            return data.remove(key);

        return User.createEmptyUser();
    }
}
