package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class JCFMessageRepository {
//    private static final JCFMessageRepository INSTANCE = new JCFMessageRepository();
//    private JCFMessageRepository(){}
//    public static JCFMessageRepository getInstance() {
//        return INSTANCE;
//    }

    private JCFBinaryContentRepository contentRepository;
    private static final List<Message> messages = new ArrayList<>();


    public Message createMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setMessageId(UUID.randomUUID());

        if(checkMessageContains(message.getMessageId())){
            while(!checkMessageContains(message.getMessageId())){
                message.setMessageId(UUID.randomUUID());
            }
        }

        message.setText(messageDto.getText());
        message.setUserId(messageDto.getUserId());
        message.setUserName(messageDto.getUserName());
        message.setChannelId(messageDto.getChannelId());
        message.setChannelName(messageDto.getChannelName());
        message.setIsContainContent(messageDto.getHaveContent());

        messages.add(message);
        if(messageDto.getHaveContent()) contentRepository.saveMessageContent(message.getMessageId());
        return message;
    }

    public void modifyMessage(MessageDto dto) {
        for (Message message : messages) {
            if (message.getUserId().equals(dto.getUserId()) && message.getMessageId().equals(dto.getMessageId())) {
                message.setText(dto.getText());
                message.setUpdatedAt(Instant.now());
                break;
            }
        }
    }

    public List<Message> findAllByChannelId(UUID channelId) {
        return messages.stream()
                .filter(x -> x.getChannelId().equals(channelId))
                .toList();
    }

    public void deleteMessage(UUID messageId){
        for(Message message : messages){
            if(message.getMessageId().equals(messageId)){
                messages.remove(message);
                contentRepository.findMessageContent(messageId);
                break;
            }
        }
    }

    public boolean checkMessageContains(UUID id){
        for(Message message : messages){
            if(message.getMessageId().equals(id)){
                return true;
            }
        }

        return false;
    }

    public void modifyUserName(UUID userId, String newUserName){
        for(Message message : messages){
            if(message.getUserId().equals(userId)){
                message.setUserName(newUserName);
            }
        }
    }

    public void modifyChannelName(UUID channelId,String newChannelId) {
        for(Message message : messages){
            if(message.getChannelId().equals(channelId)){
                message.setChannelName(newChannelId);
            }
        }
    }

    public void deletedChannelMessage(UUID channelId){
        messages.removeIf(message -> message.getChannelId().equals(channelId));
    }

    public Instant currentMessageOfChannel(UUID channelId){
        Instant time = Instant.MIN;
        for(Message message : messages){
            if(message.getChannelId().equals(channelId)){
                if(time.compareTo(message.getUpdatedAt()) <= 0){
                    time = message.getUpdatedAt();
                }
            }
        }

        return time;
    }
}
