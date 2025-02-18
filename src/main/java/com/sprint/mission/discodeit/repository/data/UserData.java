package com.sprint.mission.discodeit.repository.data;

import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// User 데이터만 관리하는 일급 컬렉션
public class UserData {

    // 싱글톤 패턴 사용 시 Stateful하면 여러 스레드 작동 시 동기화 문제 발생 가능 -> Stateless 상태 유지 필요
        // Stateful : 여러 클래스가 접근해서 변경할 수 있는 필드(멤버 변수)가 있는 것
    // ConcurrentHashMap의 경우 put이나 remove 등의 값 수정 메서드의 경우 synchronized 키워드를 사용해 Thread-safe를 보장
    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    private static UserData userData;

    private UserData() {}

    public static UserData getInstance() {

        if (userData == null) {
            userData = new UserData();
        }

        return userData;
    }
    
    public void put(User user) {

        users.put(user.getId(), user);
    }

    public Map<UUID, User> load() {

        return users;
    }

    public void delete(UUID id) {

        users.remove(id);
    }
}