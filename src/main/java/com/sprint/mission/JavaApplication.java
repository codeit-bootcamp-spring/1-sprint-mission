package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDTO;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.Interface.AuthService;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.service.Interface.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.UserStatusType.*;

@SpringBootApplication
public class JavaApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 객체들 주입
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
        AuthService authService = context.getBean(AuthService.class);

        // 1. 사용자 등록
        User user1 = userService.createUser(new UserCreateRequestDTO("Alice", "alice@naver.com", "12345", null, "ONLINE"));
        User user2 = userService.createUser(new UserCreateRequestDTO("Bob", "bob@naver.com", "67890", null, "ONLINE"));
        displayEntityDetails(user1);
        displayEntityDetails(user2);

        // 2. 채널 등록
        List<UUID> members = new ArrayList<>();
        members.add(user2.getId());
        Channel channel1 = channelService.createPublicChannel(new PublicChannelCreateRequest(user1.getId(), "general", "General discussion"));
        Channel channel2 = channelService.createPrivateChannel(new PrivateChannelCreateRequest(user1.getId(), members));
        displayEntityDetails(channel1);
        displayEntityDetails(channel2);

        // 3. 메시지 등록
        CreateMessageRequest messageRequest1 = new CreateMessageRequest("Hello everyone!", channel1.getId(), user1.getId());
        CreateMessageRequest messageRequest2 = new CreateMessageRequest("Random thoughts here.", channel2.getId(), user2.getId());
        Message message1 = messageService.createMessage(messageRequest1);
        Message message2 = messageService.createMessage(messageRequest2);
        displayEntityDetails(message1);
        displayEntityDetails(message2);

        // 4. 메시지 업데이트
        UpdateMessageRequest updateRequest = new UpdateMessageRequest(message1.getId(), "Updated hello message");
        messageService.updateMessage(updateRequest);
        System.out.println("업데이트 후 조회");
        displayEntityDetails(message1);

        // 5. 유저 상태 변경 (오프라인으로 전환)
       // userService.updateUserStatus(user2.getId(), OFFLINE);
      //  System.out.println("Bob의 상태: " + userService.getUserById(user2.getId()).get().getStatus());  // OFFLINE

        // 6. 유저 삭제 및 관련 데이터 삭제
        userService.deleteUser(user1.getId());
        channelService.deleteChannel(channel1.getId());
        messageService.deleteMessage(message2.getId());
        System.out.println("삭제 후 전체 조회");
        // displayAllData(userService, channelService, messageService);
    }

    private static <T> void displayEntityDetails(T entity) {
        System.out.println(entity.toString());
    }

    // 전체 데이터를 확인하는 메서드 (주석 처리된 코드)
    /*
    private static void displayAllData(UserService userService, ChannelService channelService, MessageService messageService) {
        System.out.println("All Users: " + userService.getAllUsers());
        System.out.println("All Channels: " + channelService.findAllChannels());
        System.out.println("All Messages: " + messageService.findAllMessages());
    }
    */
}
