package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.Validation;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID,Message> data;//검색을 위해서 map으로 변경
    private static volatile JCFMessageService instance;

    public JCFMessageService() {
        this.data = new HashMap<>();
    }

    //싱글톤
    public static JCFMessageService getInstance() {
        if (instance==null){
            synchronized (JCFChannelService.class){
                if(instance==null){
                    instance=new JCFMessageService();
                }
            }
        }
        return instance;
    }

    @Override
    public void createMessage(Message message) {
        Validation.validateUserExists(message.getSenderUser().getId());
        data.put(message.getId(),message);
    }

    @Override
    public Optional<Message> getMessageById(UUID id) {
        Message messageNullable=this.data.get(id);
        return Optional.ofNullable(Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + id + " not found")));
    }

    @Override
    public List<Message> getAllMessage() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateMessage(UUID id, Message updatedMessage) {
        Message existingChannel = data.get(id);
        if (existingChannel != null) {
            existingChannel.update(updatedMessage.getContent());
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        if(!this.data.containsKey(id)){
            throw new NoSuchElementException("Message with id "+id+" not found");
        }
        data.remove(id);
    }
}