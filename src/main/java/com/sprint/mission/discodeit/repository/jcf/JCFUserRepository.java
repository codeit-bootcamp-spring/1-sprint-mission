package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.data.UserData;

import java.util.Map;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {

    private final UserData userData = UserData.getInstance();

    // 데이터 저장
    @Override
    public void save(User user) {

        userData.put(user);
    }

    // 데이터 가져오기
    @Override
    public Map<UUID, User> load() {

        return userData.load();
    }

    // 데이터 삭제
    @Override
    public void delete(UUID id) {

        userData.delete(id);
    }
}