package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageRepository implements MessageRepository {
    private final List<Message>messageList = new ArrayList<>();

    @Override
    public void save(Message message) {
        if(message == null || message.getUserId() == null){
            throw new IllegalArgumentException(" Message cannot be null. ");
        }
        messageList.add(message);
        System.out.println("<<< Message saved successfully >>>");
    }

    @Override
    public Message findByUuid(String messageUuid) {
        if(messageUuid == null || messageUuid.isEmpty()){
            throw new IllegalArgumentException(" Channel cannot be null or empty. ");
        }
        return messageList.stream()
                .filter(message -> message.getMessageUuid().toString().equals(messageUuid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageList);
    }

    @Override
    public void delete(String messageUuid) {
        boolean removed = messageList.removeIf(channel ->
                channel.getMessageUuid().toString().equals(messageUuid));

        if (removed) {
            System.out.println("Channel with UUID " + messageUuid + " was deleted.");
        } else {
            System.out.println("Channel with UUID " + messageUuid + " not found.");
        }
    }
}
