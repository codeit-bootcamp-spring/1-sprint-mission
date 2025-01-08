package sprint.mission.discodeit.service.jcf;

import sprint.mission.discodeit.entity.User;
import sprint.mission.discodeit.service.UserService;
import sprint.mission.discodeit.validation.UserValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data; // assume that it is repository
    private final UserValidator   userValidator;

    private JCFUserService() {
        data          = new HashMap<>();
        userValidator = UserValidator.getInstance();
    }

    private static final class InstanceHolder {
        private final static JCFUserService INSTANCE = new JCFUserService();
    }

    public static JCFUserService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public User create(User userToCreate) {
        userValidator.validate(userToCreate);
        UUID key = userToCreate.getCommon().getId();
        return Optional.ofNullable(data.putIfAbsent(key, userToCreate))
                .map(existingUser -> User.createEmptyUser())
                .orElse(userToCreate);
    }

    @Override
    public User read(UUID key) {
        return Optional.ofNullable(data.get(key))
                .orElse(User.createEmptyUser());
    }

    @Override
    public User update(UUID key, User userToUpdate) {
        userValidator.validate(userToUpdate);
        return Optional.ofNullable(data.computeIfPresent(
                key, (id, user)-> userToUpdate))
                .orElse(User.createEmptyUser());
    }

    @Override
    public User delete(UUID key) {
        return Optional.ofNullable(data.remove(key))
                .orElse(User.createEmptyUser());
    }
}
