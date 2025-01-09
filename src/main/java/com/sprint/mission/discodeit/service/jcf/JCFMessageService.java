package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final JCFUserService userService; //의존성 주입
    private final JCFChannelService channelService;
    public JCFMessageService(){
        this.data = new HashMap<>();
        this.userService = new JCFUserService();
        this.channelService = new JCFChannelService();
    }

    @Override
    public Message create(Message message) {
        if (message.getAuthor() == null || message.getChannel() == null) {
            throw new IllegalArgumentException("Author and Channel cannot be null");
        }

        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> read(User user) {
        return data.values().stream()
                .filter(message -> message.getAuthor().equals(user))
                .findFirst();
    }

    @Override
    public List<Message> readAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    public Message updateByAuthor(User user, Message updatedMessage){
       Optional<Message> existingMessage = read(user);
       if(existingMessage.isEmpty()){
           throw new NoSuchElementException("No message found for the given User");
       }

        Message message = existingMessage.get();
        message.updateContent(updatedMessage.getContent());
        message.updateChannel(updatedMessage.getChannel());
        message.updateTime();

        data.put(message.getId(), message);
        return message;
    }

    @Override
    public boolean delete(UUID id) {
        if(!data.containsKey(id)){
            return false;
        }
        data.remove(id);
        return true;
    }
}
