package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.UUID;

public class JCFUserService implements UserService{
    JCFUserRepository JCFUserRepositoryInstance = JCFUserRepository.getInstance();
    FileIOHandler IOHandlerInstance = FileIOHandler.getInstance();

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
        return JCFUserRepositoryInstance.getUsersMap();
    }

    //해당 유저 리턴
    @Override
    public User getUserById(UUID userId) {
        if (userId == null || JCFUserRepositoryInstance.isUserExist(userId)==false){
            System.out.println("유저 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return JCFUserRepositoryInstance.getUser(userId);
    }

    //해당 uuid의 유저 찾아 반환
    @Override
    public String getUserNameById(UUID userId) {
        if (userId == null || JCFUserRepositoryInstance.isUserExist(userId)==false){
            System.out.println("유저이름 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return JCFUserRepositoryInstance.getUser(userId).getUserName();
    }

    //유저 생성. 'usersMap'에 uuid-유저객체 주소 넣어줌.
    @Override
    public UUID createUser(String userName) {
        if (userName == null){
            System.out.println("유저 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        User newUser = new User(userName);
        JCFUserRepositoryInstance.addUser(newUser);
        System.out.println(userName + "유저 생성 성공!");
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


    //UUID를 통해 유저 객체를 찾아 삭제.
    @Override
    public boolean deleteUser(UUID userId) {
        if (userId == null || JCFUserRepositoryInstance.isUserExist(userId)==false){
            System.out.println("유저 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        JCFUserRepositoryInstance.deleteUser(userId);
        System.out.println("유저 삭제 성공!");
        return true;
    }

    //유저명 변경.
    @Override
    public boolean changeUserName(UUID userId, String newName) {
        if (userId == null || newName == null || JCFUserRepositoryInstance.isUserExist(userId)==false){
            System.out.println("유저 이름 변경 실패. 입력값을 확인해주세요.");
            return false;
        }
        JCFUserRepositoryInstance.getUser(userId).setUserName(newName);
        System.out.println("유저 이름 변경 성공!");
        return true;
    }

    //현재 유저맵에 있는 객체정보 직렬화
    public boolean exportUsersMap(String fileName) {
        if (fileName==null || JCFUserRepositoryInstance.getUsersMap().isEmpty()){
            System.out.println("유저 직렬화 실패. 입력값을 확인해주세요.");
            return false;
        }
        IOHandlerInstance.serializeHashMap(JCFUserRepositoryInstance.getUsersMap(), fileName);
        System.out.println("유저 직렬화 성공!");
        return true;
    }
}
