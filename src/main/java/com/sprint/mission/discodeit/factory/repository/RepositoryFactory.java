package com.sprint.mission.discodeit.factory.repository;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

public interface RepositoryFactory {

    UserRepository createUserRepository();
    ChannelRepository createChannelRepository();
    MessageRepository createMessageRepository();

    UserRepository getUserRepository();
    ChannelRepository getChannelRepository();
    MessageRepository getMessageRepository();

}
