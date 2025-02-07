package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.Interface.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 사용자 등록
        User user1 = userService.createUser(new UserCreateRequestDto("Alice", "alice@naver.com", "12345",null, "ONLINE"));;
        User user2 = userService.createUser(new UserCreateRequestDto("Bob", "bob@naver.com", "67890",null,"ONLINE"));
        List<UUID> members=new ArrayList<>();
        members.add(user2.getId());
        displayEntityDetails(user1);
        displayEntityDetails(user2);

        // 채널 등록
        Channel channel1 = channelService.createPublicChannel(new PublicChannelCreateRequestDto( user1.getId(),"general", "General discussion"));
        Channel channel2 = channelService.createPrivateChannel(new PrivateChannelCreateRequestDto(user2.getId(),members));
        displayEntityDetails(channel1);
        displayEntityDetails(channel2);

        // 메시지 등록 (DTO 활용)
        CreateMessageRequestDto messageRequest1 = new CreateMessageRequestDto("Hello everyone!", channel1.getId(), user1.getId());
        CreateMessageRequestDto messageRequest2 = new CreateMessageRequestDto("Random thoughts here.", channel2.getId(), user2.getId());
        Message message1 = messageService.createMessage(messageRequest1);
        Message message2 = messageService.createMessage(messageRequest2);
        displayEntityDetails(message1);
        displayEntityDetails(message2);

        //유저 업데이트
        UserUpdateRequestDto updateRequest=new UserUpdateRequestDto(user1.getId(),"Updatealice",user1.getEmail(),user1.getPassword(),null);
        userService.updateUser(updateRequest);
        System.out.println("유저 업데이트 후 조회");
        displayAllData(userService,channelService,messageService);

        // 메시지 업데이트 (DTO 활용)
        UpdateMessageRequestDto updateMessageRequestDto = new UpdateMessageRequestDto(message1.getId(), "Updated hello message");
        messageService.updateMessage(updateMessageRequestDto);
        System.out.println("메시지 업데이트 후 조회");
        displayAllData(userService, channelService, messageService);

        //채널 업데이트(DTO 할용)
        ChannelUpdateRequestDto updateChannelRequest=new ChannelUpdateRequestDto(channel1.getId(),channel1.getChannelName(),"Update Channel");
        channelService.updateChannel(updateChannelRequest);
        System.out.println("채널 업데이트 후 조회");
        displayAllData(userService, channelService, messageService);

        // 삭제 (연관 데이터 포함)
        userService.deleteUser(user1.getId());
        channelService.deleteChannel(channel1.getId());
        //messageService.deleteMessage(message2.getId());

        System.out.println("삭제 후 전체 조회");
        displayAllData(userService, channelService, messageService);
    }

    private static <T> void displayEntityDetails(T entity) {
        System.out.println(entity.toString());
    }

    private static void displayAllData(UserService userService, ChannelService channelService, MessageService messageService) {
        System.out.println("All Users: " + userService.findAllUsers());
        System.out.println("All Channels: " + channelService.getAllChannels());
        System.out.println("All Messages: " + messageService.getAllMessages());
    }
}
