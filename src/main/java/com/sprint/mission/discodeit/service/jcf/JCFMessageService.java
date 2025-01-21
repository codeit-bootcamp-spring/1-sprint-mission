package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private static final JCFMessageService jcfMessageService = new JCFMessageService();
    private final ChannelService channelService;
    private final Map<UUID, Message> data;

    private JCFMessageService() {
        data = new HashMap<>(1000);
        channelService = JCFChannelService.getInstance();
    }

    public static JCFMessageService getInstance() {
        return jcfMessageService;
    }

    @Override
    public Message createMessage(MessageDto messageDto) {
        Channel channel = channelService.readChannel(messageDto.getChannel().getId());
        User user = channel.getUser(messageDto.getWriter().getId());

        Message message = Message.of(user, messageDto.getContent(), channel);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message readMessage(UUID messageId) {
        return Optional.ofNullable(data.get(messageId))
                .orElseThrow(() -> new NotFoundException("등록되지 않은 message입니다."));
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateMessage(UUID messageId, String content) {
        Message message = readMessage(messageId);
        message.updateContent(content);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        data.remove(messageId);
    }
}
