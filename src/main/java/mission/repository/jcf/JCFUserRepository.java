package mission.repository.jcf;

import mission.entity.User;
import mission.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserRepository implements UserRepository {

    // ID 별 저장 소
    private final Map<UUID, User> data = new HashMap<>();
    //private final Map<String, List<USER>> userNames = new HashSet<>();

    @Override
    public User saveUser(User user) {
        // 유저이름 중복에 대한 확인을 하고 싶으면 앞단에서 USER 생성 시 검증 메서드 사용 ㄱ
        return data.put(user.getId(), user);
    }

    // 고유 ID는 훼손하지 않고 수정
//    @Override
//    public User updateUserNamePW(User user) {
//        return data.put(user.getId(), user);
//    }

    public Set<User> findAll() {
        return new HashSet<>(data.values());
    }

    @Override
    public User findById(UUID id) {
        try {
            return data.get(id);
        } catch (NullPointerException e) {
            throw new NullPointerException("ID를 잘못입력하셨습니다.");
        }
    }

    @Override
    public void delete(User deletingUser) {
        // id, name, password 검증은 R.M에서 끝
        System.out.printf("닉네임 %s는 사라집니다.", deletingUser.getName());
        deletingUser.removeAllChannel();
        data.remove(deletingUser.getId());
    }
}
