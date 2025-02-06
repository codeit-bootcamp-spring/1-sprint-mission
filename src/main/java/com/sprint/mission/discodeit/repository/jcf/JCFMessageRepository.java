package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
//implements MessageRepository
@Repository
@Profile("Jcf")
public class JCFMessageRepository implements MessageRepository  {
    private final Map<UUID, Message> messageMap;

    public JCFMessageRepository() {
        this.messageMap = new HashMap<>();
    }

    public UUID createMessage(UUID sender, String content) {
        Message message = new Message(sender, content);
        messageMap.put(message.getId(), message);
        return message.getId();
    }

    public UUID createMessage(UUID id, UUID sender, String content) {
        Message message = new Message(sender, content);
        messageMap.put(id, message);
        return message.getId();
    }

    public Message getMessage(UUID id) {
        return messageMap.get(id);
    }

    public List<Message> getMessagesByUserId(UUID userId) {
        return getMessages().stream().filter(s -> s.getSenderId().equals(userId)).collect(Collectors.toList());
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messageMap.values());
    }

    public void updateMessage(UUID id, String content) {
        if (messageMap.containsKey(id)) {
            Message message = messageMap.get(id);
            message.update(content);
        }else {
            System.out.println("메시지를 찾을 수 없습니다.");
        }
    }

    public void deleteMessage(UUID id) {
        if (messageMap.containsKey(id)) {
            messageMap.remove(id);
        }else {
            System.out.println("메시지를 찾을 수 없습니다.");
        }
    }

    @Override
    public UUID save(UUID sender, String content) {
        Message message = new Message(sender, content);
        messageMap.put(message.getId(), message);
        return message.getId();
    }

    @Override
    public Message findMessageById(UUID id) {
        return messageMap.get(id);
    }

    @Override
    public List<Message> findMessagesById(UUID id) {
        return getMessages().stream().filter(s -> s.getSenderId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public List<Message> findAll() {
        List<Message> collect = messageMap.values().stream().toList();
        return new ArrayList<>(collect);
    }

    @Override
    public boolean delete(UUID messageId) {
        if(messageMap.containsKey(messageId)){
            //Message message = messageMap.get(messageId);
            //initializeMessage(message);
            messageMap.replace(messageId, new Message());
            messageMap.remove(messageId);
            return true;
        }else {
            System.out.println("메시지를 찾을 수 없습니다.");
            return false;
        }
    }

    @Override
    public void update(UUID id, String content) {
        Message message = findMessageById(id);
        message.update(content);
        messageMap.replace(message.getId(), message);
        System.out.println("업데이트 메시지");
        System.out.println("message = " + findMessageById(id));
    }

    public void initializeMessage(Message message) {
        message = new Message();
    }
}

