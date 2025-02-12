package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.NoSuchElementException;
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
    public HashMap<UUID, User> getUsersMap() throws Exception {
        return usersMap;
    }

    // 특정 유저객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public User getUser(UUID userId) throws NoSuchElementException {
        if (usersMap.containsKey(userId) == false) {
            throw new NoSuchElementException("User not found");
        }
        return usersMap.get(userId);
    }

    // 특정 유저객체 존재여부 확인 후 삭제
    @Override
    public boolean deleteUser(UUID id) throws Exception {
        if (usersMap.containsKey(id) == false) {
            throw new RuntimeException("User is not found");
        }
        usersMap.remove(id);
        return true;
    }

    // 전달받은 유저객체 null 여부 확인 후 유저 해쉬맵에 추가.
    @Override
    public boolean saveUser(User user) throws Exception{
        if (user == null) {
            throw new RuntimeException("User is null");
        }
        usersMap.put(user.getId(), user);
        return true;
    }

    //유저 존재하는지 UUID로 확인
    @Override
    public boolean isUserExistByUUID(UUID userId) throws Exception {
        if (usersMap.containsKey(userId) == false) {
            return false;
        }
        return true;
    }

    //유저 존재하는지 이름으로 확인
    @Override
    public boolean isUserExistByUserName(String userName) throws Exception{
        boolean isMatch = usersMap.values().stream().anyMatch(user -> user.getUserName().equals(userName));
        return isMatch;
    }

    @Override
    public boolean isUserExistByEmail(String email) throws Exception{
        boolean isMatch = usersMap.values().stream().anyMatch(user -> user.getEmail().equals(email));
        return isMatch;
    }
}
