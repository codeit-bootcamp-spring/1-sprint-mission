package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;

public interface MessageService {


    //생성
    Message create(CreateMessageDto createMessageDto);

    //모두 읽기
    List<Message> findAll();

    //읽기
    Message findById(String messageId);

    //다건 조회 - 내용
    List<Message> findAllContainsContent(String content);

    //다건 조회 - 작성자
    List<Message> findAllBySenderId(String senderId);

    //다건 조회 - 날짜
    List<Message> findAllByCreatedAt(Instant createdAt);

    //다건 조회 - 특정 채널
    List<Message> findAllByChannelId(String channelId);

    //수정
    Message updateMessage(String messageId, UpdateMessageDto updateMessageDto);

    //삭제
    boolean delete(String messageId);
}
