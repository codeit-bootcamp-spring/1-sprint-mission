package discodeit.service.basic;

import discodeit.entity.User;
import discodeit.repository.UserRepository;
import discodeit.service.UserService;
import discodeit.validator.UserValidator;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserValidator validator;

    public BasicUserService(UserRepository userRepository, UserValidator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    public User create(String name, String email, String phoneNumber, String password) {
        validator.validate(name, email, phoneNumber);
        isDuplicateEmail(email);
        isDuplicatePhoneNumber(phoneNumber);
        return userRepository.save(name, email, phoneNumber, password);
    }

    @Override
    public User find(UUID userId) {
        return Optional.ofNullable(userRepository.find(userId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public String getInfo(UUID userId) {
        return find(userId).toString();
    }

    @Override
    public void update(UUID userId, String name, String email, String phoneNumber) {
        validator.validate(name, email, phoneNumber);
        User user = find(userId);
        userRepository.update(user, name, email, phoneNumber);
    }

    @Override
    public void updatePassword(UUID userId, String originalPassword, String newPassword) {
        User user = find(userId);
        userRepository.updatePassword(user, originalPassword, newPassword);
        user.updateUpdatedAt();
    }

    @Override
    public void delete(UUID userId) {
        User user = find(userId);
        userRepository.delete(user);
    }

    @Override
    public void isDuplicateEmail(String email) {
        userRepository.findAll().forEach(user -> user.isDuplicateEmail(email));
    }

    @Override
    public void isDuplicatePhoneNumber(String phoneNumber) {
        userRepository.findAll().forEach(user -> user.isDuplicatePhoneNumber(phoneNumber));
    }
}
