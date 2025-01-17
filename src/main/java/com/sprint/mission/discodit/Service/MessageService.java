package com.sprint.mission.discodit.Service;

import com.sprint.mission.discodit.Entity.Message;
import com.sprint.mission.discodit.Entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    //도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요

    UUID createMessage(UUID id, String content);

    UUID createMessage(UUID id, UUID sender, String content);

    public Message getMessage(UUID id);

    List<Message> getMessageByUserId(UUID userId);

    public List<Message> getMessages();
    public void setMessage(UUID id, String content);
    public void deleteMessage(UUID id);
}
