package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserRepository implements UserRepository {

    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String mainUserRepository = "User\\mainOIUserRepository";


    /* 외부에서 생성자 접근 불가
    private FileUserRepository() {
        fileIOHandler.serializeHashMap(new HashMap<UUID, User>(), mainUserRepository);
    }
    // 레포지토리 객체 LazyHolder 싱글톤 구현.
    private static class FileUserRepositoryHolder {
        private static final FileUserRepository INSTANCE = new FileUserRepository();
    }
    // 외부에서 호출 가능한 싱글톤 인스턴스.
    public static FileUserRepository getInstance() {
        return FileUserRepository.FileUserRepositoryHolder.INSTANCE;
    }*/




    // I/O로 생성된 모든 유저 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, User> getUsersMap() throws Exception{
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        return usersMap;
    }

    // 특정 유저객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public User getUser(UUID userId) throws Exception{
        HashMap<UUID, User> usersMap = getUsersMap();
        return Optional.ofNullable(usersMap.get(userId)).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    // 특정 유저객체 존재여부 확인 후 삭제
    @Override
    public boolean deleteUser(UUID userId) throws Exception {
        HashMap<UUID, User> usersMap = getUsersMap();
        if (usersMap.containsKey(userId) == false) {
            throw new NoSuchElementException("User is not found");
        }
        usersMap.remove(userId);
        fileIOHandler.serializeHashMap(usersMap, mainUserRepository);
        return true;
    }

    // 전달받은 유저객체 null 여부 확인 후 유저 해쉬맵에 추가.
    @Override
    public boolean saveUser(User user) throws Exception {
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        if (user == null) {
            throw new NullPointerException("Given User Object is null.");
        }
        usersMap.put(user.getId(), user);
        fileIOHandler.serializeHashMap(usersMap, mainUserRepository);
        return true;
    }

    @Override
    public boolean isUserExistByUUID(UUID userId) throws Exception{
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        if (usersMap.containsKey(userId) == false) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isUserExistByUserName(String userName) throws Exception{
        HashMap<UUID, User> usersMap = getUsersMap();
        boolean isMatch = usersMap.values().stream().anyMatch(user -> user.getUserName().equals(userName));
        return isMatch;
    }

    @Override
    public boolean isUserExistByEmail(String email) throws Exception{
        HashMap<UUID, User> usersMap = getUsersMap();
        boolean isMatch = usersMap.values().stream().anyMatch(user -> user.getEmail().equals(email));
        return isMatch;
    }

}
