package com.sprint.mission.discodeit.factory.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.ServiceFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFServiceFactoryBackup implements ServiceFactory {
    private final Map<UUID, User> userData = new HashMap<>();
    private final Map<UUID, Channel> channelData = new HashMap<>();
    private final Map<UUID, Message> messageData = new HashMap<>();

    private UserService userService;
    private ChannelService channelService;
    private MessageService messageService;

    public JCFServiceFactoryBackup() {
        //서비스 객체 생성
        this.userService = new JCFUserService(userData);
        this.channelService = new JCFChannelService(channelData);
        this.messageService = new JCFMessageService(messageData);

        //의존성 주입
        userService.setDependencies(messageService, channelService);
        channelService.setDependencies(userService, messageService);
        messageService.setDependencies(userService, channelService);
    }
    @Override
    public UserService createUserService() {
        return userService;
    }

    @Override
    public ChannelService createChannelService() {
        return channelService;
    }

    @Override
    public MessageService createMessageService() {
        return messageService;
    }
}
