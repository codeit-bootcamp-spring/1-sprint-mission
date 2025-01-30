package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
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

    //해당 채널 리턴
    @Override
    public User getUserById(UUID userId) {
        if (userId == null || FileUserRepositoryInstance.isUserExist(userId)==false){
            System.out.println("유저 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return FileUserRepositoryInstance.getUser(userId);
    }

    @Override
    public String getUserNameById(UUID userId) {
        if (userId == null || FileUserRepositoryInstance.isUserExist(userId)==false){
            System.out.println("유저이름 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return FileUserRepositoryInstance.getUser(userId).getUserName();
    }

    //유저 생성. 'UsersMap'에 uuid-유저객체 주소 넣어줌.
    @Override
    public UUID createUser(String userName) {
        if (userName == null){
            System.out.println("유저 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        User newUser = new User(userName);
        FileUserRepositoryInstance.addUser(newUser);
        System.out.println(userName + "유저 생성 성공!");
        return newUser.getId();
    }

    //유저 존재여부 확인.
    @Override
    public boolean isUserExist(UUID userId) {
        if (userId == null){
            return false;
        }
        return FileUserRepositoryInstance.isUserExist(userId);
    }

    //UUID를 통해 유저 객체를 찾아 삭제. 성공여부 리턴
    @Override
    public boolean deleteUser(UUID userId) {
        if (userId == null|| FileUserRepositoryInstance.isUserExist(userId)==false){
            System.out.println("유저 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        FileUserRepositoryInstance.deleteUser(userId);
        System.out.println("유저 삭제 성공!");
        return true;
    }

    //유저명 변경. 성공여부 반환
    @Override
    public boolean changeUserName(UUID userId, String newName) {
        if (userId == null || newName == null || FileUserRepositoryInstance.isUserExist(userId)==false){
            System.out.println("유저 이름 변경 실패. 입력값을 확인해주세요.");
            return false;
        }
        FileUserRepositoryInstance.getUser(userId).setUserName(newName);
        System.out.println("유저 이름 변경 성공!");
        return true;

    }

    //현재 유저맵에 있는 객체정보 직렬화
    public boolean exportUserMap(String fileName) {
        if (fileName==null || FileUserRepositoryInstance.getUsersMap().isEmpty()){
            System.out.println("유저 직렬화 실패. 입력값을 확인해주세요.");
            return false;
        }
        fileIOHandlerInstance.serializeHashMap(FileUserRepositoryInstance.getUsersMap(), fileName);
        System.out.println("유저 직렬화 성공!");
        return true;
    }

    //역직렬화로 불러온 유저맵을 검증하고 기존의 유저맵에 저장
    //다른 타입의 value가 역직렬화되어 들어왔을 경우를 고려하여 검증코드 추가 예정
    public boolean importUserMap(String fileName) {
        if (fileName==null){
            System.out.println("유저 불러오기 실패. 입력값을 확인해주세요.");
            return false;
        }
        HashMap<UUID, User> importedUserMap = (HashMap<UUID, User>) fileIOHandlerInstance.deserializeHashMap(fileName);
        if (importedUserMap==null || importedUserMap.isEmpty()){
            System.out.println("유저 불러오기 실패. 추가할 유저가 존재하지 않습니다.");
            return false;
        }
        FileUserRepositoryInstance.addUsers(importedUserMap);
        System.out.println("유저 불러오기 성공!");
        return true;
    }


}
