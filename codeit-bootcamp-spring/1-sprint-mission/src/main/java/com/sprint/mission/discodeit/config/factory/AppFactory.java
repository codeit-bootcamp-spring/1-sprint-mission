package com.sprint.mission.discodeit.config.factory;

import com.sprint.mission.discodeit.db.channel.ChannelRepository;
import com.sprint.mission.discodeit.db.message.MessageRepository;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.message.MessageService;
import com.sprint.mission.discodeit.service.user.UserService;

public interface AppFactory {

    UserService getUserService();

    UserRepository getUserRepository();

    ChannelRepository getChannelRepository();

    ChannelService getChannelService();

    MessageService getMessageService();

    MessageRepository getMessageRepository();
}
