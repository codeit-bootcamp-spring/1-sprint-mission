package com.sprint.misson.discordeit.factory.repository;

import com.sprint.misson.discordeit.repository.ChannelRepository;
import com.sprint.misson.discordeit.repository.MessageRepository;
import com.sprint.misson.discordeit.repository.UserRepository;

public interface RepositoryFactory {

    UserRepository createUserRepository();
    ChannelRepository createChannelRepository();
    MessageRepository createMessageRepository();

    UserRepository getUserRepository();
    ChannelRepository getChannelRepository();
    MessageRepository getMessageRepository();

}
