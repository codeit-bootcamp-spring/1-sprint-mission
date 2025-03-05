package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableJpaRepositories("com.sprint.mission.discodeit.repository")
@RequiredArgsConstructor
public class DiscodeitApplication implements CommandLineRunner {

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;
    private final ReadStatusService readStatusService;

    public static void main(String[] args) {
        SpringApplication.run(DiscodeitApplication.class, args);
        System.out.println("🚀 DiscodeitApplication 실행 완료!");
    }

    @Override
    public void run(String... args) {
        System.out.println("✅ CommandLineRunner 실행됨!");

        // ✅ 사용자 생성
        UserCreateRequest userDTO = new UserCreateRequest("Amy", "amy@example.com", null, "password123");
        userService.create(userDTO);

        List<UserReadResponse> users = userService.readAll();
        if (users.isEmpty()) {
            throw new RuntimeException("🚨 사용자 생성 실패: User 목록이 비어 있음");
        }

        UserReadResponse createdUser = users.stream()
                .filter(u -> u.getEmail().equals(userDTO.getEmail()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("🚨 사용자 생성 실패: Email 일치 사용자 없음"));

        UUID userId = createdUser.getId();

        // ✅ 채널 생성 (공개/비공개 여부에 따라 처리)
        ChannelCreateRequest channelCreateRequest = new ChannelCreateRequest(
                "Second 채널", "업데이트된 채널 설명", userId, false, List.of(userId)
        );

        ChannelResponse createdChannel;
        if (channelCreateRequest.isPrivate()) {
            createdChannel = channelService.createPrivateChannel(channelCreateRequest);
        } else {
            createdChannel = channelService.createPublicChannel(channelCreateRequest);
        }

        // ✅ 메시지 생성
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(userId, createdChannel.getId(), "안녕하세요, 첫 번째 메시지입니다!");
        messageService.create(messageCreateRequest);

        System.out.println("📌 현재 등록된 메시지 목록:");
        List<MessageResponse> messages = messageService.readAllByChannel(createdChannel.getId());
        messages.forEach(m -> System.out.printf("   - [%s] %s (by %s)%n",
                m.getChannelId(), m.getContent(), createdUser.getUsername()));
    }
}
