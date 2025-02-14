package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.basic.BasicUserStatusService;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Instant;
import java.util.Map;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        runTest(context);

        System.out.println("Spring Boot Application 실행 완료");
    }

    private static void runTest(ApplicationContext context) {
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        AuthService authService = context.getBean(AuthService.class);
        UserStatusService userStatusService = context.getBean(UserStatusService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);

        UserDTO userDTO = new UserDTO("TestUser", "test@email.com", "password1234");
        UserDTO userDTO2 = new UserDTO("TestUser2", "test2@email.com", "password1234");
        User createdUser = userService.create(userDTO, null);
        User createdUser2 = userService.create(userDTO2, null);
        System.out.println("[회원가입] 유저 생성: " + createdUser.getName() + " | " + createdUser.getEmail());
        System.out.println("[회원가입] 유저 생성: " + createdUser2.getName() + " | " + createdUser2.getEmail());

        AuthDTO authDTO = new AuthDTO("TestUser", "password1234");
        AuthDTO authDTO2 = new AuthDTO("TestUser2", "password1234");
        UserDTO loggedInUser = authService.login(authDTO);
        UserDTO loggedInUser2 = authService.login(authDTO2);
        System.out.println("[로그인] 유저 로그인 성공: " + loggedInUser.getName() + ", " + loggedInUser2.getName());

        UserStatusDTO userStatus = userStatusService.create(new UserStatusDTO(createdUser.getId(), Instant.now()));
        UserStatusDTO userStatus2 = userStatusService.create(new UserStatusDTO(createdUser2.getId(), Instant.now()));
        System.out.println("[유저 상태] 온라인 상태 업데이트 완료: " + userStatus.getUserId() + ", " + userStatus2.getUserId());

        UserStatusType currentStatus = userStatusService.getUserOnlineStatus(createdUser.getId());
        UserStatusType currentStatus2 = userStatusService.getUserOnlineStatus(createdUser2.getId());
        System.out.println("[유저 온라인 상태]: " + userDTO.getName() + ", " + userDTO2.getName() + " 현재 상태: " + currentStatus);

        ChannelDTO channelDTO = new ChannelDTO("PUBLIC CHANNEL", "오픈 채널입니다.", "PUBLIC");
        Channel createdChannel = channelService.create(channelDTO);
        System.out.println("[채널 생성] 채널명: " + createdChannel.getName() + " | 타입: " + createdChannel.getType());

        System.out.println(createdChannel);

        ChannelJoinDTO joinDTO = new ChannelJoinDTO(createdUser.getId(), createdChannel.getId());
        ChannelJoinDTO joinDTO2 = new ChannelJoinDTO(createdUser2.getId(), createdChannel.getId());
        Map<User, Channel> result = channelService.join(joinDTO);
        Map<User, Channel> result2 = channelService.join(joinDTO2);
        System.out.println("[채널 가입] 채널 명 : " + createdChannel.getName() + " | 멤버 : " +result  + ", " + result2);

        MessageDTO messageDTO = new MessageDTO("안녕하세요! 채팅 시작합니다.", createdChannel.getId(), createdUser.getId());
        Message createdMessage = messageService.create(messageDTO);
        System.out.println("[메시지 전송]: " + "채널명: "+ createdChannel.getName() + " 보낸 유저: " + createdUser.getName() + " | 내용: " + createdMessage.getContent());

        ReadStatusDTO readStatus = readStatusService.create(new ReadStatusDTO(createdUser.getId(), createdChannel.getId(), Instant.now()));
        System.out.println("[읽음 상태] 유저가 마지막으로 읽은 시간 업데이트: " + readStatus.getLastReadTime() + ", 읽었는지 안읽었는지 : " + readStatusService.readOnOff(createdUser.getId()));

        UserStatusDTO foundUserStatus = userStatusService.find(createdUser.getId());
        System.out.println("[유저 상태 조회] 마지막 접속 시간: " + foundUserStatus.getLastSeen());

        UserDTO updatedUserDTO = new UserDTO("UpdatedUser", "updated@email.com", "newPassword5678");
        User updatedUser = userService.update(createdUser.getId(), updatedUserDTO, null);
        System.out.println("[유저 수정] 변경된 유저: " + updatedUser.getName() + " | " + updatedUser.getEmail());

        ChannelDTO updatedChannelDTO = new ChannelDTO("PRIVATE CHANNEL", "비밀 채널입니다.", "PRIVATE");
        Channel updatedChannel = channelService.update(createdChannel.getId(), updatedChannelDTO);
        System.out.println("[채널 수정] 변경된 채널명: " + updatedChannel.getName() + " | " + updatedChannel.getDescription());

        MessageDTO updatedMessageDTO = new MessageDTO("수정된 메시지입니다.", createdChannel.getId(), createdUser.getId());
        Message updatedMessage = messageService.update(createdMessage.getId(), updatedMessageDTO);
        System.out.println("[메시지 수정] 변경된 내용: " + updatedMessage.getContent());

        userService.delete(createdUser.getId());
        System.out.println("[유저 삭제] 유저 삭제 완료");

        channelService.delete(createdChannel.getId());
        System.out.println("[채널 삭제] 채널 삭제 완료");

        messageService.delete(createdMessage.getId());
        System.out.println("[메시지 삭제] 메시지 삭제 완료");

        readStatusService.delete(createdUser.getId(), createdChannel.getId());
        System.out.println("[읽음 상태 삭제] 유저 읽음 기록 삭제 완료");

        userStatusService.delete(createdUser.getId());
        System.out.println("[유저 상태 삭제] 유저 상태 정보 삭제 완료");
    }
}