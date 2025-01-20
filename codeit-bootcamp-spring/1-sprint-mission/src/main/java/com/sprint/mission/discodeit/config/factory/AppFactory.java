package com.sprint.mission.discodeit.config.factory;

import com.sprint.mission.discodeit.repository.jcf.channel.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.message.ChannelMessage.ChannelMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.message.directMessage.DirectMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.user.UserRepository;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.message.channelMessage.ChannelMessageService;
import com.sprint.mission.discodeit.service.message.directMessage.DirectMessageService;
import com.sprint.mission.discodeit.service.user.UserService;

public interface AppFactory {

    UserService getUserService();

    UserRepository getUserRepository();

    ChannelRepository getChannelRepository();

    ChannelService getChannelService();

    DirectMessageService getDirectMessageService();

    DirectMessageRepository getDirectMessageRepository();

    ChannelMessageRepository getChannelMessageRepository();

    ChannelMessageService getChannelMessageService();
}
