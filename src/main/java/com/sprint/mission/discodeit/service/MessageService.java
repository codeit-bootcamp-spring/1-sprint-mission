package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CommonDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    // 메시지 유효성 확인 -> 메시지 content 내용이 비었는지만 확인

    // 메시지 생성
    CommonDTO.idResponse create(MessageDTO.request messageReqDTO, List<MultipartFile> attachments);

    // 조회
    // 단일 조회
    MessageDTO.response find(Long id);

    MessageDTO.response find(UUID uuid);

    // 전체 조회
    List<MessageDTO.response> findAllByChannelId(UUID channelUUID);   // 채널의 모든 메시지

    // 메시지 수정
    CommonDTO.idResponse update(Long messageId, String content, List<MultipartFile> attachments);

    // 메시지 삭제
    CommonDTO.idResponse delete(Long id);

    CommonDTO.idResponse delete(UUID uuid);
}
