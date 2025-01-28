package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFUserService implements UserService {

    private static JCFUserService instance; // 싱글톤 인스턴스
    private final Map<UUID, User> data = new HashMap<>(); // 사용자 데이터를 저장할 Map

    // private 생성자: 싱글톤 패턴
    private JCFUserService() {}

    // 싱글톤 인스턴스 제공 메서드
    public static JCFUserService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFUserService();
                }
            }
        }
        return instance;
    }

    @Override
    public void create(User user) {
        data.put(user.getId(), user); // 사용자 데이터 저장
    }

    @Override
    public Optional<User> read(UUID id) {
        return Optional.ofNullable(data.get(id)); // 사용자 조회 (Optional로 감싸서 반환)
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values()); // 저장된 모든 사용자 리스트 반환
    }

    @Override
    public void update(UUID id, User user) {
        // ID가 존재하는 경우 사용자 정보 업데이트
        if (data.containsKey(id)) {
            data.put(id, user);
        } else {
            throw new IllegalArgumentException("User not found: " + id); // 존재하지 않으면 예외 발생
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id); // 사용자 데이터 삭제
    }
}
