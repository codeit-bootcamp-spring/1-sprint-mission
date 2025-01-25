package com.sprint.mission.discodeit.app;



import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class JavaApplication {
    public static void main(String[] args) {
        // File 기반 레포지토리 주입
        FileUserRepository userRepository = new FileUserRepository();
        FileChannelRepository channelRepository = new FileChannelRepository();
        FileMessageRepository messageRepository = new FileMessageRepository();

        // 서비스 계층에 주입
        FileUserService userService = FileUserService.getInstance(userRepository);
        FileChannelService channelService = FileChannelService.getInstance(channelRepository, userService);
        FileMessageService messageService = FileMessageService.getInstance(messageRepository, userService);

        //user 객체 생성, user 생성
        User user1 = new User("Alice", "alice123");
        User user2 = new User("Bob", "bob456");
        User user3 = new User("Jack", "jack1234");

        //사용자 생성
        System.out.println(" <<<< 유저 생성 >>>> ");
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        //사용자 업데이트
        userService.updateUser(user1.getUserId(), "kbh");
        //사용자 삭제
        userService.deleteUser(user3.getUserId());
        System.out.println();

        // 사용자 출력
        System.out.println(" <<<< All Users >>>>");
        userService.readAllUsers().forEach(user -> System.out.println(user.getUserName()));
        System.out.println();

        // 채널 생성
        Channel channel1 = new Channel("Tech Talk", "alice123");
        Channel channel2 = new Channel("Gaming", "bob456");
        System.out.println(" <<<< 채널 생성 >>>> ");
        channelService.createChannel(channel1);
        channelService.createChannel(channel2);
        //채널 업데이트
        channelService.updateChannel(channel1.getChannelUuid(), "Student");
        System.out.println();

        // 채널 출력
        System.out.println(" <<<< All Channels >>>> ");
        channelService.readAllChannels().forEach(channel -> System.out.println(channel.getChannelTitle()));
        System.out.println();

        //채널 삭제 , 중복 확인
        System.out.println(" <<<< 채널 삭제 >>>> ");
        channelService.deleteChannel(channel1.getChannelUuid());
        channelService.deleteChannel(channel1.getChannelUuid());
        channelService.deleteChannel(channel2.getChannelUuid());
        System.out.println();

        // 메시지 생성
        System.out.println(" <<<< 메세지 생성 >>>> ");
        Message user1Message1 = new Message("alice123", "Welcome to Tech Talk!");
        Message user2Message1 = new Message("bob456", "Let’s talk about gaming.");
        messageService.createMessage(user1Message1);
        messageService.createMessage(user2Message1);
        //메세지 업데이트
        messageService.updateMessage(user1Message1.getMessageUuid(), "Hello!");
        System.out.println();

        //메시지 출력
        System.out.println(" <<<< All Messages >>>> ");
        messageService.readAllMessages().forEach(message -> System.out.println(message.getMessageText()));
        System.out.println();


        //메세지 삭제
        System.out.println(" <<<< 메세지 삭제 >>>> ");
        messageService.deleteMessage(user1Message1.getMessageUuid());
        messageService.deleteMessage(user2Message1.getMessageUuid());
        System.out.println();


        System.out.println(" <<<< All Messages >>>>");
        userService.readAllUsers().forEach(user -> System.out.println(user.getUserName()));

        System.out.println(" <<<< All Channels >>>>");
        channelService.readAllChannels().forEach(channel -> System.out.println(channel.getChannelTitle()));

        System.out.println(" <<<< All Messages >>>>");
        messageService.readAllMessages().forEach(message -> System.out.println(message.getMessageText()));


    }

}
