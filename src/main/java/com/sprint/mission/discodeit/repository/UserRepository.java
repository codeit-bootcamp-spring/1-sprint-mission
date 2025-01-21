package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;

public interface UserRepository {
    void saveAll(List<User> users);          // 모든 사용자 저장
    List<User> loadAll();                    // 모든 사용자 로드
    void reset();                            // 데이터 초기화

}
