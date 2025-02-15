package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;


import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserRepository implements UserRepository {

    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String mainUserRepository = "User\\mainOIUserRepository";

    // I/O로 생성된 모든 유저 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, User> getUsersMap() throws Exception{
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        return usersMap;
    }

    // 특정 유저객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public User getUserById(UUID userId) throws Exception{
        HashMap<UUID, User> usersMap = getUsersMap();
        return Optional.ofNullable(usersMap.get(userId)).orElseThrow(() -> new NoSuchElementException("해당 uuid를 가진 유저가 존재하지 않습니다."));
    }

    // 특정 유저객체 존재여부 확인 후 삭제
    @Override
    public boolean deleteUser(UUID userId) throws Exception {
        HashMap<UUID, User> usersMap = getUsersMap();
        //해시맵은 존재하는 key를 삭제하면 삭제한 요소를 반환하지만 없는 key를 삭제하면 null 반환.
        if (usersMap.remove(userId)==null){
            throw new NoSuchElementException("해당 uuid를 가진 유저가 존재하지 않습니다.");
        };
        fileIOHandler.serializeHashMap(usersMap, mainUserRepository);
        return true;
    }

    // 전달받은 유저객체 null 여부 확인 후 유저 해쉬맵에 추가.
    @Override
    public boolean saveUser(User user) throws Exception {
        if (user == null) {
            throw new NullPointerException("유저가 null인 상태로, 유저를 저장하지 못했습니다.");
        }
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        usersMap.put(user.getId(), user);
        fileIOHandler.serializeHashMap(usersMap, mainUserRepository);
        return true;
    }

    //해당 UUID를 가진 유저 존재여부 확인해서 반환
    @Override
    public boolean isUserExistByUUID(UUID userId) throws Exception{
        HashMap<UUID, User> usersMap = (HashMap<UUID, User>) fileIOHandler.deserializeHashMap(mainUserRepository);
        if (usersMap.containsKey(userId) == false) {
            return false;
        }
        return true;
    }

    //해당 이름을 가진 유저 존재여부 확인해서 반환
    @Override
    public boolean isUserExistByName(String userName) throws Exception{
        HashMap<UUID, User> usersMap = getUsersMap();
        boolean isMatch = usersMap.values().stream().anyMatch(user -> user.getUserName().equals(userName));
        return isMatch;
    }

    //해당 이메일을 가진 유저 존재여부 확인해서 반환
    @Override
    public boolean isUserExistByEmail(String email) throws Exception{
        HashMap<UUID, User> usersMap = getUsersMap();
        boolean isMatch = usersMap.values().stream().anyMatch(user -> user.getEmail().equals(email));
        return isMatch;
    }

    //유저 로그인 시도시 유저이름과 비밀번호가 동일한 유저객체에 포함되어있는지 여부를 확인. 그렇다면 true.
    //todo 예외를 너무 남용하고있는게아닌가? 로그인이 중지된 이유를 전달하고싶어서 우선 false를 반환하는 대신 예외를 던지는 식으로 처리했지만 뭐가 좋은 방법인지는 확인 필요.
    @Override
    public boolean validateUserToLogin(String userName, String password) throws Exception{
        HashMap<UUID, User> usersMap = getUsersMap();
        User user = usersMap.values().stream().filter(_user ->_user.getUserName().equals(userName)).findAny().orElseThrow(() -> new NoSuchElementException("해당 이름을 가진 유저가 존재하지 않습니다. "));
        if (user.getPassword().equals(password)) {
            return true;
        }else{
            throw new Exception("비밀번호가 일치하지 않습니다. ");
        }
    }

}
