package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;

public interface MessageService {


    //생성
    Message create(String content, String channelId, String userId);

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
    List<Message> findAllByChannelId(Channel channel);

    //수정
    Message updateMessage(String messageId, String newContent, Instant updatedAt);

    //삭제
    boolean deleteMessage(Message message);
}
