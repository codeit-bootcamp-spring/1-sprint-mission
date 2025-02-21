package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.*;


public class JCFMessageRepository implements MessageRepository {
    //데이터 저장
    private final Map<UUID,Message> messageData;
    public JCFMessageRepository() {
        this.messageData = new HashMap<>();
    }
    //생성,수정
    @Override
    public Message save(Message message) {
        messageData.put(message.getId(),message);
        return message;
    }

    //단일조회
    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(this.messageData.get(messageId));
    }

    //다중조회
    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageData.values());
    }

    @Override
    public boolean existsById(UUID messageId) {
        return messageData.containsKey(messageId);
    }

    //삭제
    @Override
    public void deleteById(UUID messageId) {
        messageData.remove(messageId);
    }


}
