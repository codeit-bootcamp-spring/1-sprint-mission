package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
@Repository
@Profile("File")
public class FileMessageRepository implements MessageRepository {
    private final String FILE_NAME = "C:\\Users\\ypd06\\codit\\files\\message.ser";
/*

    public FileMessageRepository() {
        this.messageMap = new HashMap<>();
    }

    @Override
    public UUID createMessage(UUID sender, String content) {
        Message message = new Message(sender, content);
        messageMap.put(message.getMessageId(), message);
        return message.getMessageId();
    }


    @Override
    public Message getMessage(UUID id) {
        Map<UUID, Message> messageMap = loadFromSer(FILE_NAME);
        if(!messageMap.containsKey(id)){
            return null;
        }
        return messageMap.get(id);
        //return this.messageMap.get(id);
    }

    @Override
    public List<Message> getMessageByUserId(UUID userId) {
        return getMessages().stream().filter(s -> s.getSender().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessages() {
        return new ArrayList<>(messageMap.values());
    }

    @Override
    public void setMessage(UUID id, String content) {
        if (messageMap.containsKey(id)) {
            Message message = messageMap.get(id);
            message.update(content);
        }else {
            System.out.println("메시지를 찾을 수 없습니다.");
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        if (messageMap.containsKey(id)) {
            messageMap.remove(id);
        }else {
            System.out.println("메시지를 찾을 수 없습니다.");
        }
    }
*/

    @Override
    public void initializeMessage(Message message) {
        message = new Message();
    }

    @Override
    public UUID save(UUID sender,UUID channelId,  String content) {
        Map<UUID, Message> messageMap = FileSerializationUtil.loadFromSer(FILE_NAME);
        Message message = new Message(sender,channelId, content);
        messageMap.put(message.getId(), message);
        FileSerializationUtil.saveToSer(FILE_NAME, messageMap);
        return message.getId();
    }

    @Override
    public Message findMessageById(UUID id) {
        Map<UUID, Message> messageMap = FileSerializationUtil.loadFromSer(FILE_NAME);
        return messageMap.get(id);
    }

    @Override
    public List<Message> findMessagesBySenderId(UUID id) {
        return findAll().stream().filter(s -> s.getSenderId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public List<Message> findMessagesByChannelId(UUID id) {
        return findAll().stream().filter(s -> s.getChannelId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public List<Message> findAll() {
        Map<UUID, Message> messageMap = FileSerializationUtil.loadFromSer(FILE_NAME);
        return new ArrayList<>(messageMap.values().stream().toList());
    }

    @Override
    public boolean delete(UUID messageId) {
        Map<UUID, Message> messageMap = FileSerializationUtil.loadFromSer(FILE_NAME);
        if(messageMap.containsKey(messageId)){
            messageMap.replace(messageId, new Message());
            messageMap.remove(messageId);
            FileSerializationUtil.saveToSer(FILE_NAME, messageMap);
            return true;
        }else {
            System.out.println("유저를 찾을 수 없습니다.");
            return false;
        }
    }

    @Override
    public void update(UUID id, String content) {
        Map<UUID, Message> messageMap = FileSerializationUtil.loadFromSer(FILE_NAME);
        if(messageMap.containsKey(id)){
            Message message = findMessageById(id);
            message.update(content);
            messageMap.replace(id, message);
            FileSerializationUtil.saveToSer(FILE_NAME, messageMap);

        }else {
            System.out.println("메시지를 찾을 수 없습니다.");
        }

    }
}

