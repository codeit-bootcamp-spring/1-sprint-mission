package mission.repository.jcf;

import mission.entity.User;
import mission.repository.UserRepository;
import mission.service.exception.NotFoundId;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void saveUser(User user) {
        data.put(user.getId(), user);
    }


    @Override
    public User findById(UUID userId) {
        User findUser = data.get(userId);
        if (findUser == null) throw new NotFoundId("ID를 찾지 못했습니다.");
        else return findUser;
        // 다른 메서드가 findById를 많이 활용하는데 그걸 대비해서 null 예외처리 여기서 잡기
        // 여기서 null 처리 안하고 Optional쓰면 다른 곳에서 코드가 지저분해짐
    }

    @Override
    public Set<User> findAll() {
        return new HashSet<>(data.values());
    }

    @Override
    public void delete(User deletingUser) {
        // id, name, password 검증은 R.M에서 끝
        deletingUser.removeAllChannel();
        System.out.printf("닉네임 %s는 사라집니다.", deletingUser.getName());
        data.remove(deletingUser.getId());
    }
}
