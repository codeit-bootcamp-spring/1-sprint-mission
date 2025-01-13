package mission.service.jcf;


import mission.entity.User;
import mission.repository.UserRepository;
import mission.repository.jcf.JCFUserRepository;
import mission.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final UserRepository userRepository = new JCFUserRepository();
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User create(User user) {
        return userRepository.saveUser(user);
    }

    @Override
    public User find(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override // 그냥 한번에 닉네임, 비밀번호 다 바꾼다고 가정
    public User update(UUID id, String newName, String password) {
        // id 검증
        if (!data.containsKey(id)){
            throw new NoSuchElementException("ID를 잘못입력하셨습니다.");
        }

        User user = data.get(id);
        // 이름 중복 검사
        if (userNames.contains(newName)) {
            throw new IllegalArgumentException("이미 사용 중인 이름입니다.");
        }

        // 비밀번호 검증
        if (user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }
        return user.setNamePassword(newName, password);
    }

    @Override
    public void delete(UUID id, String name, String password) {
        if (!data.containsKey(id)){
            throw new NullPointerException("ID를 잘못 입력하셨습니다.");
        }
        User deletingUser = data.get(id);
        if (!deletingUser.getName().equals(name) || !deletingUser.getPassword().equals(password)){
            throw new IllegalArgumentException("닉네임 or 비밀번호를 잘못 입력하셨습니다.");
        } else {
            data.remove(id);
            userNames.remove(name);
        }
    }
}
