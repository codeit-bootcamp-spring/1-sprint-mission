package com.sprint.mission.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.service.jcf.EntityService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageRepository{
    private static final MessageRepository INSTANCE = new MessageRepository();
    private MessageRepository(){}
    public static MessageRepository getInstance() {
        return INSTANCE;
    }

    private static final List<Message> messages = new ArrayList<>();


    public Message createMessage(String text, UUID userId, String userName ,UUID channelId,String channelName) {
        Message message = new Message();
        message.setText(text);
        message.setUserId(userId);
        message.setUserName(userName);
        message.setChannelId(channelId);
        message.setChannelName(channelName);
        messages.add(message);
        return message;
    }

    public void modifyMessage(UUID Userid, UUID messageId ,String text) {
        for (Message message : messages) {
            if (message.getUserId().equals(Userid) && message.getMessageId().equals(messageId)) {
                message.setText(text);
                message.setUpdatedAt(System.currentTimeMillis());
                break;
            }
        }
    }

    public List<Message> getMessagesOfChannel(UUID channelId) {
        return messages.stream()
                .filter(x -> x.getChannelId().equals(channelId))
                .toList();
    }

    public void deleteMessage(UUID messageId){
        for(Message message : messages){
            if(message.getMessageId().equals(messageId)){
                messages.remove(message);
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
        for(Message message : messages){
            if(message.getChannelId().equals(channelId)){
                messages.remove(message);
            }
        }
    }
}
