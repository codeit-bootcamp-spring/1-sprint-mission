package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscodeitApplication.class, args);


        //ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class);

/*        UserService userService = context.getBean(UserService.class);
        UserStatusService userStatusService = context.getBean(UserStatusService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);


        UUID user1Id = userService.create(new UserCreateDTO("user1", "user1@abc.com", "1234", null));
        UUID user2Id = userService.create(new UserCreateDTO("user2", "user2@abc.com", "1234", null));
        UUID user3Id = userService.create(new UserCreateDTO("user3", "user3@abc.com", "1234", null));
        userService.update(user1Id, new UserUpdateDTO("editUser1","editUser1@abc.com","editPassword", null));

        System.out.println("\n\nUser Test");

        //온라인 상태 업데이트
        userStatusService.update(user2Id, new UserStatusUpdateDTO(Instant.now()));

        //수정확인
        System.out.println("user 조회 - 수정 확인");
        System.out.println(userService.find(user1Id));
        System.out.println();

        //전체 조회
        System.out.println("user 전체조회 - 삭제, 온라인상태 업데이트확인");
        userService.delete(user1Id);
        for(UserFindDTO dto : userService.findAll()) System.out.println(dto);

        System.out.println("\n\nChannel Test");

        //채널생성(private, public)
        UUID publicChannelId1 = channelService.create(new ChannelCreatePublicDTO("privateChannel1", "공개채널1"));
        UUID privateChannelId1 = channelService.create(new ChannelCreatePrivateDTO(List.of(user2Id)));

        //채널 단건조회
        System.out.println("Channel 단건 조회 - public, private");
        System.out.println(channelService.find(publicChannelId1));
        System.out.println(channelService.find(privateChannelId1));
        System.out.println();


        //특정 사용자가 볼 수 있는 채널 조회
        System.out.println("Channel 특정 사용자가 볼 수 있는 조회");
        for(ChannelFindDTO dto : channelService.findAllByUserId(user2Id)) System.out.println(dto);
        System.out.println();

        //채널 삭제
        //channelService.deleteChannel(privateChannelId1);


        System.out.println("\n\nMessage Test");
        UUID messageId1 = messageService.create(new MessageCreateDTO("메시지1", user2Id, publicChannelId1, null));
        UUID messageId2 = messageService.create(new MessageCreateDTO("메시지2", user3Id, publicChannelId1, null));
        messageService.update(messageId1, new MessageUpdateDTO("수정된 메시지1"));

        System.out.println("Message 전체 조회 - 등록, 수정 확인");
        for(Message message: messageService.findAll()) System.out.println(message);
        System.out.println();

        System.out.println("Message 특정 채널 메시지 조회");
        for(Message message : messageService.findAllByChannelId(publicChannelId1)) System.out.println(message);
        System.out.println();

        System.out.println("ReadStatus 조회");
        for(ReadStatus readStatus : readStatusService.findAll()) System.out.println(readStatus);*/

    }
}

