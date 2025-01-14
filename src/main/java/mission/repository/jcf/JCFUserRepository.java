package mission.repository.jcf;

import mission.entity.User;
import mission.repository.UserRepository;
import mission.service.exception.DuplicateName;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();
    private final Set<String> userNames = new HashSet<>();
    // Map<UUID, Map<String, User>> 하면 닉네임 중복 로직에서 더 복잡할 것 같아서 분리

    public User saveUser(User user) {
        // 유저이름 중복에 대한 확인은 R.M 에서 이미 하고 온 상태
        return saveUserMethod(user);
    }

    public User findById(UUID id) {
        try {
            return data.get(id);
        } catch (Exception e) {
            throw new NullPointerException("ID를 잘못입력하셨습니다.");
        }
    }

    @Override
    public User findByNamePW(String name, String password) {
        try {
            // 닉네입 존재여부 확인
            validateDuplicateUserName(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (User user : data.values()) {
            if (user.getName().equals(name) && user.getPassword().equals(password)) {
                return user;
            }
        }
        // user password 일치하는게 없으면
        return null;
    }

    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(User deletingUser) {
        // id, name, password 검증은 R.M에서 끝
        System.out.printf("닉네임 %s는 사라집니다.", deletingUser.getName());
        userNames.remove(deletingUser.getName());
        data.remove(deletingUser.getId());
    }

    @Override
    public User updateUserNamePW(User user) {
        userNames.remove(user.getOldName());
        userNames.add(user.getName());
        return user;
    }


    public User findByUsername(String username) {
        // 닉네임으로 유저 찾기
        for (User user : data.values()) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void validateDuplicateUserName(String userName) {
        if (userNames.contains(userName)) {
            throw new DuplicateName(String.format("%s(은)는 이미 존재하는 닉네임입니다", userName));
        }
    }

//    private void validationIsUsername(String username) {
//        // 빠르게 찾기 위해 셋 자료구조로 중복 확인
//        if (!userNames.contains(username)) {
//            throw new NoSuchElementException(
//                    String.format("닉네임 %s(은)는 존재하지 않습니다.", username));
//        }
//    }

//    public List<User> findUserByTeam(){
//
//    }


    private User saveUserMethod(User user) {
        data.put(user.getId(), user);
        userNames.add(user.getName());
        return user;
    }
}
