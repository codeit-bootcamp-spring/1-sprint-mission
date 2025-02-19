package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.read.ReadStatus;

import java.util.Map;
import java.util.UUID;

public interface ReadStatusRepository {

    // 저장
    ReadStatus save(ReadStatus readStatus);

    // 유저 아이디로 검색해서 ReadStatus 얻기
    ReadStatus findByUserId(UUID userId);

    // 채널 아이디로 검색해서 ReadStatus 얻기
    ReadStatus findByChannelId(UUID channelId);

    // 모든 ReadStatus 얻기
    Map<UUID, ReadStatus> findAll();

    // 삭제
    void remove(UUID channelId);
}
