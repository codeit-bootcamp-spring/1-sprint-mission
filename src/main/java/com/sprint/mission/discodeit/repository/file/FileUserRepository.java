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

    // I/O로 생성된 모든 유저 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, User> getUsersMap(){
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        return usersMap;
    }

    // 특정 유저객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public User getUserById(UUID userId){
        HashMap<UUID, User> usersMap = getUsersMap();
        return usersMap.get(userId);
    }

    // 특정 유저객체 존재여부 확인 후 삭제
    @Override
    public boolean deleteUser(UUID userId){
        HashMap<UUID, User> usersMap = getUsersMap();
        usersMap.remove(userId);
        return fileIOHandler.serializeHashMap(usersMap, mainUserRepository);
    }

    // 전달받은 유저객체 null 여부 확인 후 유저 해쉬맵에 추가.
    @Override
    public boolean saveUser(User user) {
        if (user == null) {
            System.out.println("유저가 null인 상태로, 유저를 저장하지 못했습니다.");
            return false;
        }
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        usersMap.put(user.getId(), user);
        return fileIOHandler.serializeHashMap(usersMap, mainUserRepository);
    }

    //해당 UUID를 가진 유저 존재여부 확인해서 반환
    @Override
    public boolean isUserExistByUUID(UUID userId){
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        return usersMap.containsKey(userId);
    }

    //해당 이름을 가진 유저 존재여부 확인해서 반환
    @Override
    public boolean isUserExistByName(String userName){
        HashMap<UUID, User> usersMap = getUsersMap();
        return usersMap.values().stream().anyMatch(user -> user.getUserName().equals(userName));
    }

    //해당 이메일을 가진 유저 존재여부 확인해서 반환
    @Override
    public boolean isUserExistByEmail(String email){
        HashMap<UUID, User> usersMap = getUsersMap();
        return usersMap.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    //유저 로그인 시도시 유저이름과 비밀번호가 동일한 유저객체에 포함되어있는지 여부를 확인. 그렇다면 true.
    @Override
    public boolean validateUserToLogin(String userName, String password){
        HashMap<UUID, User> usersMap = getUsersMap();
        User user = usersMap.values().stream().filter(_user ->_user.getUserName().equals(userName)||_user.getPassword().equals(password)).findFirst().orElse(null);
        if (user == null) { System.out.println("유저이름과 비밀번호가 일치하는 유저객체가 없습니다."); return false;}
        return true;
    }
}
