package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
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
    public List<Message> findAllByChannelId(UUID channelId) {
        return this.messageData.values().stream().filter(message -> message.getChannelId().equals(channelId)).toList();
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
    @Override
    public void deleteAllByChannelId(UUID channelId) {
        this.findAllByChannelId(channelId)
                .forEach(message -> this.deleteById(message.getId()));
    }

}
