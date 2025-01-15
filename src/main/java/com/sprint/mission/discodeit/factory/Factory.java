package com.sprint.mission.discodeit.factory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.MessageValidator;

public interface Factory {

    UserService createUserService();

    ChannelService createChannelService();

    MessageService createMessageService();

    MessageValidator createMessageValidator();
}
