package com.sprint.mission.discodeit.factory.repository;

import com.sprint.mission.discodeit.repository.*;

public interface RepositoryFactory {

    UserRepository createUserRepository();
    ChannelRepository createChannelRepository();
    MessageRepository createMessageRepository();
    UserStatusRepository createUserStatusRepository();
    ReadStatusRepository createReadStatusRepository();
    BinaryContentRepository createBinaryContentRepository();

    UserRepository getUserRepository();
    ChannelRepository getChannelRepository();
    MessageRepository getMessageRepository();
    UserStatusRepository getUserStatusRepository();
    ReadStatusRepository getReadStatusRepository();
    BinaryContentRepository getBinaryContentRepository();

}
