package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtos); //메시지 생성
    Message findById(UUID messageId); // 메시지 단일 조회
    List<Message> findAllByChannelId(UUID channelId); // 모든 메시지 조회
    Message update(UUID messageId,MessageUpdateDto messageUpdateDto); // 메시지 수정
    void delete(UUID messageId); //메시지 삭제
}
