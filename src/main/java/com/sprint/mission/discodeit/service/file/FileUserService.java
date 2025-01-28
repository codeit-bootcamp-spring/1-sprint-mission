package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.UUID;

public class FileUserService implements UserService {
    FileIOHandler fileIOHandlerInstance = FileIOHandler.getInstance();
    FileUserRepository FileUserRepositoryInstance = FileUserRepository.getInstance();

    //생성자 접근 불가능하도록 함.
    private FileUserService() {
    }
    //LazyHolder 싱글톤
    private static class JCFUserServiceHolder {
        private static final FileUserService INSTANCE = new FileUserService();
    }
    //외부에서 접근 가능한 인스턴스
    public static FileUserService getInstance() { //외부에서 호출 가능한 싱글톤 인스턴스.
        return JCFUserServiceHolder.INSTANCE;
    }



    //모든 유저 관리 맵 'UsersMap' 반환
    @Override
    public HashMap<UUID, User> getUsersMap() {
        return FileUserRepositoryInstance.getUsersMap();
    }

    //유저 생성. 'UsersMap'에 uuid-유저객체 주소 넣어줌.
    @Override
    public UUID createUser(String userName) {
        if (userName == null){
            return null;
        }
        User newUser = new User(userName);
        FileUserRepositoryInstance.addUser(newUser);
        return newUser.getId();
    }
    //UUID를 통해 유저 객체를 찾아 삭제. 성공여부 리턴
    @Override
    public boolean deleteUser(UUID userId) {
        if (userId == null){
            return false;
        }
        FileUserRepositoryInstance.deleteUser(userId);
        return true;
    }

    //유저명 변경. 성공여부 반환
    @Override
    public boolean changeUserName(UUID userId, String newName) {
        if (userId == null || newName == null){
            return false;
        }
        FileUserRepositoryInstance.getUser(userId).setUserName(newName);
        return true;

    }

    @Override
    //레포지토리의 유저맵에서 해당 id get했을 때 그 반환값이 null인지 아닌지로 존재여부 확인.
    public boolean isUserExist(UUID userId) {
        if (userId == null){
            return false;
        }
        return FileUserRepositoryInstance.isUserExist(userId);
    }

    //현재 유저맵에 있는 객체정보 직렬화
    public boolean exportUserMap(String fileName) {
        if (fileName==null){
            return false;
        }
        fileIOHandlerInstance.serializeHashMap(FileUserRepositoryInstance.getUsersMap(), fileName);
        return true;
    }

    //역직렬화로 불러온 유저맵을 검증하고 기존의 유저맵에 저장
    //다른 타입의 value가 역직렬화되어 들어왔을 경우를 고려하여 검증코드 추가 예정
    public boolean importUserMap(String fileName) {
        if (fileName==null){
            return false;
        }
        HashMap<UUID, User> importedUserMap = (HashMap<UUID, User>) fileIOHandlerInstance.deserializeHashMap(fileName);
        if (importedUserMap==null || importedUserMap.isEmpty()){
            return false;
        }
        FileUserRepositoryInstance.addUsers(importedUserMap);
        return true;
    }


}
