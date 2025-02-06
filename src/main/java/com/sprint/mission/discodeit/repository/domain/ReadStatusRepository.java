package com.sprint.mission.discodeit.repository.domain;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

//사용자가 언제 마지막으로 채널에 접속했는 지 기록
@Repository
public interface ReadStatusRepository {
    void save(ReadStatusDto readStatusDto);
    ReadStatus findById(UUID id);
    List<ReadStatus> findAll();
    List<ReadStatus> findAllByUserId(UUID userId); //하나의 유저는 다수의 채널 정보 검색 가능
    List<ReadStatus> findAllByChannelId(UUID channelId); //하나의 채널은 다수의 유저 정보 가지고 있음
    void delete(UUID id);
    void deleteByUserId(UUID userId); //유저 아이디를 통한 채널 정보 통합 삭제
    void deleteByChannelId(UUID id);
    void update(ReadStatusDto readStatusDto); //유저가 채널에 방문했을 떄 방문한 시각 업데이트


}
