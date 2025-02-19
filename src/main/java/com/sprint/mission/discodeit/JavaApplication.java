//package com.sprint.mission.discodeit;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.service.basic.BasicChannelService;
//import com.sprint.mission.discodeit.service.basic.BasicMessageService;
//import com.sprint.mission.discodeit.service.basic.BasicUserService;
//import com.sprint.mission.discodeit.view.ConsoleView;
//
//
//import java.util.List;
//import java.util.UUID;
//
//
//
//public class JavaApplication {
//    public static void main(String[] args) {
//        // JCF Repository를 사용하는 경우
//        testWithJCFRepository();
//
//        // File Repository를 사용하는 경우
////        testWithFileRepository();
//    }
//
//    private static void testWithJCFRepository() {
//        System.out.println("\n=== JCF Repository 테스트 ===");
//        ConsoleView consoleView = new ConsoleView();
//
//        // Repository 초기화
//        UserRepository userRepo = new JCFUserRepository();
//        ChannelRepository channelRepo = new JCFChannelRepository();
//        MessageRepository messageRepo = new JCFMessageRepository();
//
//        // Service 초기화
//        UserService userService = new BasicUserService(userRepo);
//        ChannelService channelService = new BasicChannelService(channelRepo, consoleView, userService); // ConsoleView와 UserService 주입
//        MessageService messageService = new BasicMessageService(messageRepo, channelService, userService);
//
//        // 테스트 실행
//        runTest(userService, channelService, messageService);
//    }
//
//    private static void testWithFileRepository() {
//        System.out.println("\n=== File Repository 테스트 ===");
//        ConsoleView consoleView = new ConsoleView();
//
//        // Repository 초기화
//        UserRepository userRepo = new FileUserRepository();
//        ChannelRepository channelRepo = new FileChannelRepository();
//        MessageRepository messageRepo = new FileMessageRepository();
//
//        // Service 초기화
//        UserService userService = new BasicUserService(userRepo);
//        ChannelService channelService = new BasicChannelService(channelRepo, consoleView, userService); // Added missing parameters
//        MessageService messageService = new BasicMessageService(messageRepo, channelService, userService);
//
//        // 테스트 실행
//        runTest(userService, channelService, messageService);
//    }
//
//    private static void runTest(UserService userService, ChannelService channelService, MessageService messageService) {
//        // 유저 생성
//        User user = userService.createUser("testUser", "password123");
//        ConsoleView consoleView = new ConsoleView();
//        consoleView.displayUser(user);
//
//        // 채널 생성
//        Channel channel = channelService.createChannel("testChannel");
//        consoleView.displayChannel(channel, user);
//
//        // 유저를 채널에 추가
//        channelService.addUserToChannel(channel.getId(), user.getId());
//
//        // 메시지 생성
//        Message message = messageService.createMessage(user.getId(), channel.getId(), "Hello, World!");
//        if (message != null) {
//            consoleView.displayMessage(message, channel.getChannelName());
//        }
//
//        // 모든 메시지 출력
//        System.out.println("\n채널의 모든 메시지:");
//        List<Message> messages = messageService.getMessagesByChannel(channel.getId());
//        List<Channel> channels = channelService.getAllChannels();
//        consoleView.displayMessages(messages, channels);
//    }
//}