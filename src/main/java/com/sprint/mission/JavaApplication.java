package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ServiceFactory;
import com.sprint.mission.discodeit.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class JavaApplication {
    public static void main(String[] args) throws InterruptedException {
        //가독성을 위해 수정
        // 서비스 초기화
        UserService userService = ServiceFactory.getUserService();
        ChannelService channelService = ServiceFactory.getChannelService();
        MessageService messageService = ServiceFactory.getMessageService();

        // 사용자, 채널, 메시지 등록
        System.out.println("등록");
        User userTest = new User("Alice", "12345");
        userService.createUser(userTest);
        displayUserDetails(userService, userTest);

        Channel channelTest = new Channel("testChannel", "test_description", userTest);
        channelService.createChannel(channelTest);
        displayChannelDetails(channelService, channelTest);

        Message messageTest = new Message("test_message", userTest);
        messageService.createMessage(messageTest);
        displayMessageDetails(messageService, messageTest);
        System.out.println();

        // 사용자 검증 실패 테스트
        System.out.println("사용자 검증 실패 테스트");
        try {
            User invalidUser = new User("InvalidUser", "12345");
            Channel invalidChannel = new Channel("invalidChannel", "description", invalidUser);
            channelService.createChannel(invalidChannel);  // 예외 발생 예상
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());  // 예외 메시지 출력
        }

        // 메시지 검증 실패 테스트
        try {
            User invalidSender = new User("InvalidUser", "12345");
            Message invalidMessage = new Message("invalid_message", invalidSender);
            messageService.createMessage(invalidMessage);  // 예외 발생 예상
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());  // 예외 메시지 출력
        }


        // 데이터 추가 및 전체 조회
        System.out.println("데이터 추가 및 전체 조회");
        addTestData(userService, channelService, messageService);
        displayAllData(userService, channelService, messageService);
        System.out.println();

        // 데이터 업데이트
        Thread.sleep(2000); // 업데이트 시간 차이 확인용 딜레이
        System.out.println("업데이트");

        updateUser(userService, userTest, "UpdatedAlice", "1234567");
        System.out.println();

        System.out.println("업데이트 후 조회");
        displayUserDetails(userService, userTest);
        displayChannelDetails(channelService, channelTest);
        displayMessageDetails(messageService, messageTest);
        System.out.println();

        updateChannel(channelService, channelTest, "updateChannel", "update_test_description", userTest);
        displayUserDetails(userService, userTest);
        displayChannelDetails(channelService, channelTest);
        displayMessageDetails(messageService, messageTest);
        System.out.println();

        updateMessage(messageService, messageTest, "update_message", userTest);
        displayUserDetails(userService, userTest);
        displayChannelDetails(channelService, channelTest);
        displayMessageDetails(messageService, messageTest);
        System.out.println();
        // 데이터 삭제 및 결과 확인
        userService.deleteUser(userTest.getId());
        channelService.deleteChannel(channelTest.getId());
        messageService.deleteMessage(messageTest.getId());

        System.out.println("삭제 후 전체 조회");
        displayAllData(userService, channelService, messageService);
        System.out.println();
    }

    private static void displayUserDetails(UserService userService, User user) {
        Optional<User> userById = userService.getUserById(user.getId());
        userById.ifPresent(u -> {
            System.out.println("User: " + u.getName());
            System.out.println("Created At: " + formatDate(u.getCreatedAt()));
        });
    }

    private static void displayChannelDetails(ChannelService channelService, Channel channel) {
        Optional<Channel> channelById = channelService.getChannelById(channel.getId());
        channelById.ifPresent(c -> {
            System.out.println("Channel: " + c.getChannel());
            System.out.println("Created By: " + c.getCreator());
            System.out.println("Created At: " + formatDate(c.getCreatedAt()));
        });
    }

    private static void displayMessageDetails(MessageService messageService, Message message) {
        Optional<Message> messageById = messageService.getMessageById(message.getId());
        messageById.ifPresent(m -> {
            System.out.println("Message: " + m.getContent());
            System.out.println("Sent By: " + m.getSenderUser());
            System.out.println("Created At: " + formatDate(m.getCreatedAt()));
        });
    }

    private static void addTestData(UserService userService, ChannelService channelService, MessageService messageService) {
        User user2 = new User("Bob", "12345");
        User user3 = new User("James", "12345");

        userService.createUser(user2);
        userService.createUser(user3);

        Channel channel2 = new Channel("test2Channel", "test_description", user2);
        Channel channel3 = new Channel("test3Channel", "test_description", user3);

        channelService.createChannel(channel2);
        channelService.createChannel(channel3);

        Message message2 = new Message("test_message2", user2);
        Message message3 = new Message("test_message3", user3);

        messageService.createMessage(message2);
        messageService.createMessage(message3);
    }

    private static void displayAllData(UserService userService, ChannelService channelService, MessageService messageService) {
        List<User> allUsers = userService.getAllUsers();
        System.out.println("All Users: " + allUsers);

        List<Channel> allChannels = channelService.getAllChannels();
        System.out.println("All Channels: " + allChannels);

        List<Message> allMessages = messageService.getAllMessage();
        System.out.println("All Messages: " + allMessages);
    }

    private static void updateUser(UserService userService, User user, String newName, String newPassword) {
        User updatedUser = new User(newName, newPassword);
        userService.updateUser(user.getId(), updatedUser);
        System.out.println("Updated User: " + updatedUser.getName());
        System.out.println("Updated At: " + formatDate(updatedUser.getUpdatedAt()));
    }

    private static void updateChannel(ChannelService channelService, Channel channel, String newChannelName, String newDescription, User newCreator) {
        Channel updatedChannel = new Channel(newChannelName, newDescription, newCreator);
        channelService.updateChannel(channel.getId(), updatedChannel);
        System.out.println("Updated Channel: " + updatedChannel.getChannel());
        System.out.println("Updated At: " + formatDate(updatedChannel.getUpdatedAt()));
    }

    private static void updateMessage(MessageService messageService, Message message, String newContent, User newSender) {
        Message updatedMessage = new Message(newContent, newSender);
        messageService.updateMessage(message.getId(), updatedMessage);
        System.out.println("Updated Message: " + updatedMessage.getContent());
        System.out.println("Updated At: " + formatDate(updatedMessage.getUpdatedAt()));
    }

    private static String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
