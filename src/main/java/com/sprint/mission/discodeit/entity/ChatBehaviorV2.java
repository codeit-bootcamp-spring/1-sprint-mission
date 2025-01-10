package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageServiceV2;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class ChatBehaviorV2 implements ChannelBehavior{

    private static final JCFMessageServiceV2 messageService = JCFMessageServiceV2.getInstance();
    private Channel channel;
    private UserService userService;
    public ChatBehaviorV2(UserService userService){
        this.userService = userService;
    }
    @Override
    public void setChannelPrivate(Channel channel) {

    }

    @Override
    public void setChannelPublic(Channel channel) {

    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Message addMessage(Message message) {
        if(!checkIfUserExists(message.getUserUUID())) throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        return messageService.createMessage(message, channel.getUUID());
    }

    public boolean deleteMessage(String messageId) {
        return messageService.deleteMessage(messageId, channel.getUUID());
    }

    public List<Message> getChatHistory() {
        return messageService.getMessagesByChannel(channel.getUUID());
    }

    public Message updateMessage( String messageId, MessageUpdateDto updatedMessage) {
        return messageService.updateMessage(channel.getUUID(), messageId, updatedMessage);
    }

    public Message getSingleMessage(String messageId){
        return messageService.getMessageById(messageId, channel.getUUID()).orElse(null);
    }

    private boolean checkIfUserExists(String userId){
        return userService.readUserById(userId).isPresent();
    }
}
