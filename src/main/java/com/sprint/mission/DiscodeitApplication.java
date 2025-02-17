package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Dto.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    static void RepositoryClear(){
        FileIOHandler fileIOHandler = FileIOHandler.getInstance();
        try{fileIOHandler.serializeHashMap(new HashMap<UUID, Channel>(), "Channel\\mainOIChannelRepository");
            fileIOHandler.serializeHashMap(new HashMap<UUID, User>(), "User\\mainOIUserRepository");
            fileIOHandler.serializeHashMap(new HashMap<UUID, Message>(), "Message\\mainOIMessageRepository");
            fileIOHandler.serializeHashMap(new HashMap<UUID, UserStatus>(), "UserStatus\\mainOIUserStatusRepository");
        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
    }

    static UserDto setupUser(UserService userService) {
        UUID user1 = userService.createUser("woody", "woody@codeit.com", "woody1234");
        return userService.findUserById(user1);
    }

    static ChannelDto setupChannel(ChannelService channelService) {
        UUID channel1 = channelService.createPublicChannel("공지", "공지 채널입니다.");
        return channelService.findChannelById(channel1);
    }

    static void messageCreateTest(MessageService messageService, ChannelDto channel, UserDto author) {
        UUID message1 = messageService.createMessage(author.id(), channel.id(),"안녕하세요.");
        System.out.println("메시지 생성: " + message1);
    }


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);


        RepositoryClear();


        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);


        // 셋업
        UserDto user = setupUser(userService);
        ChannelDto channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);


    }
}