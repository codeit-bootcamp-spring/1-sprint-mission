package mission.service.jcf;


import mission.entity.User;
import mission.repository.jcf.JCFUserRepository;
import mission.service.UserService;
import mission.service.exception.DuplicateName;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {

    private final JCFUserRepository userRepository = new JCFUserRepository();
    //private final Map<UUID, User> data = new HashMap<>();

    public static JCFUserService jcfUserService;

    private JCFUserService() {}

    public static JCFUserService getInstance(){
        if (jcfUserService == null) return jcfUserService = new JCFUserService();
        else return jcfUserService;
    }

    @Override
    public User createOrUpdate(User user) {
        // validateDuplicateName(user.getName());
        // 닉네임 중복 허용
        return userRepository.saveUser(user);
    }

    @Override
    public User update(User user) {
        return createOrUpdate(user);
    }

    @Override
    public Set<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(UUID id) {
        // null 위험 없어짐
        return userRepository.findById(id);
    }

    @Override
    public Set<User> findUsersByName(String findName) {
        return findAll().stream()
                .filter(user -> user.getName().equals(findName))
                .collect(Collectors.toSet());
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void validateDuplicateName(String name) {
        if (!findUsersByName(name).isEmpty()) {
            throw new DuplicateName(String.format("%s(은)는 이미 존재하는 닉네임입니다", name));
        }
    }


}
