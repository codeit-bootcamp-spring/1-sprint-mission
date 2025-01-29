package com.sprint.mission.discodeit.app;



import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {
        // 리포지토리 생성
        JCFUserRepository userRepository = new JCFUserRepository();
        JCFChannelRepository channelRepository = new JCFChannelRepository();
        JCFMessageRepository messageRepository = new JCFMessageRepository();

        // 서비스 생성
        JCFUserService userService = JCFUserService.getInstance(userRepository);
        JCFChannelService channelService = JCFChannelService.getInstance(channelRepository, userService);
        JCFMessageService messageService = JCFMessageService.getInstance(messageRepository, userService);

        // 사용자 테스트
        System.out.println("<<<<< 사용자 테스트 >>>>>");
        User user1 = new User("Alice", "alice123");
        User user2 = new User("Bob", "bob456");

        userService.createUser(user1);
        userService.createUser(user2);

        userService.updateUser("alice123", "AliceUpdated");
        userService.deleteUser("bob456");

        userService.readAllUsers().forEach(user -> System.out.println("User: " + user.getUserName()));

        // 채널 테스트
        System.out.println("\n<<<<< 채널 테스트 >>>>>");
        Channel channel1 = new Channel("Tech Talk", "alice123");
        Channel channel2 = new Channel("Gaming Room", "alice123");

        channelService.createChannel(channel1);
        channelService.createChannel(channel2);

        channelService.updateChannel(channel1.getChannelUuid(), "Updated Tech Talk");
        channelService.deleteChannel(channel2.getChannelUuid());

        channelService.readAllChannels().forEach(channel -> System.out.println("Channel: " + channel.getChannelTitle()));

        // 메시지 테스트
        System.out.println("\n<<<<< 메시지 테스트 >>>>>");
        Message message1 = new Message("alice123", "Welcome to Tech Talk!");
        Message message2 = new Message("alice123", "Hello, everyone!");

        messageService.createMessage(message1);
        messageService.createMessage(message2);

        messageService.updateMessage(message1.getMessageUuid(), "Updated Message Text");
        messageService.deleteMessage(message2.getMessageUuid());

        messageService.readAllMessages().forEach(message -> System.out.println("Message: " + message.getMessageText()));
    }

}
