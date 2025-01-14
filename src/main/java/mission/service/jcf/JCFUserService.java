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

    @Override
    public User create(User user) {
        return userRepository.saveUser(user);
    }

    @Override
    public User update(User user) {
        return userRepository.updateUserNamePW(user);
    }

    @Override
    public Set<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id);
    }

    // 이걸 레포에서 처리해야하나....
    // 중복허용이라 잘 안

    @Override
    public Set<User> findUsersByName(String findName){
        return findAll().stream()
                .filter(user -> user.getName().equals(findName))
                .collect(Collectors.toSet());
    }

    @Override
    public User findByNamePW(String name, String password) {
        return (User) findUsersByName(name).stream()
                .filter(user -> user.getPassword().equals(password));
        // 패스워드도 이름도 똑같다면 컬렉션 반환인데, 그럴 가능성 없다고 가증
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void validateDuplicateName(String name){
        if (findUsersByName(name) != null){
            throw new DuplicateName(String.format("%s(은)는 이미 존재하는 닉네임입니다", name));
        }
    }
}
