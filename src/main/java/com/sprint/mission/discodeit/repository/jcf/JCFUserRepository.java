package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
    // 유저 객체가 담기는 해쉬맵
    private static final HashMap<UUID, User> usersMap = new HashMap<UUID, User>();

    // 외부에서 생성자 접근 불가
    private JCFUserRepository() {}

    // 레포지토리 객체 LazyHolder 싱글톤 구현.
    private static class JCFUserRepositoryHolder {
        private static final JCFUserRepository INSTANCE = new JCFUserRepository();
    }

    // 외부에서 호출 가능한 싱글톤 인스턴스.
    public static JCFUserRepository getInstance() {
        return JCFUserRepositoryHolder.INSTANCE;
    }

    // 유저 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, User> getUsersMap() {
        return usersMap;
    }

    // 특정 유저객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public User getUser(UUID userId) {
        if (usersMap.containsKey(userId) == false) {
            return null;
        }
        return usersMap.get(userId);
    }

    // 특정 유저객체 여부 확인 후 삭제. 불값 반환
    @Override
    public boolean deleteUser(UUID id) {
        if (usersMap.containsKey(id) == false) {
            return false;
        }
        usersMap.remove(id);
        return true;
    }

    // 전달받은 유저객체 null 여부 확인 후 유저 해쉬맵에 추가.
    @Override
    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }
        usersMap.put(user.getId(), user);
        return true;
    }

    @Override
    public boolean isUserExist(UUID userId) {
        if (usersMap.containsKey(userId) == false) {
            return false;
        }
        return true;
    }

}
