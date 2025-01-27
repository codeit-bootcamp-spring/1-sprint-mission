package com.sprint.mission.discodeit.factory.repository;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;

public class FileRepositoryFactory implements RepositoryFactory {
    private static FileRepositoryFactory instance;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    private FileRepositoryFactory() {
        this.userRepository = new FileUserRepository();
        this.channelRepository = new FileChannelRepository();
        this.messageRepository = new FileMessageRepository();
    }

    public static FileRepositoryFactory getInstance() {
        if (instance == null) {
            return new FileRepositoryFactory();
        }
        return instance;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    public ChannelRepository getChannelRepository() {
        return channelRepository;
    }
}
