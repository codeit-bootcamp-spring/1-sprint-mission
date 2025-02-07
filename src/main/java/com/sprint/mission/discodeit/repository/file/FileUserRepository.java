package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
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
    public HashMap<UUID, User> getUsersMap() {

        return (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
    }

    // 특정 유저객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public User getUser(UUID userId) {
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        if (usersMap.containsKey(userId) == false) {
            return null;
        }
        return usersMap.get(userId);
    }

    // 특정 유저객체 존재여부 확인 후 삭제
    @Override
    public boolean deleteUser(UUID userId) {
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        if (usersMap.containsKey(userId) == false) {
            return false;
        }
        usersMap.remove(userId);
        fileIOHandler.serializeHashMap(usersMap, mainUserRepository);
        return true;
    }

    // 전달받은 유저객체 null 여부 확인 후 유저 해쉬맵에 추가.
    @Override
    public boolean addUser(User user) {
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        if (user == null) {
            return false;
        }
        usersMap.put(user.getId(), user);
        fileIOHandler.serializeHashMap(usersMap, mainUserRepository);
        return true;
    }

    @Override
    public boolean isUserExist(UUID userId) {
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        if (usersMap.containsKey(userId) == false) {
            return false;
        }
        return true;
    }


}
