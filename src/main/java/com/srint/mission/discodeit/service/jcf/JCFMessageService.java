package com.srint.mission.discodeit.service.jcf;


import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {


    private final Map<UUID, Message> data;

    public JCFMessageService() {
        data = new HashMap<>();
    }

    @Override
    public UUID save(Message message) {
        data.put(message.getId(), message);
        return message.getId();
    }

    @Override
    public Message findOne(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("조회할 Message을 찾지 못했습니다.");
        }
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        if(data.isEmpty()){
            throw new IllegalArgumentException("Message가 없습니다.");
        }
        return data.values().stream().toList();
    }

    @Override
    public UUID delete(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("삭제할 Message를 찾지 못했습니다.");
        }
        data.remove(id);
        return id;
    }

    @Override
    public UUID create(String content, User user, Channel channel) {
        Message message = new Message(content, user, channel);
        message.setMessageChannel();
        return save(message);
    }

    @Override
    public Message read(UUID id) {
        return findOne(id);
    }

    @Override
    public List<Message> readAll() {
        return findAll().stream().toList();
    }

    @Override
    public Message update(UUID id, String message, User user) {
        Message findMessage = findOne(id);
        if(!findMessage.getUser().userCompare(user)){
            throw new IllegalStateException("message 변경 권한이 없습니다.");
        }
        findMessage.setContent(message);
        return findMessage;
    }

    @Override
    public UUID deleteMessage(UUID id, User user) {
        Message findMessage = findOne(id);
        if(!findMessage.getUser().userCompare(user)){
            throw new IllegalStateException("message 삭제 권한이 없습니다.");
        }
        findMessage.getChannel().deleteMessage(findMessage);
        return id;
    }
}
