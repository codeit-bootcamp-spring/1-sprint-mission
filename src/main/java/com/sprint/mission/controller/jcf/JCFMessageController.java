package com.sprint.mission.controller.jcf;

import com.sprint.mission.controller.MessageController;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.service.jcf.JCFChannelService;
import com.sprint.mission.service.jcf.JCFMessageService;
import com.sprint.mission.service.jcf.JCFUserService;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageController implements MessageController {

    private final JCFUserService userService = JCFUserService.getInstance();
    private final JCFChannelService channelService = JCFChannelService.getInstance();
    private final JCFMessageService messageService = JCFMessageService.getInstance();

    @Override
    public Message create(UUID channelId, UUID userId, String message) {
        // 채널 찾고, user 찾고 (findById 이 과정에서 검증 다 끝남) => message 생성
        return messageService.createOrUpdate(Message.createMessage(channelService.findById(channelId), userService.findById(userId), message));
    }

    @Override
    public Message update(UUID messageId, String newString) {
        return messageService.update(messageId, newString);
    }

    @Override
    public Message findById(UUID messageId){
        return messageService.findById(messageId);
    }

    @Override
    public Set<Message> findAll(){
        return messageService.findAll();
    }

    @Override
    public Set<Message> findMessagesByUserId(UUID userId) {
        return userService.findById(userId).getMessagesImmutable();
    }

    @Override
    public Set<Message> findMessagesInChannel(UUID channelId) {
        return messageService.findMessagesInChannel(channelService.findById(channelId));
    }

    @Override
    public Set<Message> findContainingMessageInChannel(UUID channelId, String writedMessage) {
        Channel writedChannel = channelService.findById(channelId);
        return messageService.findMessagesInChannel(writedChannel).stream()
                .filter(message -> message.getMessage().contains(writedMessage))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public void delete(UUID messageId){
        messageService.delete(messageId);
    }

    @Override
    public void createMessageDirectory() {}
}
