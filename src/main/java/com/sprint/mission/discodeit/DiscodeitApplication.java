package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.UUID;

@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class DiscodeitApplication {

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;
    private final ReadStatusService readStatusService;

    public DiscodeitApplication(
            UserService userService,
            @Qualifier("basicChannelService") ChannelService channelService,
            @Qualifier("basicMessageService") MessageService messageService,
            ReadStatusService readStatusService
    ) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageService = messageService;
        this.readStatusService = readStatusService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DiscodeitApplication.class, args);
        System.out.println("🚀 DiscodeitApplication 실행 완료! (DB 없이 실행)");
    }

    @Bean
    CommandLineRunner run() {
        return args -> {
            System.out.println("✅ CommandLineRunner 실행됨!");

            UserCreateDTO userDTO = new UserCreateDTO("Amy", "amy@example.com", null, "password123");
            userService.create(userDTO);

            List<UserReadDTO> users = userService.readAll();
            if (users.isEmpty()) {
                throw new RuntimeException("🚨 사용자 생성 실패: User 목록이 비어 있음");
            }
            UserReadDTO createdUser = users.get(0);
            UUID userId = createdUser.getId();

            System.out.printf("✅ 사용자 등록 완료: %s (%s)%n", createdUser.getUsername(), createdUser.getEmail());

            UUID channelId = UUID.randomUUID();
            ChannelCreateDTO channelCreateDTO = new ChannelCreateDTO("Second 채널", "설명", userId);
            channelService.createPublicChannel(channelCreateDTO);
            System.out.printf("✅ 채널 등록 완료: %s%n", channelCreateDTO.getName());

            MessageCreateDTO messageCreateDTO = new MessageCreateDTO(userId, channelId, "첫 번째 메시지!", null);
            messageService.create(messageCreateDTO);
            System.out.printf("✅ 메시지 등록 완료: %s%n", messageCreateDTO.getContent());
        };
    }
}
