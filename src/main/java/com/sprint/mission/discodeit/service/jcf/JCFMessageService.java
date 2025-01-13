package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messageData;
    private UserService userService;
    private ChannelService channelService;

    //팩토리 패턴으로 인하여 private이면 serviceFactory에서 접근이 불가하므로 public으로 변경
    public JCFMessageService(Map<UUID, Message> messageData) {
        this.messageData = messageData;
    }

    public void setDependencies(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(Message message) {
        // User와 Channel이 null인지 확인
        if (message.getAuthor() == null || message.getChannel() == null) {
            throw new IllegalArgumentException("Author and Channel cannot be null");
        }
        // User가 실제로 존재하는지 확인
        if(!userService.readUser(message.getAuthor()).isPresent()) {
            throw new IllegalArgumentException("Author does not exist " + message.getAuthor().getId());
        }
        //Channel이 실제로 존재하는지 확인
        Optional<Channel> existingChannel = channelService.readChannel(message.getChannel());
        if (existingChannel.isEmpty()) {
            throw new IllegalArgumentException("Channel does not exist: " + message.getChannel().getId());
        }
        // 채널에 해당 사용자가 있는지 확인
        Channel channel = existingChannel.get();
        if (!channel.getParticipants().contains(message.getAuthor())) {
            throw new IllegalArgumentException("Author is not a participant of the channel: " + message.getChannel().getId());
        }
        messageData.put(message.getId(), message);
        System.out.println(message.toString()+"\n메세지 생성 완료\n");
        return message;
    }

    @Override
    public Optional<Message> readMessage(Message message) {
        System.out.println(message.toString());
        return Optional.ofNullable(messageData.get(message.getId()));
    }

    @Override
    public List<Message> readAll(){
        return new ArrayList<>(messageData.values());
    }

    @Override
    public Message updateByAuthor(User user, Message updatedMessage){
        Optional<Message> existingMessage = messageData.values().stream()
                .filter(message -> message.getAuthor().equals(user))
                .findFirst();
        if(existingMessage.isEmpty()){
           throw new NoSuchElementException("No message found for the given User");
       }
        Message message = existingMessage.get();
        System.out.println("수정 전 메시지 = "+message.getContent());
        message.updateContent(updatedMessage.getContent());
        message.updateChannel(updatedMessage.getChannel());
        message.updateTime();
        messageData.put(message.getId(), message);
        System.out.println("수정 후 메시지 = "+message.getContent());
        return message;
    }

    @Override
    public boolean deleteMessage(Message message) {
        if (!messageData.containsKey(message.getId())) {
            return false;
        }
        messageData.remove(message.getId());
        return true;
    }

    // 채널 삭제 시 해당 채널의 메시지도 같이 삭제
    @Override
    public void deleteMessageByChannel(Channel channel) {
        messageData.values().removeIf(message -> message.getChannel().equals(channel));
    }

    //사용자 삭제 시 해당 사용자가 작성한 메시지 삭제
    @Override
    public void deleteMessageByUser(User user) {
        messageData.values().removeIf(message -> message.getAuthor().equals(user));
    }
}
