package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final JCFUserService userService; //의존성 주입
    private final JCFChannelService channelService;

    public JCFMessageService(Map<UUID, Message> data, JCFUserService userService, JCFChannelService channelService) {
        this.data = data;
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
        data.put(message.getId(), message);
        System.out.println(message.toString()+"\n메세지 생성 완료\n");
        return message;
    }

    @Override
    public Optional<Message> readMessage(Message message) {
        System.out.println(message.toString());
        return Optional.ofNullable(data.get(message.getId()));
    }

    @Override
    public List<Message> readAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    public Message updateByAuthor(User user, Message updatedMessage){
        Optional<Message> existingMessage = data.values().stream()
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
        data.put(message.getId(), message);
        System.out.println("수정 후 메시지 = "+message.getContent());
        return message;
    }

    @Override
    public boolean deleteMessage(Message message) {
        if (!data.containsKey(message.getId())) {
            return false;
        }
        data.remove(message.getId());
        return true;
    }

    // 채널 삭제 시 해당 채널의 메시지도 같이 삭제
    @Override
    public void deleteMessageByChannel(Channel channel) {
        data.values().removeIf(message -> message.getChannel().equals(channel));
    }

    //사용자 삭제 시 해당 사용자가 작성한 메시지 삭제
    @Override
    public void deleteMessageByUser(User user) {
        data.values().removeIf(message -> message.getAuthor().equals(user));
    }
}
