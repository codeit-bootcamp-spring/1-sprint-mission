package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.FindMessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    // 생성
    FindMessageResponseDto create(CreateMessageRequestDto createMessageRequestDto) throws IOException;

    // 읽기
    FindMessageResponseDto find(UUID id);

    // 모두 읽기
    List<FindMessageResponseDto> findAllByChannelId(UUID channelId);
    List<FindMessageResponseDto> findAllByUserId(UUID userId);

    // 수정
    FindMessageResponseDto updateContext(UpdateMessageRequestDto updateMessageRequestDto);
    
    // 삭제
    void delete(UUID id);

    // 메시지 존재 여부 확인
    void messageIsExist(UUID id);
}