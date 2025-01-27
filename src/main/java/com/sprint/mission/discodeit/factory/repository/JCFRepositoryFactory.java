package com.sprint.mission.discodeit.factory.repository;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;

public class JCFRepositoryFactory implements RepositoryFactory {
    private static JCFRepositoryFactory instance;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    private JCFRepositoryFactory() {
        this.userRepository = new JCFUserRepository();
        this.channelRepository = new JCFChannelRepository();
        this.messageRepository = new JCFMessageRepository();
    }

    public static JCFRepositoryFactory getInstance() {
        if (instance == null) {
            return new JCFRepositoryFactory();
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
