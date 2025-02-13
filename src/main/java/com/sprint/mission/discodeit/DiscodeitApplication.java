package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        // ✅ Spring IoC 컨테이너에서 ApplicationContext 가져오기
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
        System.out.println("🚀 DiscodeitApplication 실행 완료!");

        // ✅ IoC 컨테이너에서 필요한 Bean을 가져와 사용
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);

        // ✅ 애플리케이션 실행 로직 실행
        runApplication(userService, channelService, messageService, readStatusService);
    }

    private static void runApplication(
            UserService userService,
            ChannelService channelService,
            MessageService messageService,
            ReadStatusService readStatusService
    ) {
        System.out.println("✅ CommandLineRunner 실행됨!");

        // ✅ User 생성
        UserCreateDTO userDTO = new UserCreateDTO("Amy", "amy@example.com", null, "password123");
        userService.create(userDTO);

        // ✅ User 조회
        List<UserReadDTO> users = userService.readAll();
        if (users.isEmpty()) {
            throw new RuntimeException("🚨 사용자 생성 실패: User 목록이 비어 있음");
        }
        UserReadDTO createdUser = users.stream()
                .filter(u -> u.getEmail().equals(userDTO.getEmail()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("🚨 사용자 생성 실패: Email 일치 사용자 없음"));

        UUID userId = createdUser.getId();
        System.out.printf("✅ 사용자 등록 완료: %s (%s)%n", createdUser.getUsername(), createdUser.getEmail());

        // ✅ 채널 생성
        UUID channelId = UUID.randomUUID();
        ChannelCreateDTO channelCreateDTO = new ChannelCreateDTO("Second 채널", "업데이트된 채널 설명", userId);
        channelService.createPublicChannel(channelCreateDTO);
        System.out.printf("✅ 채널 등록 완료: %s (설명: %s)%n", channelCreateDTO.getName(), channelCreateDTO.getDescription());

        // ✅ 메시지 생성
        MessageCreateDTO messageCreateDTO = new MessageCreateDTO(userId, channelId, "안녕하세요, 첫 번째 메시지입니다!", null);
        messageService.create(messageCreateDTO);
        System.out.printf("✅ 메시지 등록 완료: %s (보낸이: %s, 채널: %s)%n",
                messageCreateDTO.getContent(), createdUser.getUsername(), channelCreateDTO.getName());

        // ✅ 메시지 전체 조회
        List<MessageDTO> messages = messageService.readAll();
        System.out.println("📌 현재 등록된 메시지 목록:");
        messages.forEach(m -> System.out.printf("   - [%s] %s (by %s)%n",
                m.getChannelId(), m.getContent(), createdUser.getUsername()));
    }
}
