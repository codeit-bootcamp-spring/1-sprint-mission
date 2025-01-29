package com.sprint.mission.discodeit.app;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
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
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

public class JavaApplication {

    public static void main(String[] args) {
        // User
        Path userDirectory = Paths.get(System.getProperty("user.dir"), "temp/user");

//        UserService userService = new FileUserService(userDirectory);
//        UserService userService = new JCFUserService();
        UserRepository userRepository = new FileUserRepository(userDirectory);
        UserRepository jcfUserRepository = new JCFUserRepository();
        UserService userService = new BasicUserService(userRepository);

        // 사용자 생성
        Long userId = userService.createUserData("username1", "nickname1", "email@mail.com", "password!1234", "KR", "010-1111-2222", null);
        System.out.println("User created with ID: " + userId);
        userId = userService.createUserData("username2", "nickname2", "email2@mail.com", "password!5678", "KR", "010-2222-2222", null);
        System.out.println("User created with ID: " + userId);
        userId = userService.createUserData("username3", "nickname3", "email3@mail.com", "password!1234", "KR", "010-2222-3333", "TEST.png");
        System.out.println("User created with ID: " + userId);
        userId = userService.createUserData("username4", "nickname4", "email4@mail.com", "password!3456", "KR", "010-4444-2222", "GOOd.png");
        System.out.println("User created with ID: " + userId);
        userId = userService.createUserData("username5", "nickname5", "email5@mail.com", "password!0987", "KR", "010-5555-2222", "HAPPY.png");
        System.out.println("User created with ID: " + userId);

        // 사용자 조회 (id)
        UserResDTO user = userService.getUser(userId);
        System.out.println(user);

        System.out.println("===========================================");

        // 사용자 조회 (닉네임)
        user = userService.getUser("username2");
        System.out.println(user);

        System.out.println("===========================================");


        // 사용자 정보 갱신
        boolean updateFlag = userService.updateUser(userId,
                new UserUpdateDTO(null, "changeNickname", null, null,
                        null, null, "TTT.jpeg", null));
        if (updateFlag) {
            // update가 되었다면
            user = userService.getUser(userId);
            System.out.println("[UPDATE]\n" + user);
        } else {
            System.out.println("[UPDATE]\n수정된 내용이 존재하지 않습니다.");
        }

        System.out.println("===========================================");

        // 모든 사용자 조회
        System.out.println("전체 조회");
        userService.getAllUser().forEach(System.out::println);

        System.out.println("===========================================");

        // 사용자 삭제
        UserResDTO deletedUser = userService.deleteUser(userId);
        System.out.println("[DELETED]\n" + deletedUser);

        // 삭제 후 모든 사용자 조회
        System.out.println("[AFTER DELETED]");
        userService.getAllUser().forEach(System.out::println);

        System.out.println("===========================================");

        // Channel
        Path channelDirectory = Paths.get(System.getProperty("user.dir"), "temp/channel");
//        ChannelService channelService = new FileChannelService(channelDirectory);
//        ChannelService channelService = new JCFChannelService();
        ChannelRepository channelRepository = new FileChannelRepository(channelDirectory);
        ChannelRepository jcfChannelRepository = new JCFChannelRepository();
        ChannelService channelService = new BasicChannelService(channelRepository);

        Optional<Map.Entry<Long, User>> owner = userService.findUserByUserName("username1");
        Optional<Map.Entry<Long, User>> owner2 = userService.findUserByUserName("username3");

        Long channelId = channelService.createChannel(owner.get().getValue(), "server1", null, null);
        System.out.println("Channel created with ID: " + channelId);

        channelId = channelService.createChannel(owner.get().getValue(), "server2", null, "ICON.jpg");
        System.out.println("Channel created with ID: " + channelId);

        channelId = channelService.createChannel(owner2.get().getValue(), "server3", "TEST", null);
        System.out.println("Channel created with ID: " + channelId);

        channelId = channelService.createChannel(owner2.get().getValue(), "server9", "TTT", "ICONID.png");
        System.out.println("Channel created with ID: " + channelId);

        channelId = channelService.createChannel(owner2.get().getValue(), "나는 서버다", "나는 서버다앙", "server.jpeg");
        System.out.println("Channel created with ID: " + channelId);

        System.out.println("채널 생성 완료!");

        System.out.println("===========================================");

        ChannelResDTO channel = channelService.getChannel(channelId);
        System.out.println(channel);

        System.out.println("===========================================");

        System.out.println("전체 조회");
        channelService.getAllChannel().forEach(System.out::println);

        System.out.println("===========================================");

        // 정보 갱신
        updateFlag = channelService.updateChannelInfo(channelId,
                new ChannelUpdateDTO(null, "sssssssss서버", "HAPPY", null));
        if (updateFlag) {
            // update가 되었다면
            channel = channelService.getChannel(channelId);
            System.out.println("[UPDATE]\n" + channel);
        } else {
            System.out.println("[UPDATE]\n수정된 내용이 존재하지 않습니다.");
        }
        System.out.println("[AFTER UPDATE]");
        channelService.getAllChannel().forEach(System.out::println);

        System.out.println("===========================================");

        // 삭제
        ChannelResDTO deletedChannel = channelService.deleteChannel(channelId);
        System.out.println("[DELETED]\n" + deletedChannel);

        // 삭제 후 전체 조회
        System.out.println("[AFTER DELETED]");
        channelService.getAllChannel().forEach(System.out::println);

        System.out.println("===========================================");

        // Message
        Path messageDirectory = Paths.get(System.getProperty("user.dir"), "temp/message");
//        MessageService messageService = new FileMessageService(messageDirectory);
//        MessageService messageService = new JCFMessageService();
        MessageRepository messageRepository = new FileMessageRepository(messageDirectory);
        MessageRepository jcfMessageRepository = new JCFMessageRepository();
        MessageService messageService = new BasicMessageService(messageRepository);

        User author1 = userService.findUserById(1L);
        Channel channel1 = channelService.findChannelById(1L);

        Long msgId = messageService.createMessage(author1, channel1, "메시지1");
        System.out.println("Message created with ID: " + msgId);

        msgId = messageService.createMessage(author1, channel1, "메시지2");
        System.out.println("Message created with ID: " + msgId);

        msgId = messageService.createMessage(author1, channel1, "메시지3");
        System.out.println("Message created with ID: " + msgId);

        msgId = messageService.createMessage(author1, channel1, "메시지4");
        System.out.println("Message created with ID: " + msgId);

        msgId = messageService.createMessage(author1, channel1, "메시지5");
        System.out.println("Message created with ID: " + msgId);

        System.out.println("메시지 작성 완료!");

        System.out.println("===========================================");

        MessageResDTO msg = messageService.getMessage(msgId);
        System.out.println(msg);

        System.out.println("===========================================");

        System.out.println("전체 조회");
        messageService.getAllMessage().forEach(System.out::println);

        // 정보 갱신
        updateFlag = messageService.updateMessage(msgId, new MessageUpdateDTO("HAPPy~"));
        if (updateFlag) {
            // update가 되었다면
            msg = messageService.getMessage(msgId);
            System.out.println("[UPDATE]\n" + msg);
        } else {
            System.out.println("[UPDATE]\n수정된 내용이 존재하지 않습니다.");
        }
        System.out.println("[AFTER UPDATE]");
        channelService.getAllChannel().forEach(System.out::println);

        System.out.println("===========================================");

        // 삭제
        MessageResDTO deletedMessage = messageService.deleteMessage(msgId);
        System.out.println("[DELETED]\n" + deletedMessage);

        // 삭제 후 전체 조회
        System.out.println("[AFTER DELETED]");
        messageService.getAllMessage().forEach(System.out::println);

        System.out.println("===========================================");

    }
}
