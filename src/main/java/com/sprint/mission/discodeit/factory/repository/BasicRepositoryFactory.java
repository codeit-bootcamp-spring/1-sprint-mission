package com.sprint.mission.discodeit.factory.repository;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;


public class BasicRepositoryFactory implements RepositoryFactory {

    private static BasicRepositoryFactory instance;

    private UserRepository userRepository;
    private ChannelRepository channelRepository;
    private MessageRepository messageRepository;
    private final String mode;

    public static BasicRepositoryFactory getInstance(String mode) {
        if(instance == null){
            instance = new BasicRepositoryFactory(mode);
        }
        return instance;
    }

    private BasicRepositoryFactory(String mode) {
        this.mode = mode;
        this.userRepository = createUserRepository();
        this.channelRepository = createChannelRepository();
        this.messageRepository = createMessageRepository();
    }


    @Override
    public UserRepository createUserRepository() {
        if(userRepository == null){
            if(mode.equals("jcf")){
                userRepository = new JCFUserRepository();
            }else if(mode.equals("file")){
                userRepository = new FileUserRepository();
            }
        }
        return userRepository;
    }

    @Override
    public ChannelRepository createChannelRepository() {
        if(channelRepository == null){
            if(mode.equals("jcf")){
                channelRepository = new JCFChannelRepository();
            }else if(mode.equals("file")){
                channelRepository = new FileChannelRepository();
            }
        }
        return channelRepository;
    }

    @Override
    public MessageRepository createMessageRepository() {
        if(messageRepository == null){
            if(mode.equals("jcf")){
                messageRepository = new JCFMessageRepository();
            }else if(mode.equals("file")){
                messageRepository = new FileMessageRepository();
            }
        }
        return messageRepository;
    }

    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public ChannelRepository getChannelRepository() {
        return channelRepository;
    }

    @Override
    public MessageRepository getMessageRepository() {
        return messageRepository;
    }
}
