package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {


    private final Map<UUID, Message> data;

    public JCFMessageService() {
        data = new HashMap<>();
    }

    public UUID save(Message message) {
        data.put(message.getId(), message);
        return message.getId();
    }

    public Message findOne(UUID id) {
        return data.get(id);
    }

    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    public UUID update(Message message){
        data.put(message.getId(), message);
        return message.getId();
    }

    public UUID delete(UUID id) {
        data.remove(id);
        return id;
    }


    @Override
    public UUID create(String content, UUID authorId, UUID channelId) {
        if(!Message.validation(content)){
            throw new IllegalArgumentException("잘못된 형식입니다.");
        }
        Message message = new Message(content, authorId, channelId);
        return save(message);
    }

    @Override
    public Message read(UUID id) {
        Message findMessage = findOne(id);
        return Optional.ofNullable(findMessage)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지가 없습니다."));
    }

    @Override
    public List<Message> readAll() {
        return findAll();
    }

    @Override
    public Message updateMessage(UUID id, String message, User user) {
        Message findMessage = findOne(id);
        if(!user.userCompare(findMessage.getAuthorId())){
            throw new IllegalStateException("message 변경 권한이 없습니다.");
        }
        findMessage.setMessage(message);
        update(findMessage);
        return findMessage;
    }

    @Override
    public UUID deleteMessage(UUID id, User user) {
        Message findMessage = findOne(id);
        if(!user.userCompare(findMessage.getAuthorId())){
            throw new IllegalStateException("message 삭제 권한이 없습니다.");
        }
        return delete(findMessage.getId());
    }
}