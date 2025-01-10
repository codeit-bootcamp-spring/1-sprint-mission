package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.global.ErrorMessage;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messageData;
    private final JCFUserService jcfUserService;
    private final JCFChannelService jcfChannelService;

    private JCFMessageService(JCFUserService jcfUserService, JCFChannelService jcfChannelService) {
        messageData = new HashMap<>();
        this.jcfUserService = jcfUserService;
        this.jcfChannelService = jcfChannelService;
    }

    public static JCFMessageService getInstance(){
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final JCFMessageService INSTANCE =
                new JCFMessageService(JCFUserService.getInstance(), JCFChannelService.getInstance());
    }

    @Override
    public Message createMessage(UUID sendUserId, UUID channelId, String content) {
        User findUser = jcfUserService.findUserById(sendUserId);
        Channel findChannel = jcfChannelService.findChannelById(channelId);

        Message newMessage = new Message(findUser, findChannel, content);

        messageData.put(newMessage.getId(), newMessage);

        return newMessage;
    }

    @Override
    public Message findMessageById(UUID messageId) {
        return Optional.ofNullable(messageData.get(messageId))
                .orElseThrow(() -> new RuntimeException(ErrorMessage.MESSAGE_NOT_FOUND));
    }

    @Override
    public List<Message> findAllMessage() {
        return new ArrayList<>(messageData.values());
    }

    @Override
    public void updateMessage(UUID sendUserId, UUID messageId, String content) {
        jcfUserService.findUserById(sendUserId);
        Message findMessage = findMessageById(messageId);

        if(findMessage.getSendUser().getId() != sendUserId){
            throw new RuntimeException(ErrorMessage.NOT_MESSAGE_CREATOR);
        }

        findMessage.updateContent(content);
        findMessage.updateUpdatedAt(Instant.now().toEpochMilli());

        messageData.put(findMessage.getId(), findMessage);
    }

    @Override
    public void deleteMessage(UUID sendUserId, UUID messageId) {
        // 메세지 조회
        Message findMessage = findMessageById(messageId);

        // 메세지 생성자가 맞는지 확인
        if(findMessage.getSendUser().getId() != sendUserId){
            throw new RuntimeException(ErrorMessage.NOT_MESSAGE_CREATOR);
        }

        // 메세지 삭제
        messageData.remove(messageId);
    }
}
