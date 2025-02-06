package com.sprint.mission.discodeit.factory.repository;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.*;


public class BasicRepositoryFactory implements RepositoryFactory {

    private static BasicRepositoryFactory instance;

    private UserRepository userRepository;
    private ChannelRepository channelRepository;
    private MessageRepository messageRepository;
    private UserStatusRepository userStatusRepository;
    private ReadStatusRepository readStatusRepository;
    private BinaryContentRepository binaryContentRepository;
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
        this.userStatusRepository = createUserStatusRepository();
        this.readStatusRepository = createReadStatusRepository();
        this.binaryContentRepository = createBinaryContentRepository();
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
    public UserStatusRepository createUserStatusRepository() {
        if(userStatusRepository == null){
            if(mode.equals("jcf")){
                userStatusRepository = new JCFUserStatusRepository();
            }
//            else if(mode.equals("file")){
//                userStatusRepository = new FileUserStatusRepository();
//            }
        }
        return userStatusRepository;
    }

    @Override
    public ReadStatusRepository createReadStatusRepository() {
        if(readStatusRepository == null){
            if(mode.equals("jcf")){
                readStatusRepository = new JCFReadStatusRepository();
            }
//            else if(mode.equals("file")){
//                userStatusRepository = new FileUserStatusRepository();
//            }
        }
        return readStatusRepository;    }

    @Override
    public BinaryContentRepository createBinaryContentRepository() {
        if(binaryContentRepository == null){
            if(mode.equals("jcf")){
                binaryContentRepository = new JCFBinaryContentRepository();
            }
//            else if(mode.equals("file")){
//                userStatusRepository = new FileUserStatusRepository();
//            }
        }
        return binaryContentRepository;
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

    @Override
    public UserStatusRepository getUserStatusRepository() {
        return userStatusRepository;
    }

    @Override
    public ReadStatusRepository getReadStatusRepository() {
        return readStatusRepository;
    }

    @Override
    public BinaryContentRepository getBinaryContentRepository() {
        return binaryContentRepository;
    }
}
