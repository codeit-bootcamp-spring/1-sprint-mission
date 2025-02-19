//package com.sprint.mission.discodeit;
//
//import com.sprint.mission.discodeit.entity.*;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.file.*;
//import com.sprint.mission.discodeit.repository.jcf.*;
//import com.sprint.mission.discodeit.service.*;
//import com.sprint.mission.discodeit.service.basic.BasicChannelService;
//import com.sprint.mission.discodeit.service.basic.BasicMassageService;
//import com.sprint.mission.discodeit.service.basic.BasicUserService;
//
//public class JavaApplication {
//
//    static User setupUser(UserService userService) {
//        User user = userService.createUser("woody", "woody@codeit.com", "woody1234");
//        return user;
//    }
//
//    static Channel setupChannel(ChannelService channelService) {
//        Channel channel = channelService.createChannel(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.");
//        return channel;
//    }
//
//    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
//        Message message = messageService.createMessage("안녕하세요.", channel.getId(), author.getId());
//        System.out.println("메시지 생성: " + message.getId());
//    }
//
//    public static void main(String[] args) {
//
//        // jcf
////        System.out.println("===Use JCF Repository===");
////        UserRepository userRepository = new JCFUserRepository();
////        ChannelRepository channelRepository = new JCFChannelRepository();
////        MessageRepository messageRepository = new JCFMessageRepository();
//
//        // file
//        System.out.println("===Use File Repository===");
//        UserRepository userRepository = new FileUserRepository();
//        ChannelRepository channelRepository = new FileChannelRepository();
//        MessageRepository messageRepository = new FileMessageRepository();
//
//        // 서비스 초기화
////        // TODO Basic*Service 구현체를 초기화하세요.
////        UserService userService = new BasicUserService(userRepository);
////        ChannelService channelService = new BasicChannelService(channelRepository);
////        MessageService messageService = new BasicMassageService(messageRepository, channelService, userService);
////
////        // 셋업
////        User user = setupUser(userService);
////        Channel channel = setupChannel(channelService);
////        // 테스트
////        messageCreateTest(messageService, channel, user);
//
//    }
//}
