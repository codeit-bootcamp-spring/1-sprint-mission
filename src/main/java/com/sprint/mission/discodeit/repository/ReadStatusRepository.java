package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

public interface ReadStatusRepository {
    // 읽었는지 아닌지 상태 저장
    boolean save(ReadStatus readStatus);

    // 읽음 상태 수정
    boolean updateOne(ReadStatus readStatus);
}
