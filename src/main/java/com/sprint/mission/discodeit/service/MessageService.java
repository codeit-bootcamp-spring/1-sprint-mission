package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    // 메시지 유효성 확인 -> 메시지 content 내용이 비었는지만 확인

    // 메시지 생성
    MessageResponseDto create(MessageRequestDto messageReqDTO, List<MultipartFile> attachments) throws IOException;

    // 조회
    // 단일 조회
    MessageResponseDto find(UUID messageId);

    // 전체 조회
    List<MessageResponseDto> findAllByChannelId(UUID channelId);   // 채널의 모든 메시지

    // 페이징 포함
    PageResponse<MessageResponseDto> findMessagesByChannelId(UUID channelId, UUID cursor);

    // 메시지 수정
    MessageResponseDto update(UUID messageId, String content, List<MultipartFile> attachments) throws IOException;

    // 메시지 삭제
    MessageResponseDto delete(UUID messageId);
}
