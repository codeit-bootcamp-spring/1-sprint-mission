package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.config.AppConfig;
import com.sprint.mission.discodeit.dto.channel.ChannelServiceFindAllByUserIdDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelServiceFindDTO;
import com.sprint.mission.discodeit.dto.message.MessageServiceCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageServiceUpdateDTO;
import com.sprint.mission.discodeit.dto.user.UserServiceCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserServiceFindDTO;
import com.sprint.mission.discodeit.dto.user.UserServiceUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;


@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        //SpringApplication.run(DiscodeitApplication.class, args);

        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);


        UUID user1Id = userService.create(new UserServiceCreateDTO("user1", "user1@abc.com", "1234", null));
        UUID user2Id = userService.create(new UserServiceCreateDTO("user2", "user2@abc.com", "1234", null));
        UUID user3Id = userService.create(new UserServiceCreateDTO("user3", "user3@abc.com", "1234", null));
        userService.update(new UserServiceUpdateDTO(user1Id, "editUser1","editUser1@abc.com",null));

        System.out.println("\n\nUser Test");

        //온라인 상태 업데이트
        userService.updateUserOnline(user1Id, UserStatusType.OFFLINE);

        //수정확인
        System.out.println("user 조회 - 수정 확인");
        System.out.println(userService.find(user1Id));
        System.out.println();

        //전체 조회
        System.out.println("user 전체조회 - 삭제확인");
        userService.delete(user1Id);
        for(UserServiceFindDTO dto : userService.findAll()) System.out.println(dto);

        System.out.println("\n\nChannel Test");

        //채널생성(private, public)
        UUID publicChannelId1 = channelService.createPublic("privateChannel1", "공개채널1");
        UUID privateChannelId1 = channelService.createPrivate(user2Id);

        //채널 단건조회
        System.out.println("Channel 단건 조회 - public, private");
        System.out.println(channelService.find(publicChannelId1));
        System.out.println(channelService.find(privateChannelId1));
        System.out.println();


        //특정 사용자가 볼 수 있는 채널 조회
        System.out.println("Channel 특정 사용자가 볼 수 있는 조회");
        for(ChannelServiceFindAllByUserIdDTO dto : channelService.findAllByUserId(user2Id)) System.out.println(dto);
        System.out.println();

        //채널 삭제
        channelService.deleteChannel(privateChannelId1);

        //채널 다건 조회
        System.out.println("Channel 전체 조회 - 채널 삭제 확인");
        for(ChannelServiceFindDTO dto : channelService.findAll()) System.out.println(dto);

        System.out.println("\n\nMessage Test");
        UUID messageId1 = messageService.create(new MessageServiceCreateDTO("메시지1", user2Id, publicChannelId1, null));
        UUID messageId2 = messageService.create(new MessageServiceCreateDTO("메시지2", user3Id, publicChannelId1, null));
        messageService.update(new MessageServiceUpdateDTO(messageId1, "수정된 메시지1"));

        System.out.println("Message 전체 조회 - 등록, 수정 확인");
        for(Message message: messageService.findAll()) System.out.println(message);
        System.out.println();

        System.out.println("Message 특정 채널 메시지 조회");
        for(Message message : messageService.findAllByChannelId(publicChannelId1)) System.out.println(message);
        System.out.println();


    }
}

