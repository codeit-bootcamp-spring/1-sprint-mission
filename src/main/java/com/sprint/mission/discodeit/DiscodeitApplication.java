package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.Interface.AuthService;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.service.Interface.*;
import com.sprint.mission.discodeit.service.Interface.ReadStatusService;
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
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
        AuthService authService = context.getBean(AuthService.class);

        // 사용자 등록
        User user1 = userService.createUser(new UserCreateRequestDTO("Alice", "alice@naver.com", "12345",null, "ONLINE"));;
        User user2 = userService.createUser(new UserCreateRequestDTO("Bob", "bob@naver.com", "67890",null,"ONLINE"));
        List<UUID> members=new ArrayList<>();
        members.add(user2.getId());
        displayEntityDetails(user1);
        displayEntityDetails(user2);

        // 채널 등록
        Channel channel1 = channelService.createPublicChannel(new PublicChannelCreateRequest( user1.getId(),"general", "General discussion"));
        Channel channel2 = channelService.createPrivateChannel(new PrivateChannelCreateRequest(user2.getId(),members));
        displayEntityDetails(channel1);
        displayEntityDetails(channel2);

        // 메시지 등록 (DTO 활용)
        CreateMessageRequest messageRequest1 = new CreateMessageRequest("Hello everyone!", channel1.getId(), user1.getId());
        CreateMessageRequest messageRequest2 = new CreateMessageRequest("Random thoughts here.", channel2.getId(), user2.getId());
        Message message1 = messageService.createMessage(messageRequest1);
        Message message2 = messageService.createMessage(messageRequest2);
        displayEntityDetails(message1);
        displayEntityDetails(message2);

        //유저 업데이트
        UserUpdateRequestDTO updateRequest=new UserUpdateRequestDTO(user1.getId(),"Updatealice",user1.getEmail(),user1.getPassword(),null);
        userService.updateUser(updateRequest);
        System.out.println("유저 업데이트 후 조회");
        displayAllData(userService,channelService,messageService);

        // 메시지 업데이트 (DTO 활용)
        UpdateMessageRequest updateMessageRequest = new UpdateMessageRequest(message1.getId(), "Updated hello message");
        messageService.updateMessage(updateMessageRequest);
        System.out.println("메시지 업데이트 후 조회");
        displayAllData(userService, channelService, messageService);

        //채널 업데이트(DTO 할용)
        ChannelUpdateRequest updateChannelRequest=new ChannelUpdateRequest(channel1.getId(),channel1.getChannelName(),"Update Channel");
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
