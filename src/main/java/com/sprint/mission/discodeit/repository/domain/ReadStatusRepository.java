package com.sprint.mission.discodeit.repository.domain;

import com.sprint.mission.discodeit.domain.ReadStatus;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

//사용자가 언제 마지막으로 채널에 접속했는 지 기록
@Repository
public interface ReadStatusRepository {
    void save(ReadStatus readStatus);
    ReadStatus getReadStatusById(UUID id);
    List<ReadStatus> getAllReadStatus();
    List<ReadStatus> getReadStatusByUserId(UUID userId); //하나의 유저는 다수의 채널 정보 검색 가능
    void deleteReadStatusById(UUID id);
    void deleteReadStatusByUserId(UUID userId); //유저 아이디를 통한 채널 정보 통합 삭제
    void updateReadStatus(UUID id, Instant now); //유저가 채널에 방문했을 떄 방문한 시각 업데이트

}
