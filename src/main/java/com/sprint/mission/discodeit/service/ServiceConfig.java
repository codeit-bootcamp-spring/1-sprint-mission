//package com.sprint.mission.discodeit.service;
//
//import com.sprint.mission.discodeit.repository.*;
//import com.sprint.mission.discodeit.repository.file.json.JsonBinaryContentRepository;
//import com.sprint.mission.discodeit.repository.file.json.JsonUserStatusRepository;
//import com.sprint.mission.discodeit.service.basic.BasicChannelService;
//import com.sprint.mission.discodeit.service.basic.BasicMessageService;
//import com.sprint.mission.discodeit.service.basic.BasicUserService;
//import com.sprint.mission.discodeit.service.status.UserStateService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ServiceConfig {
//    @Bean
//    UserService userService(UserRepository userRepository,
//                            UserStateService userStatusService,
//                            BinaryContentService binaryContentService) {
//
//        return new BasicUserService(userRepository, userStatusService, binaryContentService);
//    }
//
//    @Bean
//    ChannelService channelService(ChannelRepository channelRepository, MessageService messageService, ReadStatusRepository readStatusRepository) {
//        return new BasicChannelService(channelRepository, messageService, readStatusRepository);
//    }
//
//    @Bean
//    MessageService messageService(MessageRepository messageRepository,
//                                  BinaryContentRepository binaryContentRepository,
//                                  ChannelRepository channelRepository ) {
//
//        return new BasicMessageService(messageRepository, binaryContentRepository, channelRepository);
//    }
//
//    @Bean
//    UserStateService userStateService(UserRepository userRepository, UserStatusRepository userStatusRepository) {
//        return new UserStateService(userRepository, userStatusRepository);
//    }
//
//    @Bean
//    BinaryContentRepository binaryContentRepository() {
//        return new JsonBinaryContentRepository();
//    }
//
//    @Bean
//    UserStatusRepository userStatusRepository() {
//        return new JsonUserStatusRepository();
//    }
//
//}
