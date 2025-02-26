package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReadStatusRepository {

  // 저장
  ReadStatus save(ReadStatus readStatus);

  // 유저 아이디로 검색해서 ReadStatus 얻기
  List<ReadStatus> findAllReadStatusByUserId(UUID userId);

  // 채널 아이디로 검색해서 ReadStatus 얻기
  ReadStatus findByChannelId(UUID channelId);

  // 모든 ReadStatus 얻기
  Map<UUID, ReadStatus> findAll();

  // 삭제
  void remove(UUID channelId);

  // 채널 메세지 수신 정보 수정
  ReadStatus update(UUID channelId);

  ReadStatus find(UUID id);

  // 테스트용

  void clearData();

  void resetData();
}
