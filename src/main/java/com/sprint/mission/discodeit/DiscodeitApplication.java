package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableJpaRepositories("com.sprint.mission.discodeit.repository")
public class DiscodeitApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
        System.out.println("🚀 DiscodeitApplication 실행 완료!");

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);

        runApplication(userService, channelService, messageService, readStatusService);
    }

    private static void runApplication(
            UserService userService,
            ChannelService channelService,
            MessageService messageService,
            ReadStatusService readStatusService
    ) {
        System.out.println("✅ CommandLineRunner 실행됨!");

        UserCreateDTO userDTO = new UserCreateDTO("Amy", "amy@example.com", null, "password123");
        userService.create(userDTO);

        List<UserReadDTO> users = userService.readAll();
        if (users.isEmpty()) {
            throw new RuntimeException("🚨 사용자 생성 실패: User 목록이 비어 있음");
        }

        UserReadDTO createdUser = users.stream()
                .filter(u -> u.getEmail().equals(userDTO.getEmail()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("🚨 사용자 생성 실패: Email 일치 사용자 없음"));

        UUID userId = createdUser.getId();

        ChannelCreateDTO channelCreateDTO = new ChannelCreateDTO(
                "Second 채널", "업데이트된 채널 설명", userId, false, List.of()
        );
        channelService.createChannel(channelCreateDTO);

        MessageCreateDTO messageCreateDTO = new MessageCreateDTO(userId, UUID.randomUUID(), "안녕하세요, 첫 번째 메시지입니다!");
        messageService.create(messageCreateDTO);

        System.out.println("📌 현재 등록된 메시지 목록:");
        List<MessageDTO> messages = messageService.readAllByChannel(UUID.randomUUID());
        messages.forEach(m -> System.out.printf("   - [%s] %s (by %s)%n",
                m.getChannelId(), m.getContent(), createdUser.getUsername()));
    }
}
