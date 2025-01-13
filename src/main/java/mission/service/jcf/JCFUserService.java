package mission.service.jcf;


import mission.entity.User;
import mission.repository.UserRepository;
import mission.repository.jcf.JCFUserRepository;
import mission.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final UserRepository userRepository = new JCFUserRepository();
    //private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User create(User user) {
        return userRepository.saveUser(user);
    }

    @Override
    public User update(User user) {
        return userRepository.updateUserNamePW(user);
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByNamePW(String name, String password) {
        return userRepository.findByNamePW(name, password);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    public void validateDuplicateName(String name){
        userRepository.validateDuplicateUserName(name);
    }
}
