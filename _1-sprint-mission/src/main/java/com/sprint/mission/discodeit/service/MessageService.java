package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageDto messageDto); //메시지 생성
    Message findById(UUID messageId); // 메시지 단일 조회
    List<Message> findAllByChannelId(UUID channelId); // 모든 메시지 조회
    Message update(UpdateMessageDto updateMessageDto); // 메시지 수정
    void delete(UUID messageId); //메시지 삭제
}
