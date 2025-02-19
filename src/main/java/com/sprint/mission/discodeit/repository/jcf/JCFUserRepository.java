package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.HashMap;
import java.util.UUID;

@ConditionalOnProperty(name = "app.user-repository", havingValue = "jcf")
public class JCFUserRepository implements UserRepository {

    // 유저 객체가 담기는 해쉬맵
    private static final HashMap<UUID, User> usersMap = new HashMap<UUID, User>();

    // 유저 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, User> getUsersMap(){
        return usersMap;
    }

    // 특정 유저객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public User getUserById(UUID userId){
        return usersMap.get(userId);
    }

    // 특정 유저객체 존재여부 확인 후 삭제
    @Override
    public boolean deleteUser(UUID userId){
        //해시맵은 존재하는 key를 삭제하면 삭제한 요소를 반환하지만 없는 key를 삭제하면 null 반환.
        return usersMap.remove(userId)!=null;
    }

    // 전달받은 유저객체 null 여부 확인 후 유저 해쉬맵에 추가.
    @Override
    public boolean saveUser(User user){
        if (user == null) {System.out.println("파라미터에 전달된 유저가 null인 상태입니다. "); return false;}
        usersMap.put(user.getId(), user);
        return true;
    }

    //유저 존재하는지 UUID로 확인
    @Override
    public boolean isUserExistByUUID(UUID userId){
        return usersMap.containsKey(userId);
    }

    //유저 존재하는지 이름으로 확인
    @Override
    public boolean isUserExistByName(String userName){
        return usersMap.values().stream().anyMatch(user -> user.getUserName().equals(userName));
    }

    //유저 존재하는지 이메일로
    @Override
    public boolean isUserExistByEmail(String email){
        return usersMap.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    //유저 로그인 시도시 유저이름과 비밀번호가 동일한 유저객체에 포함되어있는지 여부를 확인해서 반환.
    @Override
    public boolean validateUserToLogin(String userName, String password){
        User user = usersMap.values().stream().filter(_user ->_user.getUserName().equals(userName)||_user.getPassword().equals(password)).findFirst().orElse(null);
        if (user == null) {System.out.println("유저이름과 비밀번호가 일치하는 유저객체가 없습니다."); return false;}
        return true;
    }
}
