package discodeit.service.jcf;

import discodeit.Validator.UserValidator;
import discodeit.entity.User;
import discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final UserValidator validator;
    private final Map<UUID, User> users;

    private JCFUserService() {
        this.validator = new UserValidator();
        this.users = new HashMap<>();
    }

    private static class JCFUserServiceHolder {
        private static final UserService INSTANCE = new JCFUserService();
    }

    public static UserService getInstance() {
        return JCFUserServiceHolder.INSTANCE;
    }

    @Override
    public User create(String name, String email, String phoneNumber, String password) {
        validator.validate(name, email, phoneNumber);
        isDuplicateEmail(email);
        isDuplicatePhoneNumber(phoneNumber);
        User user = new User(name, email, phoneNumber, password);
        users.put(user.getId() ,user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        User foundUser = users.get(userId);

        return Optional.ofNullable(foundUser)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public String getInfo(UUID userId) {
        User user = find(userId);
        return user.toString();
    }

    @Override
    public void update(UUID userId, String name, String email, String phoneNumber) {
        User user = find(userId);
        validator.validateName(name);
        validator.validateEmail(email);
        validator.validatePhoneNumber(phoneNumber);

        user.update(name, email, phoneNumber);
    }

    @Override
    public void updatePassword(User user, String originalPassword, String newPassword) {
        user.updatePassword(originalPassword, newPassword);
        user.updateUpdatedAt();
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public void isDuplicateEmail(String email) {
        users.values().forEach(user -> user.isDuplicateEmail(email));
    }

    @Override
    public void isDuplicatePhoneNumber(String phoneNumber) {
        users.values().forEach(user -> user.isDuplicatePhoneNumber(phoneNumber));
    }
}
