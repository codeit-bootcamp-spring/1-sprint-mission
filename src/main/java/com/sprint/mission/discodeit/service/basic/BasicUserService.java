package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

/**
 * BasicUserService는 UserService 인터페이스의 기본 구현체입니다.
 * 저장 로직은 UserRepository 인터페이스를 통해 처리합니다.
 */
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    /**
     * BasicUserService 생성자.
     *
     * @param userRepository UserRepository 구현체
     */
    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(User user) {
        List<User> users = userRepository.loadAll(); // 저장소에서 모든 사용자 로드
        if (users.stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + user.getEmail());
        }
        users.add(user); // 새로운 사용자 추가
        userRepository.saveAll(users); // 저장소에 저장
        System.out.println("사용자가 생성되었습니다: " + user);
    }

    @Override
    public User readUser(String id) {
        List<User> users = userRepository.loadAll(); // 저장소에서 모든 사용자 로드
        return users.stream()
                .filter(user -> user.getId().toString().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<User> readAllUsers() {
        return userRepository.loadAll(); // 저장소에서 모든 사용자 로드
    }

    @Override
    public void updateUser(User user) {
        List<User> users = userRepository.loadAll(); // 저장소에서 모든 사용자 로드
        boolean updated = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user); // 사용자 업데이트
                updated = true;
                break;
            }
        }
        if (!updated) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + user.getId());
        }
        userRepository.saveAll(users); // 저장소에 저장
        System.out.println("사용자가 업데이트되었습니다: " + user);
    }

    @Override
    public void deleteUser(String id) {
        List<User> users = userRepository.loadAll(); // 저장소에서 모든 사용자 로드
        if (!users.removeIf(user -> user.getId().toString().equals(id))) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + id);
        }
        userRepository.saveAll(users); // 저장소에 저장
        System.out.println("사용자가 삭제되었습니다: " + id);
    }
}
