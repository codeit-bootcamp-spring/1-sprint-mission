package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;

public interface MessageService {


    //생성
    Message create(String content, String channelId, String userId);

    //모두 읽기
    List<Message> getMessages();

    //읽기
    Message getMessageByUUID(String messageId);

    //다건 조회 - 내용
    List<Message> getMessageByContent(String content);

    //다건 조회 - 작성자
    List<Message> getMessageBySenderId(String senderId);

    //다건 조회 - 날짜
    List<Message> getMessageByCreatedAt(Instant createdAt);

    //다건 조회 - 특정 채널
    List<Message> getMessagesByChannel(Channel channel);

    //수정
    Message updateMessage(String messageId, String newContent, Instant updatedAt);

    //삭제
    boolean deleteMessage(Message message);
}
