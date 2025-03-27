package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    // 생성
    MessageDto create(CreateMessageRequestDto createMessageRequestDto) throws IOException;

    // 읽기
    MessageDto find(UUID id);

    // 모두 읽기
    List<MessageDto> findAllByChannelId(UUID channelId);
    List<MessageDto> findAllByUserId(UUID userId);

    // 수정
    MessageDto updateContent(UpdateMessageRequestDto updateMessageRequestDto);
    
    // 삭제
    void delete(UUID id);
}