//package com.spirnt.mission.discodeit.config;
//
//import com.spirnt.mission.discodeit.repository.*;
//import com.spirnt.mission.discodeit.repository.file.*;
//import com.spirnt.mission.discodeit.repository.jcf.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RepositoryConfig {
//    private final RepositoryProperties properties;
//
//    public RepositoryConfig(RepositoryProperties properties) {
//        this.properties = properties;
//    }
//
//    //default: FileRepository
//    @Bean
//    public UserRepository userRepository() {
//        if (properties.getType().equals("jcf")) return new JCFUserRepository();
//        return new FileUserRepository(properties.getFileDirectory());
//    }
//
//    @Bean
//    public ChannelRepository channelRepository() {
//        if (properties.getType().equals("jcf")) return new JCFChannelRepository();
//        return new FileChannelRepository(properties.getFileDirectory());
//    }
//
//    @Bean
//    public MessageRepository messageRepository() {
//        if (properties.getType().equals("jcf")) return new JCFMessageRepository();
//        return new FileMessageRepository(properties.getFileDirectory());
//    }
//
//    //
//
//    @Bean
//    public ReadStatusRepository readStatusRepository() {
//        if (properties.getType().equals("jcf")) return new JCFReadStatusRepository();
//        return new FileReadStatusRepository(properties.getFileDirectory());
//    }
//
//    @Bean
//    public UserStatusRepository userStatusRepository() {
//        if (properties.getType().equals("jcf")) return new JCFUserStatusRepository();
//        return new FileUserStatusRepository(properties.getFileDirectory());
//    }
//
//    @Bean
//    public BinaryContentRepository binaryContentRepository() {
//        if (properties.getType().equals("jcf")) return new JCFBinaryContentRepository();
//        return new FileBinaryContentRepository(properties.getFileDirectory());
//    }
//
//
//}
