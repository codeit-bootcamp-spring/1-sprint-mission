//package com.spirnt.mission.discodeit.config;
//
//
//import com.spirnt.mission.discodeit.repository.*;
//import com.spirnt.mission.discodeit.service.*;
//import com.spirnt.mission.discodeit.service.basic.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ServiceConfig {
//    // Basic*Service 구현체를 Service 인터페이스의 Bean으로 등록
//    private final UserRepository userRepository;
//    private final ChannelRepository channelRepository;
//    private final MessageRepository messageRepository;
//    private final ReadStatusRepository readStatusRepository;
//    private final UserStatusRepository userStatusRepository;
//    private final BinaryContentRepository binaryContentRepository;
//
//    // Repository Bean을 생성자 주입
//    public ServiceConfig(UserRepository userRepository, ChannelRepository channelRepository,
//                         MessageRepository messageRepository, ReadStatusRepository readStatusRepository,
//                         UserStatusRepository userStatusRepository, BinaryContentRepository binaryContentRepository) {
//        this.userRepository = userRepository;
//        this.channelRepository = channelRepository;
//        this.messageRepository = messageRepository;
//        this.readStatusRepository = readStatusRepository;
//        this.userStatusRepository = userStatusRepository;
//        this.binaryContentRepository = binaryContentRepository;
//    }
//
//    @Bean
//    public UserService userService() {
//        return new BasicUserService(userRepository);
//    }
//
//    @Bean
//    public ChannelService channelService() {
//        return new BasicChannelService(channelRepository);
//    }
//
//    @Bean
//    public MessageService messageService() {
//        return new BasicMessageService(messageRepository);
//    }
//
//    //
//
//    @Bean
//    public AuthService authService() {
//        return new AuthServiceImpl(userRepository);
//    }
//
//    @Bean
//    public ReadStatusService readStatusService() {
//        return new ReadStatusServiceImpl(readStatusRepository);
//    }
//
//    @Bean
//    public UserStatusService userStatusService() {
//        return new UserStatusServiceImpl(userStatusRepository);
//    }
//
//    @Bean
//    public BinaryContentService binaryContentService() {
//        return new BinaryContentServiceImpl(binaryContentRepository);
//    }
//
//}
