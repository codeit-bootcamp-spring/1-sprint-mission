package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDTO;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    // 메시지 유효성 확인 -> 메시지 content 내용이 비었는지만 확인

    // 메시지 생성
    Long create(MessageDTO.request messageReqDTO);

    // 조회
    // 단일 조회
    MessageDTO.response find(Long id);

    MessageDTO.response find(UUID uuid);

    // 전체 조회
    List<MessageDTO.response> findAllByChannelId(UUID channelUUID);   // 채널의 모든 메시지

    // 메시지 수정
    boolean update(MessageDTO.update updateDTO);

    // 메시지 삭제
    Long delete(Long id);

    Long delete(UUID uuid);
}
