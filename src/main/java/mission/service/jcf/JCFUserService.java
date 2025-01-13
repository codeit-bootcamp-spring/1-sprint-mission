package mission.service.jcf;


import mission.entity.User;
import mission.repository.UserRepository;
import mission.repository.jcf.JCFUserRepository;
import mission.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final UserRepository userRepository = new JCFUserRepository();
    //private final Map<UUID, User> data = new HashMap<>();

    public User create(String name, String password) {
        User user = new User(name, password);
        return userRepository.saveUser(user);
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

    @Override // 그냥 한번에 닉네임, 비밀번호 다 바꾼다고 가정
    public User update(UUID id, String newName, String password) {
        return userRepository.updateUserNamePW(id, newName, password);
    }

    @Override
    public void delete(UUID id, String name, String password) {
        userRepository.delete(id, name, password);
    }
}
