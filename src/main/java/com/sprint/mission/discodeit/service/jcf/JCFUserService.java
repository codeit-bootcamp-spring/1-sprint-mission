package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    //key 값으로 value User 객체의 UUID 값을 할당해줌
    private final Map<UUID, User> data = new HashMap<>() ;


    @Override
    public void userServiceAdd(User user) {
        data.put(user.getuuID(), user);
    }

    // 유저 서비스 목록에서 특정단일 사용자 객체 반환
    @Override
    public User getUser(UUID id) {
        // HashMap에서 주어진 id로 사용자 조회
        return data.get(id);  // 해당 id에 맞는 사용자 객체 반환, 없으면 null 반환
    }


    @Override
    //모든 유저 가져오기
    public HashMap<UUID, User> getAllUsers() {
        return new HashMap<>(data);
    }

    // 사용자 수정
    @Override
    public void updateUser(UUID uuId, String email, String id, String password) {
        // 리스트에서 주어진 id를 가진 사용자를 찾아서 수정
        for (User user : data.values()) {
            if (user.getuuID().equals(uuId)) {
                // user 객체를 찾아서 update() 메서드를 호출하여 이름과 이메일을 변경
                user.update(email, id, password);
                break;
            }
        }
    }


    // 사용자 삭제 메서드 (deleteUser)
    @Override
    public void deleteUser(User user) {
        data.remove(user.getuuID());  // HashMap에서 id를 기준으로 해당 사용자 제거
    }
}
