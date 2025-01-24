package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMassageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.text.SimpleDateFormat;
import java.util.*;

public class JavaApplication {
    public static void main(String[] args) {
        // 서비스 초기화
        UserRepository userRepository = new JCFUserRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();
        MessageRepository messageRepository = new JCFMessageRepository();

//        UserRepository userRepository = new FileUserRepository();
//        ChannelRepository channelRepository = new FileChannelRepository();
//        MessageRepository messageRepository = new FileMessageRepository();

        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMassageService(messageRepository);

        // 사용자, 채널, 메시지 등록
        System.out.println("등록");
        User userTest = new User("Alice", "alice@naver.com", "12345");
        userService.createUser(userTest);
        displayEntityDetails(userService, userTest);

        Channel channelTest = new Channel("testChannel", "test_description", userTest);
        channelService.createChannel(channelTest);
        displayEntityDetails(channelService, channelTest);

        Message messageTest = new Message("test_message", channelTest, userTest);
        messageService.createMessage(messageTest);
        displayEntityDetails(messageService, messageTest);
        System.out.println();

        // 데이터 추가 및 전체 조회
        System.out.println("데이터 추가 및 전체 조회");
        addTestData(userService, channelService, messageService);
        displayAllData(userService, channelService, messageService);
        System.out.println();

        // 데이터 업데이트
        updateEntity(userService, userTest, "UpdatedAlice", "alice@naver.com", "1234567");
        updateEntity(channelService, channelTest, "updateChannel", "update_test_description", userTest);
        updateEntity(messageService, messageTest, "update_message", userTest, channelTest);
        System.out.println();

        // 데이터 삭제 및 결과 확인
        userService.deleteUser(userTest.getId());
        channelService.deleteChannel(channelTest.getId());
        messageService.deleteMessage(messageTest.getId());

        System.out.println("삭제 후 전체 조회");
        displayAllData(userService, channelService, messageService);
    }

    // 공통된 엔티티 출력 로직을 한 메서드로 통합
    private static <T> void displayEntityDetails(Object service, T entity) {
        String entityDetails = entity.toString();  // entity에 대한 기본 출력 (필요 시 추가로 포맷 가능)
        System.out.println(entityDetails);
    }

    private static void addTestData(UserService userService, ChannelService channelService, MessageService messageService) {
        User user2 = new User("Bob", "bob@naver.com", "12345");
        User user3 = new User("James", "james@naver.com", "12345");

        userService.createUser(user2);
        userService.createUser(user3);

        Channel channel2 = new Channel("test2Channel", "test_description", user2);
        Channel channel3 = new Channel("test3Channel", "test_description", user3);

        channelService.createChannel(channel2);
        channelService.createChannel(channel3);

        Message message2 = new Message("test_message2", channel2, user2);
        Message message3 = new Message("test_message3", channel3, user3);

        messageService.createMessage(message2);
        messageService.createMessage(message3);
    }

    private static void displayAllData(UserService userService, ChannelService channelService, MessageService messageService) {
        System.out.println("All Users: " + userService.getAllUsers());
        System.out.println("All Channels: " + channelService.getAllChannels());
        System.out.println("All Messages: " + messageService.getAllMessage());
    }

    private static void updateEntity(UserService userService, User user, String newName, String newEmail, String newPassword) {
        userService.updateUser(user.getId(), new User(newName, newEmail, newPassword));
        System.out.println("Updated User: " + newName);
    }

    private static void updateEntity(ChannelService channelService, Channel channel, String newChannelName, String newDescription, User newCreator) {
        channelService.updateChannel(channel.getId(), new Channel(newChannelName, newDescription, newCreator));
        System.out.println("Updated Channel: " + newChannelName);
    }

    private static void updateEntity(MessageService messageService, Message message, String newContent, User newSender, Channel newChannel) {
        messageService.updateMessage(message.getId(), new Message(newContent, newChannel, newSender));
        System.out.println("Updated Message: " + newContent);
    }

    // 포맷된 날짜 출력
    private static String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
