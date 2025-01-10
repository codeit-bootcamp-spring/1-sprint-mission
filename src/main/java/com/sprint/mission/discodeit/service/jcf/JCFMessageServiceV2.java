package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChatBehavior;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.MessageServiceV2;

import java.util.*;

public class JCFMessageServiceV2 implements MessageServiceV2 {

    private static volatile JCFMessageServiceV2 messageRepository;

    private final Map<String, List<Message>> data;
    private JCFMessageServiceV2(){
        data = new HashMap<>();
    }

    public static JCFMessageServiceV2 getInstance(){
        if(messageRepository == null){
            synchronized (JCFMessageServiceV2.class){
                if(messageRepository == null){
                    messageRepository = new JCFMessageServiceV2();
                }
            }
        }
        return messageRepository;
    }

    @Override
    public Message createMessage(Message message, String channelId) {
        data.putIfAbsent(channelId, new ArrayList<>());
        data.get(channelId).add(message);
        return message;
    }

    @Override
    public Optional<Message> getMessageById(String messageId, String channelId) {
        return data.get(channelId)
            .stream()
            .filter(m -> m.getUUID().equals(messageId))
            .findFirst();
    }

    @Override
    public List<Message> getMessagesByChannel(String channelId) {
        if(!data.containsKey(channelId)) throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        return Collections.unmodifiableList(new ArrayList<>(data.get(channelId)));
    }

    @Override
    public Message updateMessage(String channelId, String messageId, MessageUpdateDto updatedMessage) {
        return data.get(channelId)
            .stream()
            .filter(m -> m.getUUID().equals(messageId))
            .findFirst()
            .map(m -> {
                updatedMessage.getContent().ifPresent(m::setContent);
                updatedMessage.getContentUrl().ifPresent(m::setContentImage);
                m.setIsEdited();
                return m;
            }).orElse(null);
    }

    @Override
    public boolean deleteMessage(String messageId, String channelId) {
        List<Message> messages = data.get(channelId);
        if(messages != null){
            return messages.removeIf(m->m.getUUID().equals(messageId));
        }
        return false;
    }
}

