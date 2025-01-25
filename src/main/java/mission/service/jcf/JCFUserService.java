package mission.service.jcf;


import mission.entity.User;
import mission.repository.jcf.JCFUserRepository;
import mission.service.UserService;
import mission.service.exception.DuplicateName;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {

    private final JCFUserRepository userRepository = new JCFUserRepository();

    public static JCFUserService jcfUserService;
    private JCFUserService() {}
    public static JCFUserService getInstance(){
        if (jcfUserService == null) return jcfUserService = new JCFUserService();
        else return jcfUserService;
    }

    @Override // 닉네임 중복 허용
    public void createOrUpdate(User user) {
        userRepository.saveUser(user);
    }

    @Override
    public void update(User user) {
        createOrUpdate(user);
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id);  // null 위험 없음
    }

    @Override
    public Set<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
//
//    @Override 닉네임 중복 허용 안할 시
//    public void validateDuplicateName(String name) {
//
//        if (!findUsersByName(name).isEmpty()) {
//            throw new DuplicateName(String.format("%s(은)는 이미 존재하는 닉네임입니다", name));
//        }
//    }
}
