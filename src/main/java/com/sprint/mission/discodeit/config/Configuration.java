package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserSerivce;

public class Configuration {
    private static final Configuration INSTANCE = new Configuration();

    private UserService userService;
    private MessageService messageService;
    private ChannelService channelService;

    private Configuration() {
    }

    public static Configuration getInstance() {
        return INSTANCE;
    }

    public UserService userService() {
        if (userService == null) {
            userService = new JCFUserSerivce();
        }
        return userService;
    }

    public MessageService messageService() {
        if (messageService == null) {
            messageService = new JCFMessageService(userService());
        }
        return messageService;
    }

    public ChannelService channelService() {
        if (channelService == null) {
            channelService = new JCFChannelService(userService());
        }
        return channelService;
    }
}