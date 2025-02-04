package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private static JCFUserService instance; // 싱글톤 인스턴스
    private final Map<UUID, User> data = new HashMap<>(); // 사용자 데이터를 저장할 Map

    // private 생성자: 외부에서 객체 생성 불가
    private JCFUserService() {}

    // 싱글톤 인스턴스를 반환하는 메서드
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
        if (data.containsKey(user.getId())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다: " + user.getUsername());
        }
        data.put(user.getId(), user); // 사용자 데이터를 저장
    }

    @Override
    public Optional<User> read(UUID id) {
        return Optional.ofNullable(data.get(id)); // 사용자 ID로 데이터 조회
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values()); // 저장된 모든 사용자 반환
    }

    @Override
    public void update(UUID id, User user) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + id);
        }
        data.put(id, user); // 사용자 정보 업데이트
    }

    @Override
    public void delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("삭제할 사용자가 존재하지 않습니다: " + id);
        }
        data.remove(id); // 사용자 데이터 삭제
    }
}
