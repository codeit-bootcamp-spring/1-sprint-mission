package mission1.service.jcf.jcf;


import mission1.entity.User;
import mission1.service.jcf.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data = new HashMap<>();
    // Map<UUID, Map<String, User>> 하면 닉네임 중복 로직에서 더 복잡할 것 같아서 분리
    private final Set<String> userNames = new HashSet<>();

    @Override
    public User create(User user) {
        // 중복 제거 (id, name)
        if (data.containsKey(user.getId())){
            throw new IllegalArgumentException("이미 존재하는 id입니다.");
        } // 중복될일이 없긴 한데....

        if (userNames.contains(user.getName())){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        // 아이디 등록 + userName 목록도 추가
        userNames.add(user.getName());
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User find(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
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
