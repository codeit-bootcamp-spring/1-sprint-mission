package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ChatBehavior implements ChannelBehavior {
    private final List<Message> chatHistory = new ArrayList<>();
    private final UserService userService;
    private final MessageService messageService;
    public ChatBehavior(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void setChannelPrivate(Channel channel) {
        channel.updatePrivate(false, channel);
    }

    @Override
    public void setChannelPublic(Channel channel) {
        channel.updatePrivate(true, channel);
    }

    public void addMessage(Message message){
        if(!checkIfUserExists(message.getUserUUID())){
            throw new IllegalArgumentException("존재하지 않는 user 입니다.");
        }
        chatHistory.add(message);
    }

    public Message getMessageById(String id){
        Message message = null;
        for(Message m : chatHistory){
            if(m.getUUID().equals(id)){
                message = m;
            }
        }
        return message;
    }

    public void deleteMessage(String id){
        for(Message m : chatHistory){
            if(m.getUUID().equals(id)){
                chatHistory.remove(m);
                break;
            }
        }
    }

    public void editMessage(String messageId, String messageContent){
        Optional<Message> message = chatHistory.stream().filter(msg -> msg.getUUID().equals(messageId)).findFirst();
        if(message.isPresent()){
            message.get().setContent(messageContent);
        }
    }

    public List<Message> getChatHistory(){
        return Collections.unmodifiableList(chatHistory);
    }

    private boolean checkIfUserExists(String userId){
        return userService.readUserById(userId).isPresent();
    }
}
