package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.UUID;

public class JCFUserService implements UserService{
    JCFUserRepository JCFUserRepositoryInstance = JCFUserRepository.getInstance();

    private static final HashMap<UUID, User> usersMap = new HashMap<UUID, User>();

    //생성자 접근 불가능하도록 함.
    private JCFUserService() {
    }

    //모든 유저객체가 담기는 해쉬맵 싱글톤 객체 'usersMap' 생성. LazyHolder 방식으로 스레드세이프 보장
    private static class JCFUserServiceHolder {
        private static final JCFUserService INSTANCE = new JCFUserService();
    }

    public static JCFUserService getInstance() { //외부에서 호출 가능한 싱글톤 인스턴스.
        return JCFUserServiceHolder.INSTANCE;
    }

    //모든 유저 관리 맵 'usersMap' 반환
    @Override
    public HashMap<UUID, User> getUsersMap() {
        return usersMap;
    }

    //유저 생성. 'usersMap'에 uuid-유저객체 주소 넣어줌. 성공하면 uuid, 실패하면 null 반환
    @Override
    public UUID createUser(String userName) {
        if (userName == null){
            return null;
        }
        User newUser = new User(userName);
        getUsersMap().put(newUser.getId(), newUser);
        return newUser.getId();
    }

    //유저 존재여부 확인
    @Override
    public boolean isUserExist(UUID userId) {
        if (userId == null){
            return false;
        }
        return JCFUserRepositoryInstance.isUserExist(userId);
    }


    //UUID를 통해 유저 객체를 찾아 삭제. 성공여부 리턴
    @Override
    public boolean deleteUser(UUID userId) {
        if (userId == null){
            return false;
        }
        JCFUserRepositoryInstance.deleteUser(userId);
        return true;
    }

    //유저명 변경. 성공여부 반환
    @Override
    public boolean changeUserName(UUID userId, String newName) {
        if (userId == null||newName == null){
            return false;
        }
        JCFUserRepositoryInstance.getUser(userId).setUserName(newName);
        return true;

    }
}
