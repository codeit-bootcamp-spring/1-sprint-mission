package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.HashMap;
import java.util.UUID;

public class FileUserStatusRepository implements UserStatusRepository {
    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String mainUserStatusRepository = "UserStatus\\mainOIUserStatusRepository";

    //todo IO핸들러에서 문제생길시 빈해시맵 반환하도록한다면 IO핸들러에서 문제생겨서 반환한 빈 해시맵에 작업하고 그대로 저장까지 하는 대참사가 일어날수도있음.
    // null or 빈 해시맵.. 뭘반환하는게 나을까 ? 우선 null반환하게했음.

    // I/O로 생성된 모든 유저스테이터스 객체가 담기는 해쉬맵 반환. IO핸들러에서 null반환했을시 빈 해시맵 반환.
    @Override
    public HashMap<UUID, UserStatus> getUserStatusMap(){
        HashMap<UUID, UserStatus> userStatusMap = (HashMap<UUID, UserStatus>) fileIOHandler.deserializeHashMap(mainUserStatusRepository);
        return userStatusMap != null ? userStatusMap : null;
    }

    //유저 id로 해당 유저의 유저스테이터스 객체 반환
    @Override
    public UserStatus getUserStatusById(UUID userId) {
        HashMap<UUID, UserStatus> userStatusMap = getUserStatusMap();
        return userStatusMap.get(userId);
    }

    //유저 스테이터스 객체 유저스테이터스맵에 추가. IO핸들러 저장까지 정상적으로 완료됐다면 true.
    @Override
    public boolean addUserStatus(UserStatus userStatus) {
        HashMap<UUID, UserStatus> userStatusMap = getUserStatusMap();
        userStatusMap.put(userStatus.getUserId(), userStatus);
        return fileIOHandler.serializeHashMap(userStatusMap, mainUserStatusRepository);
    }

    //유저스테이터스맵의 객체 삭제. IO핸들러 저장까지 정상적으로 완료됐다면 true.
    @Override
    public boolean deleteUserStatusById(UUID userId) {
        HashMap<UUID, UserStatus> userStatusMap = getUserStatusMap();
        if (userStatusMap.remove(userId) == null) {
            System.out.println("해당 userId의 userStatus가 Map에 존재하지 않습니다. ");
            return false;
        }
        return fileIOHandler.serializeHashMap(userStatusMap, mainUserStatusRepository);
    }

    //유저스테이터스맵의 해당 객체 존재 여부 반환.
    @Override
    public boolean isUserStatus(UUID UserId) {
        HashMap<UUID, UserStatus> userStatusMap = getUserStatusMap();
        return userStatusMap.containsKey(UserId);
    }

    //해당 유저스테이터스의 UpdatedAt와 UserStatusType(온라인/오프라인)을 업데이트한다. 직렬화까지 정상적으로 실행되었는지 여부 반환.
    @Override
    public boolean updateUserStatus(UUID userId) {
        HashMap<UUID, UserStatus> userStatusMap = getUserStatusMap();
        UserStatus userStatus = userStatusMap.get(userId);
        if (userStatus == null) {
            System.out.println("userId에 해당하는 userStatus 객체를 찾지 못했습니다.");
            return false;
        }
        userStatus.updateStatus();
        return fileIOHandler.serializeHashMap(userStatusMap, mainUserStatusRepository);
    }
}
