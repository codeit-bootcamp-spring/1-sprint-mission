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
    public JCFMessageService(Map<UUID, Message> messageData,UserService userService, ChannelService channelService) {
        this.messageData = messageData;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(Message message) {
        // User와 Channel이 null인지 확인 이러한 부분도 .orElseThrow를 사용하여 null을 처리하는부분을 줄여야될까...?
        if (message.getAuthor() == null || message.getChannel() == null) {
            throw new IllegalArgumentException("Author and Channel cannot be null");
        }
        // User가 실제로 존재하는지 확인
        User author = userService.readUser(message.getAuthor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Author does not exist: "
                        + message.getAuthor().getId()));
        //Channel이 실제로 존재하는지 확인
        Channel channel = channelService.readChannel(message.getChannel().getId())
                .orElseThrow(() -> new IllegalArgumentException("Channel does not exist: " + message.getChannel().getId()));
        // 채널에 해당 사용자가 있는지 확인
        if (!channel.getParticipants().containsKey(author)) {
            throw new IllegalArgumentException("Author is not a participant of the channel: " + message.getChannel().getId());
        }
        messageData.put(message.getId(), message);
        System.out.println(message.toString()+"\n메세지 생성 완료\n");
        return message;
    }

    @Override
    public Optional<Message> readMessage(UUID existMessageId) {
        return Optional.ofNullable(messageData.get(existMessageId));
    }

    @Override
    public List<Message> readAllMessages() {
        return new ArrayList<>(messageData.values());
    }

    @Override
    public Message updateByAuthor(UUID existUserId, Message updateMessage){
        Optional<Message> existingMessage = messageData.values().stream()
                .filter(message -> message.getAuthor().equals(existUserId))
                .findFirst();
        if(existingMessage.isEmpty()){
           throw new NoSuchElementException("No message found for the given User");
       }
        Message existMessage = existingMessage.get();
        System.out.println("수정 전 메시지 = "+existMessage.getContent());
        existMessage.updateContent(updateMessage.getContent());
        existMessage.updateChannel(updateMessage.getChannel());
        existMessage.updateTime();
        messageData.put(existMessage.getId(), existMessage);
        System.out.println("수정 후 메시지 = "+existMessage.getContent());
        return existMessage;
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        if (!messageData.containsKey(messageId)) {
            return false;
        }
        messageData.remove(messageId);
        return true;
    }
}
